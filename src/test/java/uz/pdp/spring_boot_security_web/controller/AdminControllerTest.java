package uz.pdp.spring_boot_security_web.controller;

import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

class AdminControllerTest extends BaseTest {

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
    public void updateAdminShouldReturnStatusOkStatus() {

    }

//    @Test
//    private ResultActions callAdd() {
//        final MockHttpServletRequestBuilder request =
//                put()
//    }

    @SneakyThrows
    private ResultActions callUpdate() {
        final MockHttpServletRequestBuilder request =
                put(ADMIN_UPDATE_PATH, "asil")
                        .param("username", "ASIL")
                        .param("password", "1999")
                        .param("name", "Nuraliyev Asilbek")
                        .param("role", "[" + "ADMIN" + "USER" + "]")
                        .param("permissions", "[" + "ADD" + "READ" + "]");
        return mockMvc.perform(request);
    }
}