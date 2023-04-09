package com.example.sns.fixture;

import lombok.Data;

public class TestInfoFixture {

    public static TestInfo get() {
        TestInfo info = new TestInfo();
        info.setPostId(1);
        info.setUserId(1);
        info.setUserName("name");
        info.setPassword("password");
        info.setTitle("title");
        info.setBody("body");
        return info;
    }
    @Data
    public static class TestInfo {
        private Integer postId;
        private Integer userId;
        private String userName;
        private String password;
        private String title;
        private String body;

        public void setPostId(Integer postId) {
            this.postId = postId;
        }

        public void setUserId(Integer userId) {
            this.userId = userId;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public Integer getPostId() {
            return postId;
        }

        public Integer getUserId() {
            return userId;
        }

        public String getUserName() {
            return userName;
        }

        public String getPassword() {
            return password;
        }

        public String getTitle() {
            return title;
        }

        public String getBody() {
            return body;
        }
    }
}

