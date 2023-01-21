package com.xxxx.server.service;

import com.xxxx.server.pojo.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Role;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
public interface MenuService extends IService<Menu> {

    /**
     * 根据用户id查询列表
     * @return
     */
    List<Menu> getMenusByAdminId();
    List<Menu> getMenusWithRole();

    /**
     * 查询所有菜饭
     * @return
     */
    List<Role> getAllmenus();
}
