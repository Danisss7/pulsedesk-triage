package org.example.pulsedesk.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CommentRequest {

    @NotBlank(message = "text is required")
    @Size(max = 5000, message = "text must be at most 5000 characters")
    private String text;

    @NotBlank(message = "source is required")
    @Size(max = 100, message = "source must be at most 100 characters")
    private String source;

    public CommentRequest() {}

    public String getText() { return text; }
    public String getSource() { return source; }

    public void setText(String text) { this.text = text; }
    public void setSource(String source) { this.source = source; }
}