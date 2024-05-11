package com.IssueTracker.IssueTracker.Issue;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.IssueTracker.IssueTracker.User.TestUserUtil;
import com.IssueTracker.IssueTracker.User.UserEntity;
import com.IssueTracker.IssueTracker.User.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

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
class IssueControllerTest {

    private final IssueService issueService;

    private final UserService userService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    UserEntity savedCreatedUser;

    UserEntity savedAssignedUser;

    IssueEntity savedIssue1;

    IssueEntity savedIssue2;

    @Autowired
    public IssueControllerTest(
            IssueService issueService,
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            UserService userService) {
        this.issueService = issueService;
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        UserEntity createdUser = TestUserUtil.createTestUserA();
        savedCreatedUser = userService.createUser(createdUser);
        UserEntity assignedUser = TestUserUtil.createTestUserB();
        savedAssignedUser = userService.createUser(assignedUser);

        IssueEntity issue1 = TestIssueUtil.createIssue1();
        issue1.setCreatedBy(createdUser);
        issue1.setAssignedTo(assignedUser);
        savedIssue1 = issueService.createIssue(issue1);

        IssueEntity issue2 = TestIssueUtil.createIssue2();
        issue2.setCreatedBy(createdUser);
        savedIssue2 = issueService.createIssue(issue2);
    }

    @Test
    public void testCreateIssueSuccess() throws Exception {
        IssueEntity testIssueNew = TestIssueUtil.createIssueNew();
        testIssueNew.setCreatedBy(savedCreatedUser);
        testIssueNew.setAssignedTo(savedAssignedUser);
        String testIssueNewJson = objectMapper.writeValueAsString(testIssueNew);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/issues")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testIssueNewJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test title new"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.description")
                                .value("test description new"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("NORMAL"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("FEATURE"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ASSIGNED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2022-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(savedCreatedUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value(savedAssignedUser));
    }

    @Test
    public void testCreateIssueWhenTitleMissing() throws Exception {
        IssueEntity testIssueNew = TestIssueUtil.createIssueNew();
        testIssueNew.setCreatedBy(savedCreatedUser);
        testIssueNew.setTitle("");
        String testIssueNewJson = objectMapper.writeValueAsString(testIssueNew);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/issues")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testIssueNewJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void getAllIssues() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/issues"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }
}
