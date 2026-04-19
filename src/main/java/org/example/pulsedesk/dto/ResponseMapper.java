package org.example.pulsedesk.dto;

import org.example.pulsedesk.model.Comment;
import org.example.pulsedesk.model.Ticket;

public class ResponseMapper {

    public static CommentResponse toCommentResponse(Comment comment) {
        Long ticketId = comment.getTicket() != null ? comment.getTicket().getId() : null;

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

    public static TicketResponse toTicketResponse(Ticket ticket) {
        Long commentId = ticket.getComment() != null ? ticket.getComment().getId() : null;

        return new TicketResponse(
                ticket.getId(),
                commentId,
                ticket.getTitle(),
                ticket.getCategory(),
                ticket.getPriority(),
                ticket.getSummary(),
                ticket.getCreatedAt()
        );
    }
}