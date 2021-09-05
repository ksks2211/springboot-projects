package org.zerock.guestbook.service;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.guestbook.dto.GuestbookDTO;
import org.zerock.guestbook.dto.PageRequestDTO;
import org.zerock.guestbook.dto.PageResultDTO;
import org.zerock.guestbook.entity.Guestbook;
import org.zerock.guestbook.entity.QGuestbook;
import org.zerock.guestbook.repository.GuestBookRepository;

import java.util.Optional;
import java.util.function.Function;


@Service
@Log4j2
@RequiredArgsConstructor
public class GuestbookServiceImpl implements GuestbookService{

    private final GuestBookRepository repository;

    @Override
    public Long register(GuestbookDTO dto) {
        log.info("DTO-----------------------------");
        log.info(dto);
        Guestbook entity = dtoToEntity(dto);
        log.info(entity);

        repository.save(entity);
        return entity.getGno();
    }

    @Override
    public GuestbookDTO read(Long gno) {
        Optional<Guestbook> result = repository.findById(gno);
        return result.isPresent() ? entityToDto(result.get()) : null;
    }

    @Override
    public void remove(Long gno) {
        repository.deleteById(gno);
    }

    @Override
    public void modify(GuestbookDTO dto) {
        Optional<Guestbook> result = repository.findById(dto.getGno());
        if(result.isPresent()){
            Guestbook entity = result.get();
            entity.changeTitle(dto.getTitle());
            entity.changeContent(dto.getContent());
            repository.save(entity);
        }
    }

    @Override
    public PageResultDTO<GuestbookDTO, Guestbook> getList(PageRequestDTO requestDTO) {
        Pageable pageable =requestDTO.getPageable(Sort.by("gno").descending());
        BooleanBuilder booleanBuilder = getSearch(requestDTO);

        // querydsl 사용
        Page<Guestbook> result = repository.findAll(booleanBuilder, pageable);

        //Function<Guestbook,GuestbookDTO> fn = (guestbook) -> entityToDto(guestbook);
        Function<Guestbook,GuestbookDTO> fn = this::entityToDto;
        return new PageResultDTO<>(result, fn);
    }



    private BooleanBuilder getSearch(PageRequestDTO requestDTO){
        String type = requestDTO.getType();
        String keyword = requestDTO.getKeyword();

        // booleanBuilder 객체 생성
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QGuestbook qGuestbook = QGuestbook.guestbook;

        // 공통 조건 생성 gno > 0
        BooleanExpression expression =  qGuestbook.gno.gt(0L);
        booleanBuilder.and(expression);

        // 검색조건이 존재하지 않음.
        if(type==null || type.trim().length()==0){
            return booleanBuilder;
        }
        // 검색 조건 존재
        BooleanBuilder conditionBuilder = new BooleanBuilder();

        // 제목을 검색
        if(type.contains("t")){
            conditionBuilder.and(qGuestbook.title.contains(keyword));
        }
        // 내용을 검색
        if(type.contains("c")){
            conditionBuilder.and(qGuestbook.content.contains(keyword));
        }
        // 작성자 검색
        if(type.contains("w")){
            conditionBuilder.and(qGuestbook.writer.contains(keyword));
        }
        booleanBuilder.and(conditionBuilder);
        return booleanBuilder;
    }
}
