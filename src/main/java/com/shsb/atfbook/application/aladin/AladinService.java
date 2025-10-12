package com.shsb.atfbook.application.aladin;

import com.shsb.atfbook.application.gpt.GptService;
import com.shsb.atfbook.application.recommend.RecommendRequest;
import com.shsb.atfbook.domain.aladin.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class AladinService {
    @Qualifier("aladinApi")
    @Autowired
    private RestClient aladinApi;
    @Autowired
    private GptService gptService;
    @Autowired
    private Environment env;
    @Autowired
    private AladinBookRepository aladinBookRepository;
    @Autowired
    private BookCommentRepository commentRepository;
    @Autowired
    private BookCommentRepository bookCommentRepository;

    public List<AladinBook> bookListForBatch(AladinRequest aladinRequest) {
        var aladinBooks = this.getApi(AladinConstants.ITEM_LIST, aladinRequest).getItem();
        if (ObjectUtils.isEmpty(aladinBooks)) throw new AladinException("상품조회시 데이터가 없습니다.");
        return aladinBooks;
    }
    //책 상세 조회
    public AladinBook bookDetail(AladinRequest aladinRequest) {
        var aladinBooks = this.getApi(AladinConstants.ITEM_LOOKUP, aladinRequest).getItem();
        if (aladinBooks.isEmpty()) throw new AladinException("상품조회시 데이터가 없습니다.");

        var aladinbook = aladinBooks.get(0);
        //코멘트 세팅
        aladinbook.settingBookCommentList(gptService);
        return aladinbook;
    }

    //@CircuitBreaker(name = "aladinApi", fallbackMethod = "getApiFallback")
    private AladinResponse getApi(String path, AladinRequest aladinRequest) {
        ResponseEntity<AladinResponse> response = null;
        try{
            response = aladinApi
                    .get()
                    .uri(uriBuilder -> uriBuilder
                            .path(path)
                            .queryParam("ttbkey", env.getProperty("aladin.ttbkey"))
                            .queryParams(aladinRequest.getApiParamMap())
                            .build()
                    )
                    .retrieve()
                    .toEntity(AladinResponse.class);
            return response.getBody();
        } catch (Exception e) {
            log.error("[알라딘] 에러 메세지 파싱 에러 errorMessage={}", e.getMessage(), e);
            throw new AladinException("파싱에러");
        }

    }


    public List<AladinBook> findAll() {
        var aladinBooks = aladinBookRepository.findAll(Sort.by(Sort.Direction.DESC, "itemId"));
        aladinBooks.stream().forEach(i -> {
            var commentList = bookCommentRepository.findBookCommentsByAladinBookItemId(i.getItemId());
            if (Objects.nonNull(commentList)) i.setBookCommentList(commentList);
        });
        return aladinBooks;
    }
}
