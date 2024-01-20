package com.study.board.controller;

import com.study.board.entity.Board;
import com.study.board.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller

public class Boardcontroller {
    @Autowired
    private BoardService boardService;

    @GetMapping("/board/write")//localhost:8080/board/write
    public String boardWriteFrom() {
        return "boardwrite";
    }

    @PostMapping("/board/writepro")
    public String boardWritePro(Board board, Model model) {

        boardService.write(board);

        model.addAttribute("message", "글 작성이 완료되었습니다."); //작성완료버튼을 눌렀을 때 띄워줄 메세지 내용입니다.
        model.addAttribute("searchUrl", "/board/list"); //model에 담겨서 message.html에 넘어옵니다(??)

        return "message";
    }

    //게시글 리스트 처리
    @GetMapping("/board/list")
    public String boardList(Model model, @PageableDefault(page=0, size=10, sort="id",
            direction = Sort.Direction.DESC) Pageable pageable)
    //디폴트 페이지:0, 한 페이지당 게시글 수:10개, 정렬 기준 방식: id, 정렬 순서:역순
    {
        Page<Board> list = boardService.boardList(pageable);

        int nowPage = list.getPageable().getPageNumber() + 1; //Pageable에서 넘어온 현재 페이지. 시작값이 0이기 때문에 편의를 위하여 1을 더해주었습니다.
        int startPage = Math.max(nowPage - 4, 1); //블럭에서 보여줄 시작 페이지. Math.max를 이용해 페이지 번호가 음수가 되지 않도록 설정했습니다.
        int endPage = Math.min(nowPage + 5, list.getTotalPages()); //블럭에서 보여줄 마지막 페이지. Math.min을 이용해 페이지 번호가 총 페이지수를 넘을경우 totalpages를 반환하도록 설정했습니다.

        model.addAttribute("list", boardService.boardList());
        model.addAttribute("nowPage", nowPage);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);

        return "boardlist";
    }

    @GetMapping("/board/view") // localhost:8080/board/view?id=1
    public String boardView(Model model, Integer id) {
        model.addAttribute("board", boardService.boardView(id));
        return "boardview";
    }

    @GetMapping("/board/delete")
    public String boardDelete(Integer id) {
        boardService.boardDelete(id);
        return "redirect:/board/list"; //원하는 페이지를 삭제한 후에는 리스트 페이지로 되돌아갑니다.
    }
}
