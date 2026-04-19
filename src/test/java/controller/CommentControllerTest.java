package controller;

import org.example.pulsedesk.dto.TriageResult;
import org.example.pulsedesk.service.HuggingFaceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HuggingFaceService huggingFaceService;

    @Test
    @DisplayName("POST /comments creates a ticket when AI marks it actionable")
    void shouldCreateCommentAndTicket() throws Exception {
        TriageResult result = new TriageResult();
        result.setShouldCreateTicket(true);
        result.setTitle("Duplicate billing charge");
        result.setCategory("billing");
        result.setPriority("high");
        result.setSummary("User was charged twice and cannot access invoice.");

        when(huggingFaceService.analyzeComment(anyString())).thenReturn(result);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "I was charged twice and can't access my invoice",
                                  "source": "web-form"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.analyzed").value(true))
                .andExpect(jsonPath("$.shouldCreateTicket").value(true))
                .andExpect(jsonPath("$.ticketId").isNumber());
    }

    @Test
    @DisplayName("POST /comments does not create ticket for praise")
    void shouldCreateCommentWithoutTicket() throws Exception {
        TriageResult result = new TriageResult();
        result.setShouldCreateTicket(false);
        result.setTitle("");
        result.setCategory("other");
        result.setPriority("low");
        result.setSummary("Comment does not require a support ticket.");

        when(huggingFaceService.analyzeComment(anyString())).thenReturn(result);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "Love the app, great work team",
                                  "source": "app-review"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.analyzed").value(true))
                .andExpect(jsonPath("$.shouldCreateTicket").value(false))
                .andExpect(jsonPath("$.ticketId").doesNotExist());
    }

    @Test
    @DisplayName("GET /comments returns stored comments")
    void shouldReturnComments() throws Exception {
        TriageResult result = new TriageResult();
        result.setShouldCreateTicket(false);
        result.setTitle("");
        result.setCategory("other");
        result.setPriority("low");
        result.setSummary("Comment does not require a support ticket.");

        when(huggingFaceService.analyzeComment(anyString())).thenReturn(result);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "Nice UI",
                                  "source": "chat-widget"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].text").exists());
    }

    @Test
    @DisplayName("POST /comments validates required fields")
    void shouldValidateCommentRequest() throws Exception {
        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "",
                                  "source": ""
                                }
                                """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation failed"));
    }
}