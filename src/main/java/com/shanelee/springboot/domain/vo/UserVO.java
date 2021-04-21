package com.shanelee.springboot.domain.vo;

import lombok.Data;

import java.util.List;

/**
 * @author: manji
 * @data: 2021/4/20
 */
@Data
public class UserVO {
    private Long userId;
    private String loginName;
    private String password;

    private UserExtendVO userExtendVO;
    private List<RoleVO> roleVOS;
}
