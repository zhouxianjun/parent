package com.gary.dao.mybatis;

import com.gary.dao.result.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * @author zhouxianjun(Gary)
 * @ClassName:
 * @Description:
 * @date 2015/4/24 14:54
 */
public interface Mapper<E, I> {
    /**
     * 保存
     * @param e
     * @return
     */
    int save(E e);

    /**
     * 更新
     * @param e
     * @return
     */
    int update(E e);

    /**
     * 删除
     * @param id
     * @return
     */
    int del(@Param("id") I id);

    /**
     * 根据唯一ID获取
     * @param id
     * @return
     */
    E get(@Param("id") I id);

    /**
     * 根据查询条件获取
     * @param params
     * @return
     */
    E get(@Param("params") Map<String, Object> params);

    /**
     * 使用 DUPLICATE KEY UPDATE
     * @param e
     * @return
     */
    int saveOrUpdate(E e);

    /**
     * 分页查询
     * @param page
     * @param params
     * @return
     */
    List<E> selectPage(@Param("page") Page<E> page, @Param("params") Map<String, Object> params);
}
