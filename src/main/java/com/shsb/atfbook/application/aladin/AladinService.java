package com.shsb.atfbook.application.aladin;

import com.shsb.atfbook.application.recommend.RecommendRequest;
import com.shsb.atfbook.domain.aladin.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AladinService {
    private final RestClient aladinApi;
    private final Environment env;
    //private final AladinBookRepository aladinBookRepository;

    public List<AladinBook> bookListForBatch(AladinRequest aladinRequest) {
        var aladinBooks = this.getApi(AladinConstants.ITEM_LIST, aladinRequest).getItem();
        if (aladinBooks.isEmpty()) {
            throw new AladinException("상품조회시 데이터가 없습니다.");
        }
        return aladinBooks;
    }
    //책 상세 조회
    public AladinBook bookDetail(AladinRequest aladinRequest) {
        var aladinBooks = this.getApi(AladinConstants.ITEM_LOOKUP, aladinRequest).getItem();
        if (aladinBooks.isEmpty()) throw new AladinException("상품조회시 데이터가 없습니다.");

        return aladinBooks.get(0);
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
            log.error("[알라딘] 에러 메세지 파싱 에러 code={}, errorMessage={}", response.getStatusCodeValue(), e.getMessage(), e);
            throw new AladinException("파싱에러", HttpStatus.valueOf(response.getStatusCodeValue()));
        }

    }
}
