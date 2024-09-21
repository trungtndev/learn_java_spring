package com.learnweb.learnweb.controller;

import com.learnweb.learnweb.dto.request.ApiResponse;
import com.learnweb.learnweb.dto.request.UserCreationRequest;
import com.learnweb.learnweb.dto.request.UserUpdateRequest;
import com.learnweb.learnweb.dto.response.UserRespone;
import com.learnweb.learnweb.entity.User;
import com.learnweb.learnweb.repository.UserRepository;
import com.learnweb.learnweb.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/users")
@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserController {
    UserService userService;

    @PostMapping()
    ApiResponse<UserRespone> createUser(@RequestBody @Valid UserCreationRequest request){
        ApiResponse<UserRespone> apiResponse = new ApiResponse<UserRespone>();

        apiResponse.setCode(1000);
        apiResponse.setResult(userService.createUser(request));

        return apiResponse;
    }

    @GetMapping
    List<User> getUsers(){
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    UserRespone getUser(@PathVariable("userId") String userId){
        return userService.getUser(userId);
    }

    @PutMapping("/{userId}")
    UserRespone updateUser(@PathVariable String userId,@RequestBody UserUpdateRequest request){
        return  userService.updateUser(userId, request);
    }
//
    @DeleteMapping("/{userId}")
    String delateUser(@PathVariable String userId){
        userService.delateUserId(userId);
        return "UserDeleted";
    }




}
