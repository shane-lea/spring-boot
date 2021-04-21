package com.shanelee.springboot.domain.entity;

import com.shanelee.springboot.domain.Base;
import lombok.Data;

import java.util.List;

/**
 * @author: manji
 * @data: 2021/4/20
 */
@Data
public class User extends Base {

    private Long userId;
    private String loginName;
    private String password;

    private UserExtend userExtend;
    private List<Role> roles;
}
