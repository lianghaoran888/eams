package com.eams.service;

import com.eams.dto.AssetApplicationAuditDTO;
import com.eams.dto.AssetApplicationDTO;
import com.eams.dto.AssetApplicationPageQueryDTO;
import com.eams.result.PageResult;
import com.eams.vo.AssetApplicationVO;

public interface AssetApplicationService {

    /**
     * 员工提交资产申请
     */
    AssetApplicationVO submit(AssetApplicationDTO assetApplicationDTO);

    /**
     * 管理端：申请单分页查询
     */
    PageResult pageQuery(AssetApplicationPageQueryDTO assetApplicationPageQueryDTO);

    /**
     * 员工端：查询我提交的申请单
     */
    PageResult pageQueryByEmployee(AssetApplicationPageQueryDTO assetApplicationPageQueryDTO);

    /**
     * 申请单详情
     */
    AssetApplicationVO getById(Long id);

    /**
     * 管理端：审批（通过/拒绝），通过后资产变已领用
     */
    void audit(AssetApplicationAuditDTO assetApplicationAuditDTO);

    /**
     * 管理端：标记已完成（资产归还，在库）
     */
    void complete(Long id);
}