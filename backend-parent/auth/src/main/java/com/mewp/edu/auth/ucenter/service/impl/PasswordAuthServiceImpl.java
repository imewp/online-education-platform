package com.mewp.edu.auth.ucenter.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mewp.edu.auth.ucenter.entity.dto.AuthParamsDTO;
import com.mewp.edu.auth.ucenter.entity.dto.UserExtDTO;
import com.mewp.edu.auth.ucenter.entity.po.XcUser;
import com.mewp.edu.auth.ucenter.feignclient.CheckCodeClient;
import com.mewp.edu.auth.ucenter.mapper.XcUserMapper;
import com.mewp.edu.auth.ucenter.service.AuthService;
import com.mewp.edu.common.exception.CustomException;
import com.mewp.edu.common.utils.StringUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * 账号密码认证
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/27 21:37
 */
@Slf4j
@AllArgsConstructor
@Service("password_authService")
public class PasswordAuthServiceImpl implements AuthService {
    private XcUserMapper userMapper;
    private PasswordEncoder passwordEncoder;
    private CheckCodeClient checkCodeClient;

    @Override
    public UserExtDTO execute(AuthParamsDTO authParams) {
        // 校验验证码
        String checkCode = authParams.getCheckcode();
        String checkCodeKey = authParams.getCheckcodeKey();
        if (StringUtil.isBlank(checkCode) || StringUtil.isBlank(checkCodeKey)) {
            throw new CustomException("验证码为空");
        }
        Boolean verify = checkCodeClient.verify(checkCodeKey, checkCode);
        if (!verify) {
            throw new CustomException("验证码输入错误");
        }

        String username = authParams.getUsername();
        XcUser user = userMapper.selectOne(
                new LambdaQueryWrapper<XcUser>().eq(XcUser::getUsername, username));
        if (user == null) {
            // 返回空 表示用户不存在
            return null;
        }
        // 取出数据库中存储的密码
        String password = user.getPassword();
        String passwordParam = authParams.getPassword();
        // 校验密码
        boolean matches = passwordEncoder.matches(passwordParam, password);
        if (!matches) {
            throw new CustomException("账号或密码错误");
        }
        UserExtDTO userExtDTO = new UserExtDTO();
        BeanUtils.copyProperties(user, userExtDTO);
        return userExtDTO;
    }
}
