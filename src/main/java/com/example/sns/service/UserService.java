package com.example.sns.service;

import com.example.sns.model.User;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SimpleSnsApplicationException;
import com.example.sns.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class UserService {


    private final UserRepository userRepository;



    @Transactional
    public User join(String userName, String password) {
        userRepository.findByUserName(userName).ifPresent(it -> {
            throw new SimpleSnsApplicationException(ErrorCode.DUPLICATED_USER_NAME, String.format("userName is %s", userName));
        });//
        UserEntity userEntity = userRepository.save(UserEntity.of(userName, password));
        return User.fromEntity(userEntity);
    }



    public String login(String userName, String password) {
        UserEntity userEntity=userRepository.findByUserName(userName).orElseThrow(()->
         new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND,String.format("userName is %s",userName))
        );
        if(!userEntity.getPassword().equals(password))
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PASSWORD,String.format("password is %s",password));


        return "";
    }




}
