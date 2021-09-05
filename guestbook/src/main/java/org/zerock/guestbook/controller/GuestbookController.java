package org.zerock.guestbook.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.service.GuestbookService;

@Controller
@RequestMapping("/guestbook")
@Log4j2
@RequiredArgsConstructor // 생성자 bean 으로 service 주입
public class GuestbookController {

    private final GuestbookService service ;

    @GetMapping("/")
    public String index(){
        return "redirect:/guestbook/list";
    }

    @GetMapping("/list")  // template에서 list.html을 반환
    public void list(PageRequestDTO pageRequestDTO, Model model){
        log.info("list............."+pageRequestDTO);
        // result : PageResultDTO<GuestbookDTO, Guestbook>
        model.addAttribute("result",service.getList(pageRequestDTO));
    }


    //등록
    @GetMapping("/register")
    public void register(){
        log.info("register get...");
    }

    @PostMapping("/register")
    public String registerPost(GuestbookDTO dto, RedirectAttributes redirectAttributes){
        log.info("dto..."+dto);
        //  dto를 전달받아서 DB에 반영
        Long gno = service.register(dto);

        // 리다이렉트시 전달할 정보
        redirectAttributes.addFlashAttribute("msg",gno);
        return "redirect:/guestbook/list";
    }


    // 조회
    @GetMapping({"/read","/modify"})
    public void read(long gno, @ModelAttribute("requestDTO") PageRequestDTO requestDTO, Model model){
        log.info("gno : "+gno);
        GuestbookDTO dto = service.read(gno);
        model.addAttribute("dto",dto);
    }


    // delete
    @ResponseBody
    @DeleteMapping("/remove")
    public ResponseEntity<String> remove(long gno){
        log.info("gno: "+gno);
        service.remove(gno);
        return new ResponseEntity<>("OK", HttpStatus.OK);
    }



    // update
    @ResponseBody
    @PatchMapping("/modify")
    public ResponseEntity<String> modify(@RequestBody GuestbookDTO dto){
        log.info("post modify..................");
        log.info("dto : "+dto);
        service.modify(dto);
        return new ResponseEntity<>("OK",HttpStatus.OK);
    }



}
