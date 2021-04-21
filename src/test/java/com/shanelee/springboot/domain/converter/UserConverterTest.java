package com.shanelee.springboot.domain.converter;

import com.shanelee.springboot.domain.entity.Role;
import com.shanelee.springboot.domain.entity.User;
import com.shanelee.springboot.domain.entity.UserExtend;
import com.shanelee.springboot.domain.vo.UserVO;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

class UserConverterTest {

    @Test
    public void  testDO2VO(){

        UserVO userVO = UserConverter.userCV.do2VO(createUser());
        System.out.println(1);
    }

    @Test
    public void  testDOS2VOS(){

        List<User> users = new ArrayList<>();
        users.add(createUser());
        users.add(createUser());
        List<UserVO> userVOS = UserConverter.userCV.dos2VOS(users);
        System.out.println(1);
    }

    private User createUser(){

        UserExtend userExtend = new UserExtend();
        userExtend.setTelephone("13911119999");
        List<Role> roles = new ArrayList<>();
        Role r1 = new Role();
        r1.setRoleName("role-name-01");
        roles.add(r1);
        Role r2 = new Role();
        r2.setRoleName("role-name-02");
        roles.add(r2);

        User user = new User();
        user.setLoginName("login-name");
        user.setUserExtend(userExtend);
        user.setRoles(roles);
        return user;
    }
}