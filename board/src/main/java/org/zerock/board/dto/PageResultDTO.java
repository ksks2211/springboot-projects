package org.zerock.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/*
* 페이지 응답용 dto
* */
@Data
public class PageResultDTO<DTO,EN> {

    private List<DTO> dtoList;

    private int totalPage;
    private int page;
    private int size;
    private int start,end;

    private boolean prev,next;
    private List<Integer> pageList;




    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){
        // entity를 dto 로 변환하기
        this.dtoList = result.stream().map(fn).collect(Collectors.toList());
        this.totalPage = result.getTotalPages();
        makePageList(result.getPageable());

    }

    private void makePageList(Pageable pageable){

        this.page = pageable.getPageNumber()+1;
        this.size = pageable.getPageSize();

        int tempEnd = (int)(Math.ceil((double)page/size))*size;
        this.start = tempEnd - size +1;
        this.prev = start >1;
        this.next = totalPage > tempEnd;
        this.end = next ? tempEnd : totalPage;

        this.pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());

    }


}
