package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SimpleSnsApplicationException;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    @Transactional
    public void create(String title, String body, String username) {
        UserEntity userEntity = getUserEntityOrException(username);


        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postRepository.save(postEntity);


    }

    //유저확인 후 검증로직
    private UserEntity getUserEntityOrException(String username) {
        return userRepository.findByUserName(username)
                .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", username)));
    }


}
