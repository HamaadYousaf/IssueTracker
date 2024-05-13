package com.IssueTracker.IssueTracker.Comment;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
    public CommentControllerTest(
            IssueService issueService,
            UserService userService,
            MockMvc mockMvc,
            CommentService commentService) {
        this.issueService = issueService;
        this.userService = userService;
        this.mockMvc = mockMvc;
        this.commentService = commentService;
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
    }

    @BeforeEach
    void setUp() {
        savedCreatedUser = userService.createUser(TestUserUtil.createTestUserA());
        savedAssignedUser = userService.createUser(TestUserUtil.createTestUserB());

        savedIssue1 =
                issueService.createIssue(
                        TestIssueUtil.createIssue1(savedCreatedUser, savedAssignedUser));
        savedIssue2 =
                issueService.createIssue(
                        TestIssueUtil.createIssue2(savedCreatedUser, savedAssignedUser));

        savedComment1 =
                commentService.createComment(
                        TestCommentUtil.createTestComment1(savedIssue1.getId(), savedCreatedUser));
        savedComment2 =
                commentService.createComment(
                        TestCommentUtil.createTestComment2(savedIssue1.getId(), savedCreatedUser));
    }

    @Test
    public void testCreateCommentSuccess() throws Exception {
        CommentEntity testCommentNew =
                TestCommentUtil.createTestCommentNew(savedIssue1.getId(), savedCreatedUser);
        String testCommentNewJson = objectMapper.writeValueAsString(testCommentNew);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/comments")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testCommentNewJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").isNumber())
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("test desc new"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2022-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").value(savedCreatedUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$.issueId").value(savedIssue1.getId()));
    }

    @Test
    public void testCreateCommentNoDesc() throws Exception {
        CommentEntity testCommentNew =
                TestCommentUtil.createTestCommentNew(savedIssue1.getId(), savedCreatedUser);
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
        CommentEntity testCommentNew =
                TestCommentUtil.createTestCommentNew(99999, savedCreatedUser);
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

    @Test
    public void testUpdateCommentDescById() throws Exception {
        savedComment1.setDescription("UPDATED");
        String testCommentUpdatedJson = objectMapper.writeValueAsString(savedComment1);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/comments/" + savedComment1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testCommentUpdatedJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedComment1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("UPDATED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2022-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").value(savedCreatedUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$.issueId").value(savedIssue1.getId()));

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/" + savedIssue1.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    public void testUpdateCommentByIdWhenCommentDoesNotExist() throws Exception {
        savedComment1.setDescription("UPDATED");
        String testCommentUpdatedJson = objectMapper.writeValueAsString(savedComment1);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/comments/999999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testCommentUpdatedJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteCommentById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/comments/" + savedComment1.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/comments/" + savedIssue1.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void testDeleteCommentByIdWhenCommentDoesNotExist() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/comments/999999")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
