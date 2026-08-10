package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.ApplicationStatusConstant;
import com.sky.constant.AssetStatusConstant;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.AssetApplicationAuditDTO;
import com.sky.dto.AssetApplicationDTO;
import com.sky.dto.AssetApplicationPageQueryDTO;
import com.sky.entity.Asset;
import com.sky.entity.AssetApplication;
import com.sky.entity.Employee;
import com.sky.exception.BaseException;
import com.sky.mapper.AssetApplicationMapper;
import com.sky.mapper.AssetMapper;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.AssetApplicationService;
import com.sky.vo.AssetApplicationVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@Slf4j
public class AssetApplicationServiceImpl implements AssetApplicationService {

    @Autowired
    private AssetApplicationMapper assetApplicationMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private EmployeeMapper employeeMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 员工提交资产申请：申请时资产须为在库状态
     */
    @Override
    @Transactional
    public AssetApplicationVO submit(AssetApplicationDTO assetApplicationDTO) {
        if (assetApplicationDTO.getAssetId() == null) {
            throw new BaseException(MessageConstant.APPLICATION_ASSET_REQUIRED);
        }
        Asset asset = assetMapper.getById(assetApplicationDTO.getAssetId());
        if (asset == null) {
            throw new BaseException(MessageConstant.ASSET_NOT_FOUND);
        }
        if (asset.getStatus() != AssetStatusConstant.IN_STOCK) {
            throw new BaseException(MessageConstant.ASSET_STATUS_ERROR);
        }

        Long employeeId = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();
        AssetApplication application = new AssetApplication();
        application.setApplicationNo("APP" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
                + (int) (Math.random() * 900 + 100));
        application.setEmployeeId(employeeId);
        application.setAssetId(asset.getId());
        application.setQuantity(assetApplicationDTO.getQuantity() == null ? 1 : assetApplicationDTO.getQuantity());
        application.setReason(assetApplicationDTO.getReason());
        application.setRemark(assetApplicationDTO.getRemark());
        application.setStatus(ApplicationStatusConstant.PENDING);
        application.setApplyTime(now);
        application.setCreateTime(now);
        application.setUpdateTime(now);
        assetApplicationMapper.insert(application);

        // WebSocket 通知管理端有新申请待审批
        webSocketServer.sendToAllClient("新资产申请待审批：" + application.getApplicationNo());

        return getById(application.getId());
    }

    /**
     * 管理端：申请单分页查询
     */
    @Override
    public PageResult pageQuery(AssetApplicationPageQueryDTO assetApplicationPageQueryDTO) {
        PageHelper.startPage(assetApplicationPageQueryDTO.getPage(), assetApplicationPageQueryDTO.getPageSize());
        Page<AssetApplicationVO> page = assetApplicationMapper.pageQuery(assetApplicationPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 员工端：查询我提交的申请单
     */
    @Override
    public PageResult pageQueryByEmployee(AssetApplicationPageQueryDTO assetApplicationPageQueryDTO) {
        assetApplicationPageQueryDTO.setEmployeeId(BaseContext.getCurrentId());
        return pageQuery(assetApplicationPageQueryDTO);
    }

    /**
     * 申请单详情
     */
    @Override
    public AssetApplicationVO getById(Long id) {
        AssetApplication application = assetApplicationMapper.getById(id);
        if (application == null) {
            throw new BaseException(MessageConstant.APPLICATION_NOT_FOUND);
        }
        AssetApplicationVO vo = new AssetApplicationVO();
        BeanUtils.copyProperties(application, vo);

        Employee employee = employeeMapper.getById(application.getEmployeeId());
        if (employee != null) {
            vo.setEmployeeName(employee.getName());
            vo.setEmployeeDepartment(employee.getDepartment());
        }
        Asset asset = assetMapper.getById(application.getAssetId());
        if (asset != null) {
            vo.setAssetCode(asset.getCode());
            vo.setAssetName(asset.getName());
        }
        return vo;
    }

    /**
     * 管理端审批：通过 → 资产变已领用；拒绝 → 需填写拒绝原因
     */
    @Override
    @Transactional
    public void audit(AssetApplicationAuditDTO auditDTO) {
        AssetApplication application = assetApplicationMapper.getById(auditDTO.getId());
        if (application == null) {
            throw new BaseException(MessageConstant.APPLICATION_NOT_FOUND);
        }
        if (application.getStatus() != ApplicationStatusConstant.PENDING) {
            throw new BaseException(MessageConstant.APPLICATION_STATUS_ERROR);
        }

        Long approverId = BaseContext.getCurrentId();
        LocalDateTime now = LocalDateTime.now();
        application.setApproveTime(now);
        application.setApproverId(approverId);
        application.setUpdateTime(now);

        if (auditDTO.getStatus() == ApplicationStatusConstant.APPROVED) {
            application.setStatus(ApplicationStatusConstant.APPROVED);
            application.setRejectReason(null);
            // 资产变为已领用
            assetMapper.updateStatus(Asset.builder()
                    .id(application.getAssetId())
                    .status(AssetStatusConstant.USED)
                    .build());
            webSocketServer.sendToAllClient("申请已通过：" + application.getApplicationNo());
        } else if (auditDTO.getStatus() == ApplicationStatusConstant.REJECTED) {
            if (auditDTO.getRejectReason() == null || auditDTO.getRejectReason().isEmpty()) {
                throw new BaseException("拒绝时必须填写拒绝原因");
            }
            application.setStatus(ApplicationStatusConstant.REJECTED);
            application.setRejectReason(auditDTO.getRejectReason());
            webSocketServer.sendToAllClient("申请已被拒绝：" + application.getApplicationNo());
        } else {
            throw new BaseException(MessageConstant.APPLICATION_STATUS_ERROR);
        }
        assetApplicationMapper.update(application);
    }

    /**
     * 管理端标记完成：资产归还，状态回在库
     */
    @Override
    @Transactional
    public void complete(Long id) {
        AssetApplication application = assetApplicationMapper.getById(id);
        if (application == null) {
            throw new BaseException(MessageConstant.APPLICATION_NOT_FOUND);
        }
        if (application.getStatus() != ApplicationStatusConstant.APPROVED) {
            throw new BaseException(MessageConstant.APPLICATION_STATUS_ERROR);
        }
        application.setStatus(ApplicationStatusConstant.COMPLETED);
        application.setCompleteTime(LocalDateTime.now());
        application.setUpdateTime(LocalDateTime.now());
        assetApplicationMapper.update(application);

        // 资产归还：已领用 → 在库
        Asset asset = assetMapper.getById(application.getAssetId());
        if (asset != null && asset.getStatus() == AssetStatusConstant.USED) {
            assetMapper.updateStatus(Asset.builder()
                    .id(asset.getId())
                    .status(AssetStatusConstant.IN_STOCK)
                    .build());
        }
        webSocketServer.sendToAllClient("申请单已完成：" + application.getApplicationNo());
    }
}