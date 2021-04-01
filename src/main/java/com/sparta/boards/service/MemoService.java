package com.sparta.boards.service;

import com.sparta.boards.domain.Board;
import com.sparta.boards.domain.Memo;
import com.sparta.boards.dto.BoardDto;
import com.sparta.boards.dto.MemoDto;
import com.sparta.boards.repository.BoardRepository;
import com.sparta.boards.repository.MemoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class MemoService {

    private final MemoRepository memoRepository;

    @Transactional
    public Long updateMemo(Long id, MemoDto requestDto) {

        Memo memo = memoRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("아이디가 존재하지 않습니다.")
        );
        memo.update(requestDto);
        return memo.getId();
    }

}


