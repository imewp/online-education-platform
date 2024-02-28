package com.mewp.edu.auth.ucenter.service.impl;

import com.mewp.edu.auth.ucenter.entity.dto.AuthParamsDTO;
import com.mewp.edu.auth.ucenter.entity.dto.UserExtDTO;
import com.mewp.edu.auth.ucenter.mapper.XcUserMapper;
import com.mewp.edu.auth.ucenter.service.AuthService;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.common.utils.JsonUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * 自定义UserDetailsService用来对接Spring Security
 *
 * @author mewp
 * @version 1.0
 * @date 2024/4/26 17:19
 */
@Slf4j
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserDetailsService {
    private XcUserMapper userMapper;
    private ApplicationContext applicationContext;

    /**
     * 查询用户信息组成用户身份信息
     *
     * @param s AuthParamsDTO类型的json数据
     * @return 用户信息
     */
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        AuthParamsDTO authParamsDto;
        try {
            // 将认证参数转换为AuthParamsDTO类型
            authParamsDto = JsonUtil.jsonToObject(s, AuthParamsDTO.class);
            if (authParamsDto == null) {
                throw new CustomException("认证请求参数为空");
            }
        } catch (Exception e) {
            log.error("认证请求参数不符合项目要求：{}", e.getMessage(), e);
            throw new RuntimeException("认证请求数据格式不正确");
        }
        // 开始认证
        // 认证方式
        String authType = authParamsDto.getAuthType();
        AuthService authService = applicationContext.getBean(authType + "_authService", AuthService.class);
        UserExtDTO userExt = authService.execute(authParamsDto);
        return getUserPrincipal(userExt);
    }

    /**
     * 转换成UserDetails对象
     *
     * @param userExt 用户信息
     * @return UserDetails对象
     */
    public UserDetails getUserPrincipal(UserExtDTO userExt) {
        // todo：构建用户权限
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("test");
        String password = userExt.getPassword();
        // 为了安全在令牌中不存放密码
        userExt.setPassword(null);
        // 将 user对象转换成json字符串
        String userJsonString = JsonUtil.objectTojson(userExt);
        // 创建UserDetails对象
        return User.withUsername(userJsonString).password(password)
                .authorities(grantedAuthority).build();
    }



    /* @Override
    public UserDetails loadUserByUsername(String s) {
        AuthParamsDTO authParamsDto;
        try {
            // 将认证参数转换为AuthParamsDTO类型
            authParamsDto = JsonUtil.jsonToObject(s, AuthParamsDTO.class);
            if (authParamsDto == null) {
                throw new CustomException("认证请求参数为空");
            }
        } catch (Exception e) {
            log.error("认证请求参数不符合项目要求：{}", e.getMessage(), e);
            throw new RuntimeException("认证请求数据格式不正确");
        }
        String username = authParamsDto.getUsername();

        XcUser user = userMapper.selectOne(
                new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        if (user == null) {
            // 返回空 表示用户不存在
            return null;
        }
        // 取出数据库中存储的密码
        String password = user.getPassword();
        // 为了安全在令牌中不存放密码
        user.setPassword(null);
        // 将 user对象转换成json字符串
        String userJsonString = JsonUtil.objectTojson(user);

        // todo：构建用户权限
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("test");
        // 创建UserDetails对象
        return User.withUsername(userJsonString).password(password)
                .authorities(grantedAuthority).build();
    }*/


    /**
     * 根据用户名查询用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
/*    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        XcUser user = userMapper.selectOne(
                new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        if (user == null) {
            // 返回空 表示用户不存在
            return null;
        }
        // 取出数据库中存储的密码
        String password = user.getPassword();
        // 为了安全在令牌中不存放密码
        user.setPassword(null);
        // 将 user对象转换成json字符串
        String userJsonString = JsonUtil.objectTojson(user);

        // todo：构建用户权限
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("test");
        // 创建UserDetails对象
        return User.withUsername(userJsonString).password(password)
                .authorities(grantedAuthority).build();
    }*/
}
