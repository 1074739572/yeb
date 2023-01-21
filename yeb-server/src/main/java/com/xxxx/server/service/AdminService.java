package com.xxxx.server.service;

import com.xxxx.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xxxx.server.pojo.Menu;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
public interface AdminService extends IService<Admin> {

    /**
     * 登录之后返回token
     *
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    RespBean login(String username, String password, String code, HttpServletRequest request);

    /**
     * 根据用户名实现用户
     * @param username
     * @return
     */
    Admin getAdminByUserName(String username);

    /**
     * 根据将用户名id查询角色列表
     * @param adminId
     * @return
     */
    List<Role> getRoles(Integer adminId);

    List<Admin> getAllAdmins(String keywords);

    RespBean updateAdminRole(Integer adminId, Integer[] rids);

    RespBean updateAdminPassword(String oldPass, String pass, Integer adminId);

    RespBean updateAdminUserFace(String url, Integer id, Authentication authentication);
}
