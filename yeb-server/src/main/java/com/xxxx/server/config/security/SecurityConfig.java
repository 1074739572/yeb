package com.xxxx.server.config.security;


import com.xxxx.server.config.security.component.*;
import com.xxxx.server.pojo.Admin;
import com.xxxx.server.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private AdminService adminService;
    @Autowired
    private RestAuthorizationEntryPoint restAuthorizationEntryPoint;
    @Autowired
    private RestfulAccessReniedHandler restfulAccessReniedHandler;

    @Autowired
    private CustomFliter customFliter;

    @Autowired
    private CustomUrlDecisionManager customUrlDecisionManager;

    @Override
    // 2、让 Security 走我们重写的 UserDetailsService ，通过 getAdminByUserName 获取用户名
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService()).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // 使用 JWT , 不需要 csrf
        http.csrf()
                .disable()
                // 基于 token,不需要 session
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // 所有请求都要求认证
                .anyRequest()
                .authenticated()
                // 动态权限配置
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {

                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setAccessDecisionManager(customUrlDecisionManager);
                        object.setSecurityMetadataSource(customFliter);
                        return object;
                    }
                })
                .and()
                // 禁用缓存
                .headers()
                .cacheControl();

        // 添加 jwt 登录授权过滤器
        http.addFilterBefore(jwtAuthencationTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        // 添加自定义未授权和未登录结果返回
        http.exceptionHandling()
                .accessDeniedHandler(restfulAccessReniedHandler)
                .authenticationEntryPoint(restAuthorizationEntryPoint);
    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers(
                "/websocket/**",
                "/**.html",
                "/login/**",
                "//hello/**",
                "/register/**",
                "/logout/**",
                "/css/**",
                "/js/**",
                "/img/**",
                "/fonts/**",
                "favicon.ico",
                "/doc.html",                    // 放行 swagger 资源
                "/webjars/**",                  // 放行 swagger 资源
                "/swagger-resources/**",        // 放行 swagger 资源
                "/v2/api-docs/**",              // 放行 swagger 资源
                "/captcha",      // 验证码接口
                "/ws/**"
        );
    }

    @Override
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> {
            Admin admin = adminService.getAdminByUserName(username);
            if (null != admin) {
                admin.setRoles(adminService.getRoles(admin.getId()));
                return admin;
            }
            throw new UsernameNotFoundException("用户名或密码不正确");
        };
    }
    @Bean
    public JwtAuthencationTokenFilter jwtAuthencationTokenFilter(){
        return new  JwtAuthencationTokenFilter();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
