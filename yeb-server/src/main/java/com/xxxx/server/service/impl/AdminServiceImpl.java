package com.xxxx.server.service.impl;



import com.alibaba.druid.util.StringUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xxxx.server.Utlis.AdminUtlis;
import com.xxxx.server.config.security.component.JwtTokenUtil;
import com.xxxx.server.mapper.AdminRoleMapper;
import com.xxxx.server.mapper.RoleMapper;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.pojo.AdminRole;
import com.xxxx.server.pojo.RespBean;
import com.xxxx.server.pojo.Role;
import com.xxxx.server.service.AdminService;
import com.xxxx.server.mapper.AdminMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author Bing
 * @since 2022-11-09
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements AdminService {

    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private UserDetailsService userDetailsServices;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RoleMapper roleMapper;

    @Autowired
    private AdminRoleMapper adminRoleMapper;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    /**
     * 登录之后返回token
     *
     * @param username
     * @param password
     * @param code
     * @param request
     * @return
     */
    @Override
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        String captcha = (String) request.getSession().getAttribute("captcha");
        if (StringUtils.isEmpty(code) || ! captcha.equalsIgnoreCase(code) ){
            return RespBean.error("验证码的痛苦");
        }
        //登录
        UserDetails userDetails=userDetailsServices.loadUserByUsername(username);
//        if (null == userDetails || passwordEncoder.matches(password, userDetails.getPassword())) {
//            return RespBean.error("判定的问题");
//        }
        if(!userDetails.isEnabled()){
            return RespBean.error("账户被禁用,请联系管理员!");
        }
        //更新登录用户对象
        UsernamePasswordAuthenticationToken authenticationToken
                =new UsernamePasswordAuthenticationToken(userDetails
        ,null,userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //生成Tken
        String token= jwtTokenUtil.generateToken(userDetails);
        System.out.println(token);
        Map<String ,String> tokenMap=new HashMap<>();
        tokenMap.put("token",token);
        tokenMap.put("tokenHead",tokenHead);
        return RespBean.success("登陆成功",tokenMap);
    }

    /**
     * 根据用户名获得用户
     * @param username
     * @return
     */
    @Override
    public Admin getAdminByUserName(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username",username).eq("enabled",true));
    }

    @Override
    public List<Role> getRoles(Integer adminId) {
        return roleMapper.getRoles(adminId);
    }

    @Override
    public List<Admin> getAllAdmins(String keywords) {
        return adminMapper.getAllAdmins(AdminUtlis.getCurrentAdmin().getId(),keywords);
    }

    @Override
    public RespBean updateAdminRole(Integer adminId, Integer[] rids) {
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId",adminId));
        Integer result=adminRoleMapper.addAdminRole(adminId,rids);
        if(rids.length==result){
            return RespBean.success("添加成功!");
        }
        return RespBean.error("更新失败!");
    }

    /**
     * 更新用户密码
     *
     * @param oldPass
     * @param pass
     * @param adminId
     * @return
     */
    @Override
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = baseMapper.selectById(adminId);
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        // 比对密码，判断旧密码是否正确
        if (encoder.matches(oldPass, admin.getPassword())) {
            // 设置密码，并加密
            admin.setPassword(encoder.encode(pass));
            int result = baseMapper.updateById(admin);
            if (1 == result) {
                return RespBean.success("更新成功！");
            }
        }
        return RespBean.error("更新失败！");
    }

    @Override
    public RespBean updateAdminUserFace(String url, Integer id, Authentication authentication) {
        Admin admin=adminMapper.selectById(id);
        admin.setUserFace(url);
        int result=adminMapper.updateById(admin);
        if(1==result){
            Admin principal = (Admin) authentication.getPrincipal();
            principal.setUserFace(url);
            SecurityContextHolder.getContext().setAuthentication(
                    new UsernamePasswordAuthenticationToken(admin,null,authentication.getAuthorities()));
            return RespBean.success("更新成功",url);
        }
        return RespBean.success("更新失败");
    }
}
