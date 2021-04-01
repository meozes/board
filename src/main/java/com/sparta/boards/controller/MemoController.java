package com.sparta.boards.controller;

import com.sparta.boards.domain.Memo;
import com.sparta.boards.dto.BoardDto;
import com.sparta.boards.dto.MemoDto;
import com.sparta.boards.repository.MemoRepository;
import com.sparta.boards.security.UserDetailsImpl;
import com.sparta.boards.service.BoardService;
import com.sparta.boards.service.MemoService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MemoController {


    private final MemoService memoService;
    private final MemoRepository memoRepository;


    /* 댓글 조회 */
    @ResponseBody
    @GetMapping("/post/{postId}/comment")
    public List<Memo> getMemo(@PathVariable Long postId) {
        return memoRepository.findByPostIdOrderByModifiedDateDesc(postId);
    }


    /* 댓글 입력 */
    @ResponseBody
    @PostMapping("/post/{postId}/comment")
    public Memo createMemo(@PathVariable Long postId, @RequestBody MemoDto memoDto) {
        memoDto.setPostId(postId);

//        System.out.println(memoDto.getUsername());
//
//        if(memoDto.getUsername().equals("anonymousUser")) {
//            throw new IllegalArgumentException("로그인 하지 않은 사용자 입니다.");
//        }
        Memo memo = new Memo(memoDto);
        return memoRepository.save(memo);
    }

    /* 댓글 수정 */
    @ResponseBody
    @PutMapping("/post/{postId}/comment/{id}")
    public Long updateMemo(@PathVariable Long postId, @PathVariable Long id, @RequestBody MemoDto memoDto) {
        memoDto.setPostId(postId);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!memoDto.getUsername().equals(auth.getName())){
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다");
        }

        return memoService.updateMemo(id, memoDto);

   }

    /* 댓글 삭제 */
    @ResponseBody
    @DeleteMapping("/comment/{id}")
    public Long deleteMemo(@PathVariable Long id, @RequestBody MemoDto memoDto) {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!memoDto.getUsername().equals(auth.getName())){
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다");
        }

        memoRepository.deleteById(id);
        return id;
    }

    //get : /post/{id}/comment, post : /post/{id}/comment, put : /post/{id}/comment/{id}, delete : /post/{id}/comment/{id}

}




