package com.learnweb.learnweb.mapper;

import com.learnweb.learnweb.dto.request.UserCreationRequest;
import com.learnweb.learnweb.dto.request.UserUpdateRequest;
import com.learnweb.learnweb.dto.response.UserRespone;
import com.learnweb.learnweb.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreationRequest request);

    @Mapping(source = "firstName", target = "lastName")
    UserRespone toUserResponse(User user);
    void toUpdateUSer(@MappingTarget User user, UserUpdateRequest request);
}
