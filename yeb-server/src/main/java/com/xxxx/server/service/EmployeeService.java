package com.xxxx.server.service;

import com.xxxx.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
public interface EmployeeService extends IService<Employee> {

    RespPageBean getEmployeeByPage(Integer currenPage,
                                   Integer size,
                                   Employee employee,
                                   LocalDate[] beginDateScope);

    RespBean maxWorkId();

    /**
     * 添加员工
     * @param employee
     * @return
     */
    RespBean addEmp(Employee employee);

    /**
     * 查询员工
     * @param id
     * @return
     */
    List<Employee> getEmployee(Integer id);

    RespPageBean getEmployeeWithSalary(Integer currentPage, Integer size);
}
