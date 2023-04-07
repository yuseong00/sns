package com.example.sns.service;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SimpleSnsApplicationException;
import com.example.sns.fixture.TestInfoFixture;
import com.example.sns.model.entity.PostEntity;
import com.example.sns.model.entity.UserEntity;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


@SpringBootTest
public class PostServiceTest {
    @Autowired PostService postService;
    @MockBean PostRepository postRepository;
    @MockBean UserRepository  userRepository;

    @Test
    public void  포스트작성이_성공한경우   ()throws Exception{
    //given
    String title = "title";
    String body = "body";
    String username = "username";

    //when
        when(userRepository.findByUserName(username)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postRepository.save(any())).thenReturn(mock(PostEntity.class));

    //then
        Assertions.assertDoesNotThrow(() -> postService.create(title, body, username));

    }

    @Test
    public void  포스트작성이_요청한유저가_존재하지_않은경우   ()throws Exception{
        String title = "title";
        String body = "body";
        String username = "username";

        //when
        when(userRepository.findByUserName(username)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(PostEntity.class));

        //then
        Assertions.assertThrows(SimpleSnsApplicationException.class ,() -> postService.create(title, body, username));

    }

    @Test
    void 포스트_수정시_포스트가_존재하지_않으면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () ->
                postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }

    @Test
    void 포스트_수정시_유저가_존재하지_않으면_에러를_내뱉는다() {

        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(PostEntity.class)));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.empty());
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void 포스트_수정시_포스트_작성자와_유저가_일치하지_않으면_에러를_내뱉는다() {
        PostEntity mockPostEntity = mock(PostEntity.class);
        UserEntity mockUserEntity = mock(UserEntity.class);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPostEntity));
        when(userRepository.findByUserName(fixture.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getUser()).thenReturn(mock(UserEntity.class));
        SimpleSnsApplicationException exception = Assertions.assertThrows(SimpleSnsApplicationException.class, () -> postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getBody()));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }
}