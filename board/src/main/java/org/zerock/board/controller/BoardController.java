package org.zerock.board.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.service.BoardService;

@Controller
@RequestMapping("/board/")
@Log4j2
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;

    // 목록 요청 url (/board/list? => list.html
    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list.............."+pageRequestDTO);
        model.addAttribute("result",boardService.getList(pageRequestDTO));
    }


    // 등록 양식 페이지 반환 register.html
    @GetMapping("/register")
    public void register(){
        log.info("register get...");
    }

    // 실제 데이터 등록
    @PostMapping("/register")
    public String registerPost( BoardDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto ......"+dto);
        Long bno = boardService.register(dto);
        log.info("BNO : "+bno);
        // 등록된 게시글의 번호를 msg변수에 반환
        redirectAttributes.addFlashAttribute("msg",bno);
        return "redirect:/board/list";
    }


    // bno를 받아서 해당 게시물 조회, 조회이후에 이전과 같은 리스트로 돌아갈수 있도록 requestDTO 저장 => read.html 반환
    @GetMapping({"/read","/modify"})
    public void read(@ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Long bno, Model model){
        log.info("bno : "+bno);
        BoardDTO boardDTO = boardService.get(bno);
        log.info(boardDTO);
        
        model.addAttribute("dto",boardDTO);
    }


    // 게시물 삭제
    @ResponseBody
    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(long bno){
        log.info("bno : "+bno);
        boardService.removeWithReplies(bno);
        return new ResponseEntity<String>("OK", HttpStatus.OK);
    }

    // 게시물 수정
    @ResponseBody
    @PatchMapping("/modify")
    public ResponseEntity<String> modify(@RequestBody BoardDTO dto){
        log.info("post modify.............");
        log.info("dto : "+dto);
        boardService.modify(dto);

        return new ResponseEntity<>("OK",HttpStatus.OK);
    }


}
