package com.IssueTracker.IssueTracker.User;

public class TestUserUtil {

    public TestUserUtil() {}

    public static UserEntity createTestUserA() {
        return UserEntity.builder()
                .username("test name A")
                .email("testA@email.com")
                .password("123123")
                .build();
    }

    public static UserEntity createTestUserB() {
        return UserEntity.builder()
                .username("test name B")
                .email("testB@email.com")
                .password("123123")
                .build();
    }

    public static UserEntity createTestUserC() {
        return UserEntity.builder()
                .username("test name C")
                .email("testC@email.com")
                .password("123123")
                .build();
    }

    public static UserEntity createTestUserNew() {
        return UserEntity.builder()
                .username("test name New")
                .email("testNew@email.com")
                .password("123123")
                .build();
    }
}
