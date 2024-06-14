package com.azizo.book.feedback;


import com.azizo.book.common.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("feedbacks")
@Tag(name = "Feedback")
@RequiredArgsConstructor
public class FeedbackController {

    private final FeedbackService service;

    @PostMapping
    public ResponseEntity<Integer> saveFeedback(
            @Valid @RequestBody FeedbackRequest request,
            Authentication connectedUser
    ){
        return ok(service.save(request, connectedUser))
    }

    @GetMapping("/book/{book-id}")
    public ResponseEntity<PageResponse<FeedbackResponse>> findAllFeedbackByBook(
            @PathVariable ("book-id") Integer bookId,
            @RequestParam(name = "page" , defaultValue = "0",required = false) int page,
            @RequestParam(name = "size" , defaultValue = "10",required = false) int size,
            Authentication connectedUser
    ){

        return ResponseEntity.ok(service.finAllFeedbacksByBook(bookId, page, size,connectedUser));
    }

}
