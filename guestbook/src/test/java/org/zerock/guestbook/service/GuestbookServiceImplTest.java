package org.zerock.guestbook.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;



@SpringBootTest
class GuestbookServiceImplTest {

    @Autowired
    private GuestbookService service;

    @Test
    void testRegister() {
        GuestbookDTO guestbookDTO = GuestbookDTO.builder()
                .title("Sample title")
                .content("Sample content")
                .writer("user0")
                .build();
        System.out.println(service.register(guestbookDTO));
    }


    @Test
    public void testList(){

        // 페이지 요청 DTO
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .build();

        // 페이지 요청을 매개변수로해서 서비스에 요청한뒤 응답 정보를 담은 DTO 반환
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);

        System.out.println("PREV : "+resultDTO.isPrev());
        System.out.println("NEXT : "+resultDTO.isNext());
        System.out.println("TOTAL : "+resultDTO.getTotalPage());
        System.out.println("-------------------------------------");
        for(GuestbookDTO guestbookDTO : resultDTO.getDtoList()){
            System.out.println(guestbookDTO);
        }
        System.out.println("=====================================");
        resultDTO.getPageList().forEach(System.out::println);

    }


    @Test
    public void testSearch(){
        PageRequestDTO pageRequestDTO = PageRequestDTO.builder()
                .page(1)
                .size(10)
                .type("tc") // title  or content
                .keyword("modified")
                .build();

        // 페이지 결과를 담은 DTO,
        PageResultDTO<GuestbookDTO, Guestbook> resultDTO = service.getList(pageRequestDTO);


        System.out.println("PREV : "+resultDTO.isPrev());
        System.out.println("NEXT : "+resultDTO.isNext());
        System.out.println("Total : "+resultDTO.getTotalPage());

        System.out.println("-------------------------------");
        for(GuestbookDTO dto : resultDTO.getDtoList()){
            System.out.println(dto);
        }

        System.out.println("==========================================");
        resultDTO.getPageList().forEach(System.out::println);
    }




}