package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.ApplicationStatusConstant;
import com.sky.constant.AssetStatusConstant;
import com.sky.constant.MessageConstant;
import com.sky.context.BaseContext;
import com.sky.dto.RepairPageQueryDTO;
import com.sky.dto.RepairRecordDTO;
import com.sky.entity.Asset;
import com.sky.entity.AssetApplication;
import com.sky.entity.RepairRecord;
import com.sky.exception.BaseException;
import com.sky.mapper.AssetApplicationMapper;
import com.sky.mapper.AssetMapper;
import com.sky.mapper.RepairRecordMapper;
import com.sky.result.PageResult;
import com.sky.service.RepairService;
import com.sky.vo.RepairRecordVO;
import com.sky.websocket.WebSocketServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@Slf4j
public class RepairServiceImpl implements RepairService {

    @Autowired
    private RepairRecordMapper repairRecordMapper;

    @Autowired
    private AssetApplicationMapper assetApplicationMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private WebSocketServer webSocketServer;

    /**
     * 员工提交报修：校验申请单与资产，资产状态变维修中
     */
    @Override
    @Transactional
    public void submit(RepairRecordDTO repairRecordDTO) {
        AssetApplication application = repairRecordDTO.getApplicationId() == null
                ? null : assetApplicationMapper.getById(repairRecordDTO.getApplicationId());
        Asset asset = repairRecordDTO.getAssetId() == null ? null : assetMapper.getById(repairRecordDTO.getAssetId());
        if (application == null) {
            throw new BaseException(MessageConstant.APPLICATION_NOT_FOUND);
        }
        if (asset == null) {
            throw new BaseException(MessageConstant.ASSET_NOT_FOUND);
        }

        LocalDateTime now = LocalDateTime.now();
        RepairRecord record = new RepairRecord();
        record.setApplicationId(application.getId());
        record.setAssetId(asset.getId());
        record.setDescription(repairRecordDTO.getDescription());
        record.setRepairCompany(repairRecordDTO.getRepairCompany());
        record.setFee(repairRecordDTO.getFee());
        record.setResult(repairRecordDTO.getResult());
        record.setRepairTime(repairRecordDTO.getResult() == null || repairRecordDTO.getResult().isEmpty() ? null : now);
        record.setCreateTime(now);
        record.setUpdateTime(now);
        record.setCreateUser(BaseContext.getCurrentId());
        repairRecordMapper.insert(record);

        // 资产状态变维修中
        assetMapper.updateStatus(Asset.builder()
                .id(asset.getId())
                .status(AssetStatusConstant.REPAIRING)
                .build());

        webSocketServer.sendToAllClient("新增报修记录：" + application.getApplicationNo());
    }

    /**
     * 管理端：报修记录分页查询
     */
    @Override
    public PageResult pageQuery(RepairPageQueryDTO repairPageQueryDTO) {
        PageHelper.startPage(repairPageQueryDTO.getPage(), repairPageQueryDTO.getPageSize());
        Page<RepairRecordVO> page = repairRecordMapper.pageQuery(repairPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 员工端：查询我发起的报修记录
     */
    @Override
    public PageResult pageQueryByEmployee(RepairPageQueryDTO repairPageQueryDTO) {
        repairPageQueryDTO.setEmployeeId(BaseContext.getCurrentId());
        return pageQuery(repairPageQueryDTO);
    }

    /**
     * 报修记录详情
     */
    @Override
    public RepairRecordVO getById(Long id) {
        RepairRecord record = repairRecordMapper.getById(id);
        if (record == null) {
            throw new BaseException(MessageConstant.REPAIR_NOT_FOUND);
        }

        RepairRecordVO vo = new RepairRecordVO();
        vo.setId(record.getId());
        vo.setApplicationId(record.getApplicationId());
        vo.setAssetId(record.getAssetId());
        vo.setDescription(record.getDescription());
        vo.setRepairCompany(record.getRepairCompany());
        vo.setFee(record.getFee());
        vo.setResult(record.getResult());
        vo.setRepairTime(record.getRepairTime());
        vo.setCreateTime(record.getCreateTime());

        AssetApplication application = assetApplicationMapper.getById(record.getApplicationId());
        if (application != null) {
            vo.setApplicationNo(application.getApplicationNo());
        }
        Asset asset = assetMapper.getById(record.getAssetId());
        if (asset != null) {
            vo.setAssetCode(asset.getCode());
            vo.setAssetName(asset.getName());
        }
        return vo;
    }

    /**
     * 管理端完成维修：填写维修公司/费用/结果，资产状态回在库，关联申请单完成
     */
    @Override
    @Transactional
    public void complete(Long id, RepairRecordDTO repairRecordDTO) {
        RepairRecord record = repairRecordMapper.getById(id);
        if (record == null) {
            throw new BaseException(MessageConstant.REPAIR_NOT_FOUND);
        }

        LocalDateTime now = LocalDateTime.now();
        record.setRepairCompany(repairRecordDTO.getRepairCompany());
        record.setFee(repairRecordDTO.getFee());
        record.setResult(repairRecordDTO.getResult());
        record.setRepairTime(now);
        record.setUpdateTime(now);
        repairRecordMapper.update(record);

        // 资产状态回在库
        assetMapper.updateStatus(Asset.builder()
                .id(record.getAssetId())
                .status(AssetStatusConstant.IN_STOCK)
                .build());

        // 关联申请单完成
        AssetApplication application = assetApplicationMapper.getById(record.getApplicationId());
        if (application != null && application.getStatus() == ApplicationStatusConstant.APPROVED) {
            application.setStatus(ApplicationStatusConstant.COMPLETED);
            application.setCompleteTime(now);
            application.setUpdateTime(now);
            assetApplicationMapper.update(application);
        }

        webSocketServer.sendToAllClient("维修完成，资产已回库");
    }
}