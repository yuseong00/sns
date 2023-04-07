package com.example.sns.controller;

import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.SimpleSnsApplicationException;
import com.example.sns.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    void 포스트작성() throws Exception {
        String title = "name";
        String body = "body";


        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostController.PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    void 포스트작성시_로그인하지않은경우() throws Exception {
        String title = "name";
        String body = "body";


        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostController.PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }
    @Test
    @WithMockUser
    void 포스트수정() throws Exception {
        String title = "name";
        String body = "body";


        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostController.PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }


    @Test
    @WithAnonymousUser
    void 포스트수정시_로그인하지_않은경우() throws Exception {
        String title = "name";
        String body = "body";


        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostController.PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 포스트수정시_본인이작성한글이아닌경우() throws Exception {
        String title = "name";
        String body = "body";

        doThrow(new SimpleSnsApplicationException(ErrorCode.INVALID_PERMISSION, "본인이 작성한 글이 아닙니다."))
                .when(postService).modify(eq(title),eq(body), any(), 1);

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostController.PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @WithMockUser
    void 포스트수정시_수정하려는글이_없는경우() throws Exception {
        String title = "name";
        String body = "body";

        doThrow(new SimpleSnsApplicationException(ErrorCode.POST_NOT_FOUND, "본인이 작성한 글이 없습니다."))
                .when(postService).modify(eq(title),eq(body), any(), 1);

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostController.PostWriteRequest(title, body))))
                .andDo(print())
                .andExpect(status().isNotFound());
    }



}
