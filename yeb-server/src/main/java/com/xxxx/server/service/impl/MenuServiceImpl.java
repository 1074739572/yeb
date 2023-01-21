package com.xxxx.server.service.impl;

import com.xxxx.server.Utlis.AdminUtlis;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.mapper.MenuMapper;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.MenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private MenuMapper menuMapper;
    @Autowired
    private RedisTemplate<String ,Object> redisTemplate;
    @Override
    public List<Menu> getMenusByAdminId() {
        Integer adminId= AdminUtlis.getCurrentAdmin().getId();
        ValueOperations<String , Object> valueOperations=redisTemplate.opsForValue();
        //从redia获取菜单数据
        List menus = (List<Menu>) valueOperations.get("menu_"+adminId);
        //如果为空,去数据库获取
        if(CollectionUtils.isEmpty(menus)){
            menus=menuMapper.getMenusByAdminId(adminId);
            //将数据库设置到Redia中
            valueOperations.set("menu"+adminId,menus);
        }
        return menus;
    }

    @Override
    public List<Role> getAllmenus() {
        return menuMapper.getAllMenus();
    }

    @Override
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }
}
