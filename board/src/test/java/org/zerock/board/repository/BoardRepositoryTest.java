package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;
import org.zerock.board.entity.Reply;
import org.zerock.board.service.BoardService;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
class BoardRepositoryTest {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Test
    public void insertBoard(){
        IntStream.rangeClosed(1,100).forEach(i->{
                Member member = Member.builder().email("user"+i+"@aaa.com").build();
                Board board = Board.builder()
                        .title("Title..."+i)
                        .content("Content..."+i)
                        .writer(member)
                        .build();
                boardRepository.save(board);
        });
    }

    @Transactional
    @Test
    public void testRead1(){
        Optional<Board> result = boardRepository.findById(100L);
        Board board = result.get();
        System.out.println(board);
        System.out.println(board.getWriter());


    }


    @Test
    public void testReadWithWriter(){
        Object result = boardRepository.getBoardWithWriter(100L);
        Object[] arr = (Object[]) result;
        Board board = (Board) arr[0];
        Member member = (Member) arr[1];

        System.out.println(Arrays.toString(arr));
        System.out.println(board);
        System.out.println(member);
    }


    @Test
    public void testGetBoardWithReply(){
        List<Object[]> result = boardRepository.getBoardWithReply(100L);
        for(Object[] arr : result){
            System.out.println(Arrays.toString(arr));
            Board board = (Board)arr[0];
            Reply reply = (Reply)arr[1];

            System.out.println(board);
            System.out.println(reply);
        }
    }

    @Test
    public void testWithReplyCount(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("bno").descending());

        Page<Object[]> result = boardRepository.getBoardWithReplyCount(pageable);

        result.get().forEach(row->{
            Object[] arr = (Object[]) row; // [Board,Member,int], [게시글,작성자,댓글수]
            System.out.println(Arrays.toString(arr));
        });

    }

    @Test
    public void testRead3(){
        Object result = boardRepository.getBoardByBno(100L);
        Object[] arr = (Object[]) result;
        System.out.println(Arrays.toString(arr));
    }


    @Test // dto 전달해서 게시글 생성
    public void testRegister(){
        BoardDTO dto = BoardDTO.builder()
                .title("Test")
                .content("Test...")
                .writerEmail("user10@aaa.com")
                .build();
        Long bno = boardService.register(dto);
    }

    // 글목록 가져오기
    @Test // requestdto 로 요청해서 resultDTO에 결과 받아오기
    public void testList(){
        // 요청객체 생성, 기본값으로
        PageRequestDTO pageRequestDTO =  new PageRequestDTO();
        // 10개의 요소로 구성되는 1페이지
        PageResultDTO<BoardDTO, Object[]> result = boardService.getList(pageRequestDTO);

        for(BoardDTO boardDTO : result.getDtoList()){
            System.out.println(boardDTO);
        }
    }



    @Test
    public void testGet(){
        BoardDTO boardDTO = boardService.get(100L);
        System.out.println(boardDTO);
    }

    @Test
    public void testRemove(){
        Long bno=1L;
        boardService.removeWithReplies(bno);
    }


    @Test
    public void testModify(){
        BoardDTO boardDTO = BoardDTO.builder()
                .bno(2L)
                .title("제목 변경......")
                .content("내용 변경 .......")
                .build();
        boardService.modify(boardDTO);
    }



    @Test
    public void testSearch1(){
        boardRepository.search1();
    }



    @Test
    public void testSearchPage(){
        Pageable pageable = PageRequest.of(0,10,Sort.by("bno").descending().and(Sort.by("title").ascending()));
        Page<Object[]> result = boardRepository.searchPage("t", "1", pageable);

    }
}