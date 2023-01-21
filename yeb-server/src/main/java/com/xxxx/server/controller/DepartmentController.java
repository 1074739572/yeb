package com.xxxx.server.controller;


import com.xxxx.server.pojo.Department;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.service.DepartmentService;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
@RestController
@RequestMapping("/sys/basic/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;

    @ApiOperation(value = "获取所有部门")
    @GetMapping("/")
    public List<Department> getAllDepartment(){
        return departmentService.getAllDepartment();
    }

    @ApiOperation(value = "添加部门")
    @PostMapping("/add")
    public RespBean addDep(@RequestBody Department dep){
        return departmentService.addDep(dep);
    }
    @ApiOperation(value = "删除部门")
    @PostMapping("/{id}")
    public RespBean deleteDep(@PathVariable Integer id){
        return departmentService.deleteDep(id);
    }
}

