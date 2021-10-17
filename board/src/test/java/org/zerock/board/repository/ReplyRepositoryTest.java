package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class ReplyRepositoryTest {

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertReply(){
        IntStream.rangeClosed(1,300).forEach(i->{
            // 1~100 사이의 랜덤수 생성
            long bno = (long)(Math.random()*100)+1;
            // PK만 가진 Entity 생성
            Board board = Board.builder().bno(bno).build();
            Reply reply = Reply.builder()
                    .text("Reply......"+i)
                    .board(board)
                    .replier("guest")
                    .build();
            replyRepository.save(reply);
        });
    }

    @Test
    public void resdReply1(){
        Optional<Reply> result = replyRepository.findById(1L);

        Reply reply = result.get();

        System.out.println(reply);
        System.out.println(reply.getBoard());

    }

    @Test
    public void testListByBoard(){
        List<Reply> replyList = replyRepository.getRepliesByBoardOrderByRno(
                Board.builder()
                        .bno(97L)
                        .build()
        );

        replyList.forEach(reply-> System.out.println(reply));

    }

}