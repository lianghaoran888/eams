package com.eams.mapper;

import com.github.pagehelper.Page;
import com.eams.dto.RepairPageQueryDTO;
import com.eams.entity.RepairRecord;
import com.eams.vo.RepairRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface RepairRecordMapper {

    /**
     * 插入报修记录
     */
    void insert(RepairRecord record);

    /**
     * 报修记录分页查询（含申请单/资产信息）
     */
    Page<RepairRecordVO> pageQuery(RepairPageQueryDTO repairPageQueryDTO);

    /**
     * 根据 id 查询报修记录
     */
    @Select("select * from repair_record where id = #{id}")
    RepairRecord getById(Long id);

    /**
     * 修改报修记录
     */
    void update(RepairRecord record);
}