package org.example.pulsedesk.service;

import org.example.pulsedesk.dto.CommentRequest;
import org.example.pulsedesk.dto.CommentResponse;
import org.example.pulsedesk.dto.TriageResult;
import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.model.Ticket;
import org.example.pulsedesk.repository.CommentRepository;
import org.example.pulsedesk.repository.TicketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final HuggingFaceService huggingFaceService;

    public CommentService(CommentRepository commentRepository,
                          TicketRepository ticketRepository,
                          HuggingFaceService huggingFaceService) {
        this.commentRepository = commentRepository;
        this.ticketRepository = ticketRepository;
        this.huggingFaceService = huggingFaceService;
    }

    public CommentResponse createComment(CommentRequest request) {
        Comment comment = new Comment(request.getText(), request.getSource(), LocalDateTime.now());
        comment = commentRepository.save(comment);

        TriageResult triage = huggingFaceService.analyzeComment(comment.getText());

        comment.setAnalyzed(true);
        comment.setShouldCreateTicket(triage.isShouldCreateTicket());
        comment = commentRepository.save(comment);

        Long ticketId = null;

        if (triage.isShouldCreateTicket()) {
            Ticket ticket = new Ticket(
                    comment,
                    triage.getTitle(),
                    triage.getCategory(),
                    triage.getPriority(),
                    triage.getSummary(),
                    LocalDateTime.now()
            );
            ticket = ticketRepository.save(ticket);
            comment.setTicket(ticket);
            comment = commentRepository.save(comment);
            ticketId = ticket.getId();
        }

        return new CommentResponse(
                comment.getId(),
                comment.getText(),
                comment.getSource(),
                comment.getCreatedAt(),
                comment.isAnalyzed(),
                comment.getShouldCreateTicket(),
                ticketId
        );
    }

    public List<Comment> getAllComments() {
        return commentRepository.findAll();
    }
}