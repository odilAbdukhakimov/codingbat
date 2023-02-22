package uz.pdp.spring_boot_security_web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;
import uz.pdp.spring_boot_security_web.service.LanguageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class LanguageControllerTest extends BaseTest {
    private static final String PATH_ADD = "/admin/lang/add";
    private static final String PATH_DEL = "/admin/lang/delete";
    @BeforeAll
    static void beforeAll() {
        Startables.deepStart(postgres).join();
    }

    @AfterEach
    void afterAll() {
        languageRepository.deleteAll();
    }

    @Test
    void addLanguage() throws Exception {
        callAdd().andExpect(status().isOk());
    }
    @Test
    void deleteLanguageShouldReturnOKStatus() throws Exception{
        callAdd();
        deleteLanguage(1).andExpect(status().isOk());
    }
    @Test
    void deleteLanguageShouldThrowUserExist() throws Exception{
        deleteLanguage(1).andExpect(status().isNotFound());
    }
    private ResultActions callAdd() throws Exception {

        final MockHttpServletRequestBuilder request =
                post(PATH_ADD)
                        .param("title", "Java");
        return mockMvc.perform(request);
    }
    private ResultActions deleteLanguage(int id) throws Exception {
        final MockHttpServletRequestBuilder request =
                delete(PATH_DEL+"/"+id);
        return mockMvc.perform(request);
    }
}