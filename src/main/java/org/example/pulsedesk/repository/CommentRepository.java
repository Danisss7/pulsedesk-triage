package org.example.pulsedesk.repository;

import org.example.pulsedesk.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}