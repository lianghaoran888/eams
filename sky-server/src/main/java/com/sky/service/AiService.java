package com.sky.service;

import com.sky.entity.AssetApplication;
import com.sky.vo.AssetVO;

import java.util.List;

/**
 * Spring AI 智能服务：智能审批建议 / 自然语言检索 / 图片识别
 */
public interface AiService {

    /**
     * 智能审批建议：根据申请单内容生成审批建议
     *
     * @param application 申请单
     * @return 建议文本
     */
    String analyzeApplication(AssetApplication application);

    /**
     * 自然语言检索：根据自然语言查询资产
     *
     * @param keyword 自然语言描述
     * @return 匹配的资产列表
     */
    List<AssetVO> searchByNaturalLanguage(String keyword);

    /**
     * 图片识别：识别资产图片中的资产信息
     *
     * @param imageUrl 图片地址
     * @return 识别结果
     */
    Object recognizeAsset(String imageUrl);
}