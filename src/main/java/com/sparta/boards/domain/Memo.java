package com.sparta.boards.domain;

import com.sparta.boards.dto.MemoDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Memo{

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    private Long id;

    @Column
    private Long postId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String contents;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime modifiedDate;

    public Memo(MemoDto requestDto) {
        this.id = requestDto.getId();
        this.postId = requestDto.getPostId();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

    public void update(MemoDto requestDto) {
        this.postId = requestDto.getPostId();
        this.username = requestDto.getUsername();
        this.contents = requestDto.getContents();
    }

}
