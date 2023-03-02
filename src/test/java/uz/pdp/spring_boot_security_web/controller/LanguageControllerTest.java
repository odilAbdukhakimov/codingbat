package uz.pdp.spring_boot_security_web.controller;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.testcontainers.lifecycle.Startables;
import uz.pdp.spring_boot_security_web.model.dto.LanguageRequestDTO;
import uz.pdp.spring_boot_security_web.service.LanguageService;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.security.test.web.servlet.response.SecurityMockMvcResultMatchers.authenticated;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

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
        ResultActions glklkdgfl = mockMvc.perform(post("/admin/lang/add")
                .param("title","Java"));
        glklkdgfl.andExpectAll(status().isOk());
    }  @Test
    void addLanguage2() throws Exception {
        callAdd();
        callAdd().andExpect(status().isBadRequest());
    }
    @Test
    void deleteLanguageShouldReturnOKStatus() throws Exception{
        callAdd();
        ResultActions perform = mockMvc.perform(get("/admin/lang/del/{id}", 3));
        perform.andExpect(view().name("redirect:/admin/lang"));
    }
//    @Test
//    void deleteLanguageShouldThrowUserExist() throws Exception{
//        callAdd();
//        ResultActions perform = mockMvc.perform(get("/admin/del/{id}", 1));
//        perform.andExpect(view().name("redirect:/admin/lang"));
//    }

    private ResultActions callAdd() throws Exception {
        final MockHttpServletRequestBuilder request =
                post(PATH_ADD)
                        .param("title", "Java");
        return mockMvc.perform(request);
    }

    @Test
    void langPage() throws Exception {
        callAdd();
        mockMvc.perform(get("/admin/lang")).andExpect(status().isOk());
    }

    @Test
    void updateLanguage() throws Exception {
        callAdd();
        ResultActions glklkdgfl = mockMvc.perform(post("/admin/lang/update/{id}",2)
                .param("title","Java"));
        glklkdgfl.andExpect(view().name("redirect:/admin/lang"));

    }
//    private ResultActions callUpdate() throws Exception {
//        final MockHttpServletRequestBuilder request =
//                post(PATH_ADD)
//                        .param("id","1")
//                        .param("title", "Java");
//        return mockMvc.perform(request);
//    }
}