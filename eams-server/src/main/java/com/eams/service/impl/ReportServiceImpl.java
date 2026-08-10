package com.eams.service.impl;

import com.eams.constant.AssetStatusConstant;
import com.eams.mapper.AssetMapper;
import com.eams.service.ReportService;
import com.eams.vo.AssetOverviewVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ReportServiceImpl implements ReportService {

    @Autowired
    private AssetMapper assetMapper;

    /**
     * 资产总览统计
     */
    @Override
    public AssetOverviewVO overview() {
        return AssetOverviewVO.builder()
                .total(assetMapper.countAll())
                .inStock(assetMapper.countByStatus(AssetStatusConstant.IN_STOCK))
                .used(assetMapper.countByStatus(AssetStatusConstant.USED))
                .repairing(assetMapper.countByStatus(AssetStatusConstant.REPAIRING))
                .scrapped(assetMapper.countByStatus(AssetStatusConstant.SCRAPPED))
                .build();
    }
}