package org.zerock.board.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.zerock.board.entity.Board;
import org.zerock.board.repository.search.SearchBoardRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board,Long>, SearchBoardRepository {
    @Query("select b, w from Board b left join b.writer w where b.bno =:bno")
    Object getBoardWithWriter(@Param("bno") Long bno);


    @Query("select b,r from Board b left join Reply r ON r.board = b where b.bno =:bno")
    List<Object[]> getBoardWithReply(@Param("bno") Long bno);


    // 게시글 목록 [게시글,작성자,댓글수] x n
    @Query(value = "select b, w, count(r) from Board b"
        +" left join b.writer w"
        +" left join Reply r ON r.board = b"
        +" group by b", countQuery = "select count(b) from Board b")
    Page<Object[]> getBoardWithReplyCount(Pageable pageable);


    // 게시글 detail,  [게시글, 작성자, 댓글수] x 1
    @Query("select b, w, count(r) from Board b left join b.writer w left outer join Reply r on r.board = b where b.bno = :bno")
    Object getBoardByBno(@Param("bno") Long bno);



}

