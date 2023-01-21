package com.xxxx.server.mapper;

import com.xxxx.server.pojo.Menu;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
public interface MenuMapper extends BaseMapper<Menu> {


    List<Menu> getMenusByAdminId(Integer id);
    List<Menu> getMenusWithRole();

    List<Role> getAllMenus();
}
