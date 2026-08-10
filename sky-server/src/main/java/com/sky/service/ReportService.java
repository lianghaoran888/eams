package com.sky.service;

import com.sky.vo.AssetOverviewVO;

public interface ReportService {

    /**
     * 资产总览：总数/在库/已领用/维修中/报废
     */
    AssetOverviewVO overview();
}