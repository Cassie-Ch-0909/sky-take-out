package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation(value = "员工登录")
    // @RequestBody EmployeeLoginDTO employeeLoginDTO: 这是Spring的注解，用于将HTTP请求体中的JSON数据转换为EmployeeLoginDTO对象。
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);

        Employee employee = employeeService.login(employeeLoginDTO);

        // 登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        // 在claims中添加一个键值对，其中键是JwtClaimsConstant.EMP_ID，值是登录成功的员工的ID EMP_ID就是employee_id
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        // 使用JwtUtil.createJWT方法生成一个JWT令牌
        String token = JwtUtil.createJWT(
                // JWT 令牌的密钥，通常是一个加密字符串
                jwtProperties.getAdminSecretKey(),
                // JWT 令牌的过期时间，以毫秒为单位，即令牌的有效期限
                jwtProperties.getAdminTtl(),
                claims);
        // 登录成功后返回给前端的响应对象
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .userName(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }

    /**
     * 退出
     *
     * @return
     */
    @PostMapping("/logout")
    @ApiOperation("员工退出")
    public Result<String> logout() {
        return Result.success();
    }

    /**
     * 新增员工
     * @param employeeDTO
     * @return
     */
    /*
     * @PostMapping用于处理客户端发送的HTTP POST请求，
     * 相当于@RequestMapping(method = RequestMethod.POST)的简写
     * */
    @PostMapping
    @ApiOperation("新增员工")
    /*
     * @RequestBody 将客户端发送的HTTP请求体中的JSON或XML数据绑定到方法参数上。
     * @RequestBody 将HTTP 请求体 中的数据转换为Java对象，并作为方法参数传递
     * */
    public Result save(@RequestBody EmployeeDTO employeeDTO) {
        // 这里的{}是一个占位符，在log日志中employeeDTO的内容会补充到{}的位置
        log.info("新增员工：{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     * @return
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    // 因为这是一个query传参，也就是地址栏？的形式，而不是json传参，所以不需要加@RequestBody注解
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO){
        log.info("员工分页查询，参数为：{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用员工账号")
    // 通过PathVariable来取@PostMapping("/status/{status}")中的路径参数
    public Result startOrStop(@PathVariable Integer status, Long id){
        log.info("启用禁用员工账号，参数为：{},{}",status,id);
        employeeService.startOrStop(status,id);
        return Result.success();
    }
}
