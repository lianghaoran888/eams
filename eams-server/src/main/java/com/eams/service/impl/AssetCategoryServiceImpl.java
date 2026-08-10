package com.eams.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.eams.constant.MessageConstant;
import com.eams.constant.RedisConstant;
import com.eams.constant.StatusConstant;
import com.eams.dto.AssetCategoryDTO;
import com.eams.dto.AssetCategoryPageQueryDTO;
import com.eams.entity.AssetCategory;
import com.eams.exception.BaseException;
import com.eams.json.JacksonObjectMapper;
import com.eams.mapper.AssetCategoryMapper;
import com.eams.mapper.AssetMapper;
import com.eams.result.PageResult;
import com.eams.service.AssetCategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * 资产分类服务：查询启用分类列表时使用 Redis 缓存（1 小时过期），增删改后清理缓存
 */
@Service
@Slf4j
public class AssetCategoryServiceImpl implements AssetCategoryService {

    @Autowired
    private AssetCategoryMapper assetCategoryMapper;

    @Autowired
    private AssetMapper assetMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private JacksonObjectMapper jacksonObjectMapper;

    /**
     * 新增分类
     */
    @Override
    public void add(AssetCategoryDTO assetCategoryDTO) {
        AssetCategory category = new AssetCategory();
        BeanUtils.copyProperties(assetCategoryDTO, category);
        category.setStatus(StatusConstant.ENABLE);
        assetCategoryMapper.insert(category);
        evictCache();
    }

    /**
     * 分类分页查询
     */
    @Override
    public PageResult pageQuery(AssetCategoryPageQueryDTO assetCategoryPageQueryDTO) {
        PageHelper.startPage(assetCategoryPageQueryDTO.getPage(), assetCategoryPageQueryDTO.getPageSize());
        Page<AssetCategory> page = assetCategoryMapper.pageQuery(assetCategoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * 根据 id 删除分类（分类下存在资产则不允许删除）
     */
    @Override
    public void deleteById(Long id) {
        Integer count = assetMapper.countByCategoryId(id);
        if (count != null && count > 0) {
            throw new BaseException(MessageConstant.CATEGORY_BE_RELATED_BY_ASSET);
        }
        assetCategoryMapper.deleteById(id);
        evictCache();
    }

    /**
     * 修改分类
     */
    @Override
    public void update(AssetCategoryDTO assetCategoryDTO) {
        AssetCategory category = new AssetCategory();
        BeanUtils.copyProperties(assetCategoryDTO, category);
        assetCategoryMapper.update(category);
        evictCache();
    }

    /**
     * 启用/禁用分类状态
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        AssetCategory category = AssetCategory.builder()
                .status(status)
                .id(id)
                .build();
        assetCategoryMapper.update(category);
        evictCache();
    }

    /**
     * 查询启用状态的全部分类：优先读 Redis 缓存，未命中则查库并回填（1 小时过期）
     */
    @Override
    public List<AssetCategory> list() {
        String cached = stringRedisTemplate.opsForValue().get(RedisConstant.CATEGORY_CACHE_KEY);
        if (cached != null && !cached.isEmpty()) {
            try {
                return jacksonObjectMapper.readValue(cached, new TypeReference<List<AssetCategory>>() {});
            } catch (Exception e) {
                log.warn("解析分类缓存失败，回源数据库: {}", e.getMessage());
            }
        }

        List<AssetCategory> list = assetCategoryMapper.list();
        try {
            stringRedisTemplate.opsForValue().set(
                    RedisConstant.CATEGORY_CACHE_KEY,
                    jacksonObjectMapper.writeValueAsString(list),
                    RedisConstant.CATEGORY_CACHE_TTL,
                    TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("写入分类缓存失败: {}", e.getMessage());
        }
        return list;
    }

    /**
     * 清理分类缓存
     */
    private void evictCache() {
        stringRedisTemplate.delete(RedisConstant.CATEGORY_CACHE_KEY);
    }
}