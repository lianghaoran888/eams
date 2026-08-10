package com.eams.service;

import com.eams.dto.RepairPageQueryDTO;
import com.eams.dto.RepairRecordDTO;
import com.eams.result.PageResult;
import com.eams.vo.RepairRecordVO;

public interface RepairService {

    /**
     * 员工提交报修：资产状态变维修中
     */
    void submit(RepairRecordDTO repairRecordDTO);

    /**
     * 管理端：报修记录分页查询
     */
    PageResult pageQuery(RepairPageQueryDTO repairPageQueryDTO);

    /**
     * 员工端：查询我发起的报修记录
     */
    PageResult pageQueryByEmployee(RepairPageQueryDTO repairPageQueryDTO);

    /**
     * 报修记录详情
     */
    RepairRecordVO getById(Long id);

    /**
     * 管理端：完成维修（填写维修公司/费用/结果），资产状态回在库，关联申请单完成
     */
    void complete(Long id, RepairRecordDTO repairRecordDTO);
}