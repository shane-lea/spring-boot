package com.shanelee.springboot.domain.converter;

import com.shanelee.springboot.domain.entity.User;
import com.shanelee.springboot.domain.vo.UserVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

/**
 * @author: manji
 * @data: 2021/4/20
 */
@Mapper
public interface UserConverter {

    UserConverter userCV = Mappers.getMapper(UserConverter.class);

    @Mappings({
            @Mapping(source = "roles" , target = "roleVOS"),
            @Mapping(source = "userExtend" , target = "userExtendVO")
    })
    UserVO do2VO(User DO);

    List<UserVO> dos2VOS(List<User> dos);

}
