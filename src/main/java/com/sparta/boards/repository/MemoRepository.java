package com.sparta.boards.repository;

import com.sparta.boards.domain.Memo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemoRepository extends JpaRepository<Memo, Long> {
    List<Memo> findByPostIdOrderByModifiedDateDesc(@Param(value = "postId") Long postId);
}
