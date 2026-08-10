package com.eams.controller.admin;

import com.eams.constant.MessageConstant;
import com.eams.result.Result;
import com.eams.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

/**
 * 通用接口
 */
@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    /**
     * 文件上传
     */
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("/upload")
    public Result<String> upload(MultipartFile file) {
        log.info("文件上传: {}", file);
        try {
            // 获取文件原始名
            String originalFilename = file.getOriginalFilename();
            // 截取后缀名 .png/.jpg
            String substring = originalFilename.substring(originalFilename.lastIndexOf("."));
            // 用UUID拼接
            String objectName = UUID.randomUUID().toString() + substring;

            // 文件请求路径
            String filePath = aliOssUtil.upload(file.getBytes(), objectName);
            return Result.success(filePath);
        } catch (Exception e) {
            log.error("文件上传失败: {}", e);
        }
        return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
