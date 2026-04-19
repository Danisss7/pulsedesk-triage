package org.example.pulsedesk.dto;

import java.time.LocalDateTime;

public class CommentResponse {
    private Long id;
    private String text;
    private String source;
    private LocalDateTime createdAt;
    private boolean analyzed;
    private Boolean shouldCreateTicket;
    private Long ticketId;

    public CommentResponse() {}

    public CommentResponse(Long id, String text, String source, LocalDateTime createdAt,
                           boolean analyzed, Boolean shouldCreateTicket, Long ticketId) {
        this.id = id;
        this.text = text;
        this.source = source;
        this.createdAt = createdAt;
        this.analyzed = analyzed;
        this.shouldCreateTicket = shouldCreateTicket;
        this.ticketId = ticketId;
    }

    public Long getId() { return id; }
    public String getText() { return text; }
    public String getSource() { return source; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public boolean isAnalyzed() { return analyzed; }
    public Boolean getShouldCreateTicket() { return shouldCreateTicket; }
    public Long getTicketId() { return ticketId; }

    public void setId(Long id) { this.id = id; }
    public void setText(String text) { this.text = text; }
    public void setSource(String source) { this.source = source; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public void setAnalyzed(boolean analyzed) { this.analyzed = analyzed; }
    public void setShouldCreateTicket(Boolean shouldCreateTicket) { this.shouldCreateTicket = shouldCreateTicket; }
    public void setTicketId(Long ticketId) { this.ticketId = ticketId; }
}