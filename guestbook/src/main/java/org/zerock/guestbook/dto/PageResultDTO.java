package org.zerock.guestbook.dto;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
        this.dtoList=result.stream().map(fn).collect(Collectors.toList());
        // 전체 페이지수
        this.totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }

    private void makePageList(Pageable pageable){
        // 요청한 페이지 번호
        this.page=pageable.getPageNumber()+1;
        // 요청한 페이지에 들어있는 개수
        this.size=pageable.getPageSize();

        // 페이지 개수가 충분한 경우 마지막 페이지
        int tempEnd = (int)(Math.ceil((double)page/size))*size;

        this.start = tempEnd - size +1;
        this.prev = start > 1;
        this.next = totalPage > tempEnd;
        this.end = next ? tempEnd : totalPage;

        // boxed() primitive 타입 -> Wrapped Type으로
        this.pageList = IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }
}
