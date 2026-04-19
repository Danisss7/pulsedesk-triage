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
class TicketControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private HuggingFaceService huggingFaceService;

    @Test
    @DisplayName("GET /tickets returns created tickets")
    void shouldReturnTickets() throws Exception {
        TriageResult result = new TriageResult();
        result.setShouldCreateTicket(true);
        result.setTitle("Login issue");
        result.setCategory("account");
        result.setPriority("medium");
        result.setSummary("User cannot log in after password reset.");

        when(huggingFaceService.analyzeComment(anyString())).thenReturn(result);

        mockMvc.perform(post("/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "text": "I can't sign in after resetting my password",
                                  "source": "web-form"
                                }
                                """))
                .andExpect(status().isOk());

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Login issue"))
                .andExpect(jsonPath("$[0].category").value("account"));
    }

    @Test
    @DisplayName("GET /tickets/{id} returns 404 for missing ticket")
    void shouldReturn404ForMissingTicket() throws Exception {
        mockMvc.perform(get("/tickets/999"))
                .andExpect(status().isNotFound());
    }
}