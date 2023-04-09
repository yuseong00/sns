package com.example.sns.service;

import com.example.sns.model.Alarm;
import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SimpleSnsApplicationException;
import com.example.sns.repository.AlarmEntityRepository;
import com.example.sns.repository.UserCacheRepository;
import com.example.sns.repository.UserEntityRepository;

import com.example.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class UserService {


    private final UserEntityRepository userRepository;
    private final AlarmEntityRepository alarmEntityRepository;

    private final UserCacheRepository userCacheRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;


    @Transactional
    public User join(String userName, String password) {
        // check the userId not exist
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("userName is %s", userName));
        });

        UserEntity savedUser = userRepository.save(UserEntity.of(userName, encoder.encode(password)));
        return User.fromEntity(savedUser);
    }


    public String login(String userName, String password) {
        long startTime = System.currentTimeMillis();

        User savedUser = loadUserByUsername(userName);
        userCacheRepository.setUser(savedUser);

        if (!encoder.matches(password, savedUser.getPassword())) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }
        long totalTime = System.currentTimeMillis() - startTime;
        System.out.println("login method execution time: " + totalTime + "ms");
        return JwtTokenUtils.generateAccessToken(userName, secretKey, expiredTimeMs);

    }


    public User loadUserByUsername(String userName) throws UsernameNotFoundException {
       return userCacheRepository.getUser(userName).orElseGet(()->
           userRepository.findByUserName(userName).map(User::fromEntity).orElseThrow(
                        () -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName))
                ));

    }


    @Transactional
    public Page<Alarm> alarmList(Integer userId, Pageable pageable) {
        return alarmEntityRepository.findAllByUserId(userId, pageable).map(Alarm::fromEntity);
    }
}
