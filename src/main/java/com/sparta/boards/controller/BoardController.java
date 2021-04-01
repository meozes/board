package com.sparta.boards.controller;

import com.sparta.boards.domain.Memo;
import com.sparta.boards.dto.BoardDto;
import com.sparta.boards.dto.MemoDto;
import com.sparta.boards.security.UserDetailsImpl;
import com.sparta.boards.service.BoardService;
import com.sparta.boards.service.MemoService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.hibernate.internal.util.collections.CollectionHelper.isNotEmpty;

@Controller
public class BoardController {

    private BoardService boardService;
    private MemoService memoService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }


    @GetMapping("/list")
    public String list(Model model) {
        List<BoardDto> boardDtoList = boardService.getBoardList();
        model.addAttribute("postList", boardDtoList);
        return "board/list.html";
    }


    @GetMapping("/post")
    public String post(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
//        model.addAttribute("username", userDetails.getUsername());
        model.addAttribute("username", userDetails.getUser().getUsername());

        return "board/post.html";
    }

    @PostMapping("/post")
    public String write(BoardDto boardDto) {
        boardService.savePost(boardDto);
        return "redirect:/list";

    }

    @GetMapping("/post/{id}")
    public String detail(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);

        if (userDetails != null) {
            model.addAttribute("username", userDetails.getUser().getUsername());
            return "board/detail.html";
        } else {
            model.addAttribute("username", "anonymousUser");
            return "board/detail.html";
        }
    }

    @GetMapping("/post/edit/{id}")
    public String edit(@PathVariable("id") Long id, Model model) {
        BoardDto boardDto = boardService.getPost(id);
        model.addAttribute("post", boardDto);
        return "board/edit.html";
    }

//    @ResponseBody
    @PutMapping("/edit/{id}")
    public String update(BoardDto boardDto) {
        boardService.savePost(boardDto);
//        return "redirect:/";
        return "redirect:/list";
    }

//    @ResponseBody
    @DeleteMapping("/edit/{id}")
    public String delete(@PathVariable("id") Long id) {
        boardService.deletePost(id);
//        return "redirect:/";
        return "redirect:/list";
    }
}
