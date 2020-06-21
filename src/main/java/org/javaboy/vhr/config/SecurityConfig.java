package org.javaboy.vhr.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.javaboy.vhr.bean.Hr;
import org.javaboy.vhr.service.HrService;
import org.javaboy.vhr.utils.ServerResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    HrService hrService;

    // 提供密码加密方法
    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    // 用户校验方式 使用hrService查询数据库校验
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(hrService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                // 所有请求都需要验证
                .anyRequest()
                .authenticated()
                .and()
                //  登录
                .formLogin()
                .usernameParameter("username")
                .passwordParameter("password")
                .loginProcessingUrl("/doLogin")
                // 需要自己写login接口   返回json数据 todo 记得加斜杠 '/'
                .loginPage("/login")
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        Hr hr = (Hr) authentication.getPrincipal();
                        hr.setPassword(null);
                        ServerResponse serverResponse = ServerResponse.success("登录成功", hr);
                        out.write(new ObjectMapper().writeValueAsString(serverResponse));
                        out.flush();
                        out.flush();
                    }
                })
                .failureHandler(new AuthenticationFailureHandler() {
                    @Override
                    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        ServerResponse serverResponse = ServerResponse.error(500,"登录失败");

                        if(e instanceof LockedException){
                            serverResponse.setCode(1000);
                            serverResponse.setMessage("用户被锁定,请联系管理员");
                        }else if(e instanceof CredentialsExpiredException){
                            serverResponse.setCode(1001);
                            serverResponse.setMessage("密码过期,请联系管理员");
                        }
                        else if(e instanceof AccountExpiredException){
                            serverResponse.setCode(1002);
                            serverResponse.setMessage("账号过期,请联系管理员");
                        }else if(e instanceof DisabledException){
                            serverResponse.setCode(1003);
                            serverResponse.setMessage("账号被禁用,请联系管理员");
                        }else if(e instanceof BadCredentialsException){
                            serverResponse.setCode(1004);
                            serverResponse.setMessage("用户或密码错误");
                        }else{
                            serverResponse.setCode(1005);
                            serverResponse.setMessage("未知错误,请联系管理员");
                        }
                        out.write(new ObjectMapper().writeValueAsString(serverResponse));
                        out.flush();
                        out.flush();
                    }
                })
                .permitAll()

                // 注销
                .and()
                .logout()
                .logoutSuccessHandler(new LogoutSuccessHandler() {
                    @Override
                    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                        response.setContentType("application/json;charset=utf-8");
                        PrintWriter out = response.getWriter();
                        out.write(new ObjectMapper().writeValueAsString(ServerResponse.success("用户注销成功")));
                        out.flush();
                        out.close();
                    }
                })
                .permitAll()

                // 关闭csrf
                .and()
                .csrf().disable();
    }
}
