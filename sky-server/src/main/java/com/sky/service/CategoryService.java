package com.sky.service;

import com.sky.dto.CategoryDTO;

/**
 * Author: Cassie
 * Date: 2024/6/22 20:49
 * Description:
 */

public interface CategoryService {

    /**
     * 新增分类
     * @param categoryDTO
     */
    void save(CategoryDTO categoryDTO);
}
