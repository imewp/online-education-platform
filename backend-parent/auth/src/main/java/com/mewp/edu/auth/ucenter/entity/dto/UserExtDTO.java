package com.mewp.edu.auth.ucenter.entity.dto;

import com.mewp.edu.auth.ucenter.entity.po.XcUser;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户扩展信息
 *
 * @author mewp
 * @version 1.0
 * @date 2024/2/28 17:25
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class UserExtDTO extends XcUser {
    /**
     * 用户权限
     */
    List<String> permissions = new ArrayList<>();
}
