package org.example.pulsedesk.controller;

import org.example.pulsedesk.dto.CommentRequest;
import org.example.pulsedesk.dto.CommentResponse;
import org.example.pulsedesk.dto.ResponseMapper;
import org.example.pulsedesk.service.CommentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public CommentResponse createComment(@RequestBody CommentRequest request) {
        return commentService.createComment(request);
    }

    @GetMapping
    public List<CommentResponse> getAllComments() {
        return commentService.getAllComments()
                .stream()
                .map(ResponseMapper::toCommentResponse)
                .toList();
    }
}