package com.sparta.boards.dto;

import com.sparta.boards.domain.Board;
import com.sparta.boards.domain.Memo;
import com.sparta.boards.security.UserDetailsImpl;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class MemoDto {
    private Long id;
    private Long postId;
    private String username;
    private String contents;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
}
