package com.example.sns.controller;

import com.example.sns.controller.response.Response;
import com.example.sns.model.Alarm;
import com.example.sns.model.User;
import com.example.sns.model.UserRole;
import com.example.sns.service.AlarmService;
import com.example.sns.service.UserService;
import com.example.sns.util.ClassUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.sql.Timestamp;

@Slf4j
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    private final AlarmService alarmService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        return Response.success(UserJoinResponse.fromUser(userService.join(request.getName(), request.getPassword())));
    }



    @PostMapping("/login")
    public Response<UserLoginResponse> login (@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getName(), request.getPassword());
        return Response.success(new UserLoginResponse(token));

    }


    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        return Response.success(userService.alarmList(user.getId(), pageable).map(AlarmResponse::fromAlarm));
    }

    @GetMapping(value = "/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        log.info("subscribe");
        User user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), User.class);
        return alarmService.connectNotification(user.getId());
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
    public static class AlarmResponse {
        private Integer id;
        private String text;
        private Timestamp registeredAt;
        private Timestamp updatedAt;
        private Timestamp removedAt;

        public static AlarmResponse fromAlarm(Alarm alarm) {
            return new AlarmResponse(
                    alarm.getId(),
                    alarm.getAlarmText(),
                    alarm.getRegisteredAt(),
                    alarm.getUpdatedAt(),
                    alarm.getRemovedAt()
            );
        }
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
