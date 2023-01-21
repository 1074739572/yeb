package com.xxxx.server.Utlis;

import com.xxxx.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

public class AdminUtlis {
    /*
    获取当前操作员
     */
    public static Admin getCurrentAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
