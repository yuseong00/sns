package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SimpleSnsApplicationException;
import com.example.sns.fixture.UserEntityFixture;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserService userService;

    @MockBean
    UserRepository userEntityRepository;

    @MockBean
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Disabled("구현중")
    @Test
    void 회원가입_정상적으로_동작하는경우(){
        String userName="test";
        String password="test";
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        when(bCryptPasswordEncoder.encode(password)).thenReturn("password_encrypt");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(UserEntityFixture.get(userName,password)));
        Assertions.assertDoesNotThrow(()->userService.join(userName,password));

    }

    @Test
    void 회원가입시_이미유저가_있는경우(){
        String userName="test";
        String password="test";
        UserEntity userEntity=UserEntityFixture.get(userName,password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));
        when(bCryptPasswordEncoder.encode(password)).thenReturn("password_encrypt");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(userEntity));
        Assertions.assertThrows(SimpleSnsApplicationException.class,()->userService.join(userName,password));

    }


    @Disabled("구현중")
    @Test
    void 로그인이_정상동작한다(){
        String userName="test";
        String password="test";
        UserEntity userEntity=UserEntityFixture.get(userName,password);


        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        Assertions.assertDoesNotThrow(()->userService.login(userName,password));

    }
    @Disabled("구현중")
    @Test
    void 로그인시_유저가_존재하지_않으면_에러를_내뱉는다(){
        String userName="test";
        String password="test";
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.empty());
        Assertions.assertThrows(SimpleSnsApplicationException.class,()->userService.login(userName,password));

    }
    @Disabled("구현중")
    @Test
    void 로그인시_패스워드가_틀린경우() {

        String userName="test";
        String password="test";
        String wrongPassword="wrongPassword";

        UserEntity userEntity=UserEntityFixture.get(userName,password);
        when(userEntityRepository.findByUserName(userName)).thenReturn(Optional.of(userEntity));


        Assertions.assertThrows(SimpleSnsApplicationException.class,()->userService.login(userName,wrongPassword));


    }





}
