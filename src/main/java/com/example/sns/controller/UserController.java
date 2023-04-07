package com.example.sns.controller;

import com.example.sns.controller.response.Response;
import com.example.sns.model.User;
import com.example.sns.model.UserRole;
import com.example.sns.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        return Response.success(UserJoinResponse.fromUser(userService.join(request.getName(), request.getPassword())));
    }



    @PostMapping("/login")
    public Response<UserLoginResponse> login (@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));

    }















    @Getter
    @AllArgsConstructor
    public static class UserResponse {
        private Integer id;
        private String userName;
        private UserRole role;

        public static UserResponse fromUser(User user) {
            return new UserResponse(
                    user.getId(),
                    user.getUsername(),
                    user.getRole()
        );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class UserJoinResponse {
        private Integer id;
        private String userName;

        public static UserJoinResponse fromUser(User user) {
            return new UserJoinResponse(
                    user.getId(),
                    user.getUsername());
        }
    }

    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserLoginResponse {
        private String token;
    }




    @Getter
    @AllArgsConstructor
    public static class UserJoinRequest {
        private String name;
        private String password;
    }



    @Getter
    @AllArgsConstructor
    public static class UserLoginRequest {
        private String name;
        private String password;
    }



}
