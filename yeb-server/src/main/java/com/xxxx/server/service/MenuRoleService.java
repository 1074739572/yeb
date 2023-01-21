package com.xxxx.server.service;

import com.xxxx.server.pojo.MenuRole;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.RespBean;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
public interface MenuRoleService extends IService<MenuRole> {

    RespBean updateMenuRole(Integer rid, Integer[] mids);
}
