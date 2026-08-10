package com.sky.controller.admin;

import com.sky.annotation.RateLimit;
import com.sky.entity.AssetApplication;
import com.sky.result.Result;
import com.sky.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 管理端：Spring AI 智能接口（智能审批建议 / 自然语言检索 / 图片识别）
 * 三个接口均受 Redis 限流保护：每分钟最多 10 次
 */
@RestController
@RequestMapping("/admin/ai")
@Slf4j
public class AiController {

    @Autowired
    private AiService aiService;

    /**
     * 智能审批建议
     */
    @PostMapping("/analyzeApplication")
    @RateLimit(key = "analyzeApplication", limit = 10, period = 60)
    public Result<String> analyzeApplication(@RequestBody AssetApplication application) {
        log.info("智能审批建议：{}", application == null ? null : application.getId());
        return Result.success(aiService.analyzeApplication(application));
    }

    /**
     * 自然语言检索
     */
    @PostMapping("/search")
    @RateLimit(key = "searchByNaturalLanguage", limit = 10, period = 60)
    public Result searchByNaturalLanguage(@RequestParam String keyword) {
        log.info("自然语言检索：{}", keyword);
        return Result.success(aiService.searchByNaturalLanguage(keyword));
    }

    /**
     * 图片识别
     */
    @PostMapping("/recognize")
    @RateLimit(key = "recognizeAsset", limit = 10, period = 60)
    public Result recognizeAsset(@RequestParam String imageUrl) {
        log.info("图片识别：{}", imageUrl);
        return Result.success(aiService.recognizeAsset(imageUrl));
    }
}