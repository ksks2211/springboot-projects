package org.zerock.board.service;

import org.zerock.board.dto.ReplyDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Reply;

import java.util.List;

public interface ReplyService {

    Long register(ReplyDTO replyDTO);

    List<ReplyDTO> getList(Long bno);

    void modify(ReplyDTO replyDTO);
    void remove(Long rno);


    // default  함수 dto -> entity
    default Reply dtoToEntity(ReplyDTO replyDTO){

        // bno 만 담은 Board 객체 생성
        Board board = Board.builder().bno(replyDTO.getBno()).build();
        // entity 생성
        Reply reply = Reply.builder()
                .rno(replyDTO.getRno())
                .text(replyDTO.getText())
                .replier(replyDTO.getReplier())
                .board(board)
                .build();
        return reply;
    }


    default ReplyDTO entityToDTO(Reply reply){
        ReplyDTO dto = ReplyDTO.builder()
                .rno(reply.getRno())
                .text(reply.getText())
                .replier(reply.getReplier())
                .regDate(reply.getRegDate())
                .modDate(reply.getModDate())
                //.bno(reply.getBoard().getBno())
                .build();
        return dto;
    }
}
