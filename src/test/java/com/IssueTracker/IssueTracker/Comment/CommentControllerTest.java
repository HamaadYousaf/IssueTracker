package com.IssueTracker.IssueTracker.Comment;

import com.IssueTracker.IssueTracker.Issue.IssueEntity;
import com.IssueTracker.IssueTracker.Issue.IssueService;
import com.IssueTracker.IssueTracker.Issue.TestIssueUtil;
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

import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@AutoConfigureMockMvc
class CommentControllerTest {

    private final CommentService commentService;

    private final IssueService issueService;

    private final UserService userService;

    private final MockMvc mockMvc;

    private final ObjectMapper objectMapper;

    UserEntity savedCreatedUser;

    UserEntity savedAssignedUser;

    IssueEntity savedIssue1;

    IssueEntity savedIssue2;

    CommentEntity savedComment1;

    CommentEntity savedComment2;

    @Autowired
    public CommentControllerTest(IssueService issueService, UserService userService, MockMvc mockMvc, ObjectMapper objectMapper, CommentService commentService) {
        this.issueService = issueService;
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.commentService = commentService;
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

        CommentEntity comment1 = TestCommentUtil.createTestComment1();
        comment1.setIssueId(savedIssue1.getId());
        comment1.setUser(savedCreatedUser);
        savedComment1 = commentService.createComment(comment1);

        CommentEntity comment2 = TestCommentUtil.createTestComment2();
        comment2.setIssueId(savedIssue1.getId());
        comment2.setUser(savedCreatedUser);
        savedComment2 = commentService.createComment(comment2);
    }

    @Test
    public void testCreateCommentSuccess() throws Exception {
        CommentEntity testCommentNew = TestCommentUtil.createTestCommentNew();
        testCommentNew.setUser(savedCreatedUser);
        testCommentNew.setIssueId(savedIssue1.getId());
        String testCommentNewJson = objectMapper.writeValueAsString(testCommentNew);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testCommentNewJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.description")
                                .value("test desc new"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2022-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").value(savedCreatedUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$.issueId").value(savedIssue1.getId()));
    }

    @Test
    public void testCreateCommentNoDesc() throws Exception {
        CommentEntity testCommentNew = TestCommentUtil.createTestCommentNew();
        testCommentNew.setUser(savedCreatedUser);
        testCommentNew.setIssueId(savedIssue1.getId());
        testCommentNew.setDescription("");
        String testCommentNewJson = objectMapper.writeValueAsString(testCommentNew);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testCommentNewJson))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    public void testCreateCommentIssueDoesNotExist() throws Exception {
        CommentEntity testCommentNew = TestCommentUtil.createTestCommentNew();
        testCommentNew.setUser(savedCreatedUser);
        testCommentNew.setIssueId(99999);
        String testCommentNewJson = objectMapper.writeValueAsString(testCommentNew);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testCommentNewJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void getCommentsByIssue() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/comments/" + savedIssue1.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }
}
