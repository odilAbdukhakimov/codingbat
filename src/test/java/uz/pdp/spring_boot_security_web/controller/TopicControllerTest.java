package uz.pdp.spring_boot_security_web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

class TopicControllerTest extends BaseTest {

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterAll() {
        languageRepository.deleteAll();
    }

    @Test
    void getTaskList() throws Exception {
//        callAdd();
//        ResultActions perform = mockMvc.perform(get("/{language}/{topic}", "Java", "hhhh"));
//        perform.andExpect(status().isOk());
    }

    @Test
    void topicHome() {
    }

    @Test
    void addTopic() throws Exception {
        callAdd().andExpect(view().name("redirect:/admin/lang"));
    }

    @Test
    void addTopic2() throws Exception {
        callAdd();
        callAdd().andExpect(view().name("redirect:/admin/lang"));
    }

    private ResultActions callAdd() throws Exception {
        mockMvc.perform(post("/admin/lang/add")
                .param("title", "Java"));
        final MockHttpServletRequestBuilder request =
                post("/admin/topic/add")
                        .param("name", "hhhh")
                        .param("content", "hhh")
                        .param("language", "Java");
        return mockMvc.perform(request);
    }

    private ResultActions callDelete() throws Exception {
        mockMvc.perform(post("/admin/lang/add")
                .param("title", "Java"));
        final MockHttpServletRequestBuilder request =
                post("/admin/topic/add")
                        .param("name", "hhhh")
                        .param("content", "hhh")
                        .param("language", "Java");
        return mockMvc.perform(request);
    }

    @Test
    void deleteTopic() throws Exception {
        callDelete();
        ResultActions perform = mockMvc.perform(get("/admin/topic/del/{id}", 2));
        perform.andExpect(view().name("redirect:/admin/lang"));
    }

    @Test
    void updateTopic() throws Exception {
        callAdd();
        ResultActions perform = mockMvc.perform(post("/admin/topic/update/{id}", 1)
                .param("name", "jjjj")
                .param("content", "hhh")
                .param("language", "Java"));
        perform.andExpect(view().name("redirect:/admin/lang"));

    }
}