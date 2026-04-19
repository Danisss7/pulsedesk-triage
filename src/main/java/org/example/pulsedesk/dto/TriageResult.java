package org.example.pulsedesk.dto;

public class TriageResult {

    private boolean shouldCreateTicket;
    private String title;
    private String category;
    private String priority;
    private String summary;

    public TriageResult() {}

    public boolean isShouldCreateTicket() { return shouldCreateTicket; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public String getPriority() { return priority; }
    public String getSummary() { return summary; }

    public void setShouldCreateTicket(boolean shouldCreateTicket) { this.shouldCreateTicket = shouldCreateTicket; }
    public void setTitle(String title) { this.title = title; }
    public void setCategory(String category) { this.category = category; }
    public void setPriority(String priority) { this.priority = priority; }
    public void setSummary(String summary) { this.summary = summary; }
}