package com.xxxx.server.service;

import com.xxxx.server.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
public interface DepartmentService extends IService<Department> {


    List<Department> getAllDepartment();

    RespBean addDep(Department dep);

    RespBean deleteDep(Integer id);
}
