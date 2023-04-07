package com.example.sns.controller;

import com.example.sns.controller.response.Response;
import com.example.sns.model.User;
import com.example.sns.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserResponse> join(@RequestBody UserJoinRequest request) {
        return Response.success(UserResponse.fromUser(userService.join(request.getUserName(), request.getPassword())));
    }










    @Getter
    @AllArgsConstructor
    public static
    class UserResponse {
        private Integer id;
        private String userName;

        public static UserResponse fromUser(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getUsername());
        }
    }

    @Getter
    @AllArgsConstructor
    public static
    class UserJoinResponse {
        private Integer id;
        private String name;

        public static UserJoinResponse fromUser(User user) {
            return new UserJoinResponse(
                    user.getId(),
                    user.getUsername());
        }
    }

    @Getter
    @AllArgsConstructor
    public class UserLoginResponse {
        private String token;
    }




    @Getter
    @AllArgsConstructor
    public class UserJoinRequest {
        private String userName;
        private String password;
    }


    @Getter
    @AllArgsConstructor
    public class UserLoginRequest {
        private String name;
        private String password;
    }



}
