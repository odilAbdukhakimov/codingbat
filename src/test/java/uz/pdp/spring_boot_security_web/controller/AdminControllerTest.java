package uz.pdp.spring_boot_security_web.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class AdminControllerTest extends BaseTest {
    private final static String PATH_ADD = "/api/admin/add";
    private final static String PATH_list = "/api/admin/list";

    private static final String ADMIN_UPDATE_PATH = "/api/admin/update/{username}";

    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterAll() {
        userRepository.deleteAll();
    }

    @Test
    public void addUserShouldReturnOKStatus() throws Exception {
        callAdd().andExpect(status().isOk());

    }

    @Test
    public void addUserShouldThrowUserExist() throws Exception {
        callAdd();
        callAdd().andExpect(status().isBadRequest());
    }

    private ResultActions callAdd() throws Exception {
        final MockHttpServletRequestBuilder request =
                post(PATH_ADD)
                        .param("name", "5")
                        .param("username", "5")
                        .param("password", "5")
                        .param("role", "ADMIN")
                        .param("permissions", "ADD");
        return mockMvc.perform(request);
    }

    private ResultActions callList() throws Exception {
        callAdd();
        final MockHttpServletRequestBuilder request =
                get(PATH_list);
        return mockMvc.perform(request);
    }
    @Test
    public void updateAdminShouldReturnStatusOkStatus() throws Exception {
        callAdd();
        callUpdate().andExpect(status().isOk());
    }

    @Test
    public void updateAdminShouldReturnThrowException() throws Exception {
        callAdd();
        callUpdate().andExpect(status().isBadRequest());
    }

    @SneakyThrows
    private ResultActions callUpdate() {
        final MockHttpServletRequestBuilder request =
                put(ADMIN_UPDATE_PATH, "5")
                        .param("username", "ASIL")
                        .param("password", "1999")
                        .param("name", "Nuraliyev Asilbek")
                        .param("role", "[" +"SUPER_ADMIN"+ "ADMIN" + "USER" + "]")
                        .param("permissions", "[" + "ADD" + "READ" + "]");
        return mockMvc.perform(request);
    }
}