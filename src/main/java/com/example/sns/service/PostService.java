package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SimpleSnsApplicationException;
import com.example.sns.model.Post;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostEntityRepository;
import com.example.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String userName, String title, String body) {
        UserEntity userEntity = userEntityRepository.findByUserName(userName)
                .orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", userName)));
        PostEntity postEntity = PostEntity.of(title, body, userEntity);
        postEntityRepository.save(postEntity);
    }

    @Transactional
    public Post modify(Integer userId, Integer postId, String title, String body) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() -> new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        if (!Objects.equals(postEntity.getUser().getId(), userId)) {
            throw new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
        }

        postEntity.setTitle(title);
        postEntity.setBody(body);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }









}
