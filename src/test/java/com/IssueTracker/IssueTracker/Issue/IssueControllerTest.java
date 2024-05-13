package com.IssueTracker.IssueTracker.Issue;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import com.IssueTracker.IssueTracker.Issue.Enums.Category;
import com.IssueTracker.IssueTracker.Issue.Enums.Priority;
import com.IssueTracker.IssueTracker.Issue.Enums.Status;
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
            IssueService issueService, MockMvc mockMvc, UserService userService) {
        this.issueService = issueService;
        this.userService = userService;
        this.mockMvc = mockMvc;
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
    }

    @Test
    public void testCreateIssueSuccess() throws Exception {
        IssueEntity testIssueNew =
                TestIssueUtil.createIssueNew(savedCreatedUser, savedAssignedUser);
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
        IssueEntity testIssueNew =
                TestIssueUtil.createIssueNew(savedCreatedUser, savedAssignedUser);
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

    @Test
    public void testGetIssueById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/issues/" + savedIssue1.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedIssue1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test title 1"))
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.description").value("test description 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BUG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ASSIGNED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2020-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(savedCreatedUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value(savedAssignedUser));
    }

    @Test
    public void getIssuesWithFilters() throws Exception {
        issueService.createIssue(TestIssueUtil.createIssue3(savedAssignedUser, savedCreatedUser));
        issueService.createIssue(TestIssueUtil.createIssue4(savedAssignedUser, savedCreatedUser));
        issueService.createIssue(TestIssueUtil.createIssue5(savedAssignedUser, savedCreatedUser));

        mockMvc.perform(
                        MockMvcRequestBuilders.get(
                                "/issues?priority=NORMAL&category=FEATURE&status=NEW&createdBy=test name B&assignedTo=test name A"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)));
    }

    @Test
    public void getIssuesWithFiltersDateDesc() throws Exception {
        issueService.createIssue(TestIssueUtil.createIssue3(savedAssignedUser, savedCreatedUser));
        issueService.createIssue(TestIssueUtil.createIssue4(savedAssignedUser, savedCreatedUser));
        issueService.createIssue(TestIssueUtil.createIssue5(savedAssignedUser, savedCreatedUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/issues?priority=NORMAL&sortDate=desc"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].creationDate").value("2022-01-01"))
                .andExpect(jsonPath("$[1].creationDate").value("2021-01-01"))
                .andExpect(jsonPath("$[2].creationDate").value("2020-01-01"));
    }

    @Test
    public void getIssuesWithFiltersDateAsc() throws Exception {
        issueService.createIssue(TestIssueUtil.createIssue3(savedAssignedUser, savedCreatedUser));
        issueService.createIssue(TestIssueUtil.createIssue4(savedAssignedUser, savedCreatedUser));
        issueService.createIssue(TestIssueUtil.createIssue5(savedAssignedUser, savedCreatedUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/issues?priority=NORMAL&sortDate=asc"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].creationDate").value("2020-01-01"))
                .andExpect(jsonPath("$[1].creationDate").value("2021-01-01"))
                .andExpect(jsonPath("$[2].creationDate").value("2022-01-01"));
    }

    @Test
    public void testGetIssueByIdWhenIssueDoesNotExist() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/issues/999999")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testUpdateIssueDescById() throws Exception {
        savedIssue1.setDescription("UPDATED");
        String testIssueUpdatedJson = objectMapper.writeValueAsString(savedIssue1);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/issues/" + savedIssue1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testIssueUpdatedJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedIssue1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("test title 1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("UPDATED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("HIGH"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("BUG"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("ASSIGNED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2020-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(savedCreatedUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value(savedAssignedUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/issues"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    public void testFullUpdateIssueById() throws Exception {
        savedIssue1.setTitle("UPDATED");
        savedIssue1.setDescription("UPDATED");
        savedIssue1.setPriority(Priority.LOW);
        savedIssue1.setStatus(Status.RESOLVED);
        savedIssue1.setCategory(Category.OTHER);
        savedIssue1.setAssignedTo(savedCreatedUser);
        String testIssueUpdatedJson = objectMapper.writeValueAsString(savedIssue1);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/issues/" + savedIssue1.getId())
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testIssueUpdatedJson))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(savedIssue1.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("UPDATED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.description").value("UPDATED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priority").value("LOW"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.category").value("OTHER"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value("RESOLVED"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2020-01-01"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.createdBy").value(savedCreatedUser))
                .andExpect(MockMvcResultMatchers.jsonPath("$.assignedTo").value(savedCreatedUser));

        mockMvc.perform(MockMvcRequestBuilders.get("/issues"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(2)));
    }

    @Test
    public void testUpdateIssueByIdWhenIssueDoesNotExist() throws Exception {
        savedIssue1.setDescription("UPDATED");
        String testIssueUpdatedJson = objectMapper.writeValueAsString(savedIssue1);

        mockMvc.perform(
                        MockMvcRequestBuilders.put("/issues/999999")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(testIssueUpdatedJson))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    public void testDeleteIssueById() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/issues/" + savedIssue1.getId())
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/issues"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.size()", is(1)));
    }

    @Test
    public void testDeleteIssueByIdWhenIssueDoesNotExist() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.delete("/issues/999999")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
