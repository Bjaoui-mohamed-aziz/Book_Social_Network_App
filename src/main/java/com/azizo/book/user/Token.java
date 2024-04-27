package com.azizo.book.user;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="token")
public class Token {

    @Id
    @GeneratedValue
    private Integer id;
    private Integer token;
    private LocalDateTime createAt;
    private LocalDateTime expiresAt;
    private LocalDateTime validatedAt;

    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;
}