package com.mewp.edu.content.utils;

import com.mewp.edu.common.utils.JsonUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 获取当前用户身份工具类
 *
 * @author mewp
 * @version 1.0
 * @date 2024/4/26 17:57
 */
@Slf4j
public class SecurityUtil {

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    public static XcUser getUser() {
        try {
            Object principalObj = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if (principalObj instanceof String) {
                String principal = principalObj.toString();
                return JsonUtil.jsonToObject(principal, XcUser.class);
            }
        } catch (Exception e) {
            log.error("获取当前登录用户身份出错：{}", e.getMessage(), e);
        }
        return null;
    }


    @Data
    public static class XcUser implements Serializable {

        private static final long serialVersionUID = 1L;

        private String id;

        private String username;

        private String password;

        private String salt;

        /**
         * 微信unionid
         */
        private String wxUnionid;

        /**
         * 昵称
         */
        private String nickname;

        private String name;

        /**
         * 头像
         */
        private String userpic;

        private String companyId;

        private String utype;

        private LocalDateTime birthday;

        private String sex;

        private String email;

        private String cellphone;

        private String qq;

        /**
         * 用户状态
         */
        private String status;

        private LocalDateTime createTime;

        private LocalDateTime updateTime;
    }

}
