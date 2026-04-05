package com.finance.dashboard;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.dashboard.dto.request.LoginRequest;
import com.finance.dashboard.dto.request.RegisterRequest;
import com.finance.dashboard.entity.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class AuthIntegrationTest {

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    // ---- Register ----

    @Test
    void register_withValidData_returns201() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Test User");
        req.setEmail("test@example.com");
        req.setPassword("secret123");
        req.setRole(Role.VIEWER);

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("test@example.com"))
                .andExpect(jsonPath("$.role").value("VIEWER"));
    }

    @Test
    void register_withInvalidEmail_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Bad Email");
        req.setEmail("not-an-email");
        req.setPassword("secret123");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    @Test
    void register_withShortPassword_returns400() throws Exception {
        RegisterRequest req = new RegisterRequest();
        req.setFullName("Short Pass");
        req.setEmail("short@example.com");
        req.setPassword("abc");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    // ---- Login ----

    @Test
    void login_withValidCredentials_returnsToken() throws Exception {
        // First register
        RegisterRequest reg = new RegisterRequest();
        reg.setFullName("Login Test");
        reg.setEmail("login@example.com");
        reg.setPassword("password123");
        reg.setRole(Role.ADMIN);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reg)));

        // Then login
        LoginRequest login = new LoginRequest();
        login.setEmail("login@example.com");
        login.setPassword("password123");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty())
                .andExpect(jsonPath("$.tokenType").value("Bearer"));
    }

    @Test
    void login_withWrongPassword_returns401() throws Exception {
        LoginRequest login = new LoginRequest();
        login.setEmail("nobody@example.com");
        login.setPassword("wrongpassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andExpect(status().isUnauthorized());
    }

    // ---- Role-based Access ----

    @Test
    void viewerCannotAccessRecordsEndpoint() throws Exception {
        // Register viewer
        RegisterRequest reg = new RegisterRequest();
        reg.setFullName("Viewer Test");
        reg.setEmail("viewer2@example.com");
        reg.setPassword("viewer123");
        reg.setRole(Role.VIEWER);

        mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reg)));

        // Login as viewer
        LoginRequest login = new LoginRequest();
        login.setEmail("viewer2@example.com");
        login.setPassword("viewer123");

        MvcResult loginResult = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(login)))
                .andReturn();

        String token = objectMapper.readTree(loginResult.getResponse().getContentAsString())
                .get("token").asText();

        // Viewer should be FORBIDDEN from reading records
        mockMvc.perform(get("/api/records")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isForbidden());
    }
}
