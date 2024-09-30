package com.learnweb.learnweb.service;


import com.learnweb.learnweb.dto.request.UserCreationRequest;
import com.learnweb.learnweb.dto.request.UserUpdateRequest;
import com.learnweb.learnweb.dto.response.UserRespone;
import com.learnweb.learnweb.entity.User;
import com.learnweb.learnweb.enums.Role;
import com.learnweb.learnweb.exceptoion.AppException;
import com.learnweb.learnweb.exceptoion.ErrorCode;
import com.learnweb.learnweb.mapper.UserMapper;
import com.learnweb.learnweb.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class UserService {
    UserRepository userRepository;
    UserMapper userMapper;

    public UserRespone createUser(UserCreationRequest request){
        if (userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXITED);
        }
        User user = userMapper.toUser(request);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        HashSet<String> roles = new HashSet<>();
        roles.add(Role.USER.name());

        user.setRoles(roles);


        return userMapper.toUserResponse(userRepository.save(user));

    }

    @PreAuthorize("hasRole('ADMIN')")
    public List<UserRespone> getUsers(){

        log.info("Get all users");
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse).toList();
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserRespone getUser(String id){
        log.info("Get user by id" + id);
        return userMapper.toUserResponse(userRepository.findById(id).
                orElseThrow(() -> new RuntimeException("User not found")));
    }

    public UserRespone getMyInfo(){
        var context = SecurityContextHolder.getContext();
        String name = context.getAuthentication().getName();

        User user = userRepository.findByUsername(name).orElseThrow(()
                -> new AppException(ErrorCode.USERNAME_NOT_EXITED));
        return userMapper.toUserResponse(user);
    }

    public UserRespone updateUser(String UserId, UserUpdateRequest request){
        User user = userRepository.findById(UserId).orElseThrow(() -> new RuntimeException("not found"));
        userMapper.toUpdateUSer(user, request);
        return userMapper.toUserResponse(user);
    }

    public void  delateUserId(String userId){
        userRepository.deleteById(userId);
    }
}
