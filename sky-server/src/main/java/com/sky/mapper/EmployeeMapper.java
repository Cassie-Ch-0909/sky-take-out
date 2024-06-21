package com.sky.mapper;

import com.sky.entity.Employee;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入员工数据
     * 一个注解方式的MyBatis Mapper接口方法，用于向数据库的employee表中插入一条新的员工记录
     * insert into employee (...) 指定了插入操作的目标表是employee，括号内列出了要插入的列名。
     * #{...} 是MyBatis的占位符，它会从传入的employee对象中获取对应的属性值。
     * 例如，#{name} 会从employee对象中获取name属性的值。
     * @param employee
     */
    @Insert("insert into employee (name, username, password, phone, sex, id_number, create_time, update_time, create_user, update_user,status) " +
            "values " +
            "(#{name},#{username},#{password},#{phone},#{sex},#{idNumber},#{createTime},#{updateTime},#{createUser},#{updateUser},#{status})")
    void insert(Employee employee);

}
