package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface SetMealMapper {

    /**
     * 根据分类id查询套餐的数量
     */
    @Select("select count(*) from setmeal where category_id = #{categoryId}")
    Integer countByCategoryId(Long id);

    /**
     * 套餐分页条件查询
     */
    Page<SetmealVO> pageQuery(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 新增套餐 : 插入1条套餐数据
     */
    @AutoFill(OperationType.INSERT)
    void insert(Setmeal setmeal);

    /**
     * 批量删除套餐 :
     * 判断是否为启售状态
     */
    @Select("select setmeal.status from setmeal where id = #{id}")
    Setmeal selectStatus(Long id);

    /**
     * 批量删除套餐 :
     * 批量删除套餐
     */
    void deleteBatch(List<Long> ids);

    /**
     * 修改套餐 : 查询回显
     * 根据id查询套餐数据
     */
    @Select("select * from setmeal where id = #{id}")
    Setmeal queryById(Long id);

    /**
     * 修改套餐 :
     * 修改套餐表基本信息
     */
    @AutoFill(OperationType.UPDATE)
    void update(Setmeal setmeal);

    /**
     * 更新套餐启售,停售状态
     */
    @Update("update setmeal set status = #{status} where id= #{id}")
    void updateStatus(Setmeal setmeal);

    /**
     * 用户端:
     * 动态条件查询套餐
     */
    List<Setmeal> list(Setmeal setmeal);

    /**
     * 用户端:
     * 根据套餐id查询菜品选项
     */
    @Select("select sd.name, sd.copies, d.image, d.description " +
            "from setmeal_dish sd left join dish d on sd.dish_id = d.id " +
            "where sd.setmeal_id = #{setmealId}")
    List<DishItemVO> getDishItemBySetmealId(Long setmealId);
}
