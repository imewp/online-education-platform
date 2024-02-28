package com.mewp.edu.auth.ucenter.service;

import com.mewp.edu.auth.ucenter.entity.dto.AuthParamsDTO;
import com.mewp.edu.auth.ucenter.entity.dto.UserExtDTO;

/**
 * 认证服务
 *
 * @author mewp
 * @version 1.0
 * @date 2024/5/27 21:32
 */
public interface AuthService {
    /**
     * 认证方法
     *
     * @param authParams 认证参数
     * @return 用户信息
     */
    UserExtDTO execute(AuthParamsDTO authParams);
}
