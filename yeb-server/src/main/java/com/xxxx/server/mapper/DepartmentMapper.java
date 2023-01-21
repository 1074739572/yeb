package com.xxxx.server.mapper;

import com.xxxx.server.pojo.Department;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
public interface DepartmentMapper extends BaseMapper<Department> {

    List<Department> getAllDepartment(Integer parentId);

    void addDep(Department dep);

    void deleteDep(Department dep);
}
