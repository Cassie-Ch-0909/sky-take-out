package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Author: Cassie
 * Date: 2024/6/22 20:41
 * Description:分类管理
 */

// 一个 Spring 注解，标记该类是一个控制器，用于处理 HTTP 请求
@RestController
// 定义了基本的 URL 路径映射。所有以 /admin/category 开头的请求路径都会映射到这个控制器的处理方法
@RequestMapping("/admin/category")
// 一个 Swagger 注解，用于生成 API 文档。它指定了该控制器包含的所有 API 的分组标签。
@Api(tags = "分类相关接口")
// 一个 Lombok 注解，用于自动生成 Logger 对象。Slf4j 是 Simple Logging Facade for Java 的缩写。
@Slf4j
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @PostMapping
    @ApiOperation("新增分类")
    public Result save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类, 参数为: {}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }
}
