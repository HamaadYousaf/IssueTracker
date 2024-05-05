package com.IssueTracker.IssueTracker.User;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class UserControllerTest {

    private final UserService userService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    UserEntity testUserASaved;
    UserEntity testUserBSaved;
    UserEntity testUserCSaved;

    @Autowired
    public UserControllerTest(UserService userService, MockMvc mockMvc) {
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
    }

    @BeforeEach
    void setUp() {
        UserEntity testUserA = TestUserUtil.createTestUserA();
        testUserASaved = userService.saveUser(testUserA);
        UserEntity testUserB = TestUserUtil.createTestUserB();
        testUserBSaved = userService.saveUser(testUserB);
        UserEntity testUserC = TestUserUtil.createTestUserC();
        testUserCSaved = userService.saveUser(testUserC);
    }

    @Test
    void testCreateUserSuccess() throws Exception {
        UserEntity testUserNew = TestUserUtil.createTestUserNew();
        String testUserNewJson = objectMapper.writeValueAsString(testUserNew);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testUserNewJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("test name New"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("testNew@email.com"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("123123"));
    }

    @Test
    void testCreateUserWhenUserAlreadyExists() throws Exception {
        UserEntity testUserA = TestUserUtil.createTestUserA();
        String testUserAJson = objectMapper.writeValueAsString(testUserA);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/users")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testUserAJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void getUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    public void testGetUserById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/" + testUserASaved.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.username")
                                .value(testUserASaved.getUsername()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.email").value(testUserASaved.getEmail()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.password")
                                .value(testUserASaved.getPassword()));
    }

    @Test
    public void testGetUserByIdWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/999")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateUsernameById() throws Exception {
        testUserASaved.setUsername("UPDATED");
        String testUserNewJson = objectMapper.writeValueAsString(testUserASaved);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/users/" + testUserASaved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testUserNewJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUserASaved.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("UPDATED"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.email").value(testUserASaved.getEmail()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.password")
                                .value(testUserASaved.getPassword()));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    public void testUpdateEmailById() throws Exception {
        testUserASaved.setEmail("UPDATED");
        String testUserNewJson = objectMapper.writeValueAsString(testUserASaved);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/users/" + testUserASaved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testUserNewJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUserASaved.getId()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.username")
                                .value(testUserASaved.getUsername()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("UPDATED"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.password")
                                .value(testUserASaved.getPassword()));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    public void testUpdatePasswordById() throws Exception {
        testUserASaved.setPassword("UPDATED");
        String testUserNewJson = objectMapper.writeValueAsString(testUserASaved);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/users/" + testUserASaved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testUserNewJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUserASaved.getId()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.username")
                                .value(testUserASaved.getUsername()))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.email").value(testUserASaved.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("UPDATED"));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    public void testUpdateAllById() throws Exception {
        testUserASaved.setUsername("UPDATED");
        testUserASaved.setEmail("UPDATED");
        testUserASaved.setPassword("UPDATED");
        String testUserNewJson = objectMapper.writeValueAsString(testUserASaved);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/users/" + testUserASaved.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testUserNewJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(testUserASaved.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("UPDATED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value("UPDATED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.password").value("UPDATED"));

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    public void testUpdateUserByIdWhenUserDoesNotExist() throws Exception {
        UserEntity testUserNew = TestUserUtil.createTestUserNew();
        String testUserNewJson = objectMapper.writeValueAsString(testUserNew);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/users/999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testUserNewJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteUserById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/users/" + testUserASaved.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/users/" + testUserASaved.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteUserByIdWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/users/999")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
