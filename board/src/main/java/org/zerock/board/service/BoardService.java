package org.zerock.board.service;

import org.zerock.board.dto.BoardDTO;
import org.zerock.board.dto.PageRequestDTO;
import org.zerock.board.dto.PageResultDTO;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

public interface BoardService {
    // create
    Long register(BoardDTO dto);

    // getList
    PageResultDTO<BoardDTO,Object[]> getList(PageRequestDTO pageRequestDTO);
    // get
    BoardDTO get(Long bno);


    // delete
    void removeWithReplies(Long bno);

    // update
    void modify(BoardDTO boardDto);


    // mapping (board)dto to (board)entity
    default Board dtoToEntity(BoardDTO dto){
        Member member = Member.builder()
            .email(dto.getWriterEmail())
            .build();
        Board board = Board.builder()
                .bno(dto.getBno())
                .title(dto.getTitle())
                .content(dto.getContent())
                .writer(member)
                .build();
        return board;
    };


    // mapping (board)entity to (board)dto
    default BoardDTO entityToDTO(Board board, Member member, Long replyCount){
        BoardDTO dto = BoardDTO.builder()
                .bno(board.getBno())
                .title(board.getTitle())
                .content(board.getContent())
                .writerEmail(member.getEmail())
                .writerName(member.getName())
                .regDate(board.getRegDate())
                .modDate(board.getModDate())
                .replyCount(replyCount.intValue()) // int 형변환
                .build();

        return dto;
    };



}
