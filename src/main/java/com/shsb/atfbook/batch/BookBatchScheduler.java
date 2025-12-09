package com.shsb.atfbook.batch;


import com.shsb.atfbook.application.aladin.AladinBookRepository;
import com.shsb.atfbook.application.aladin.AladinService;
import com.shsb.atfbook.application.category.CategoryService;
import com.shsb.atfbook.application.gpt.GptService;
import com.shsb.atfbook.application.recommend.RecommendRequest;
import com.shsb.atfbook.application.recommend.RecommendService;
import com.shsb.atfbook.domain.aladin.AladinBook;
import com.shsb.atfbook.domain.aladin.AladinRequest;
import com.shsb.atfbook.domain.recommend.RcmdConst;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookBatchScheduler {

    private final CategoryService categoryService;
    private final RecommendService recommendService;
    private final AladinService aladinService;
    private final GptService gptService;
    private final AladinBookRepository aladinBookRepository;

    //
    //@Scheduled(cron = "0 30 2 * * *", zone = "Asia/Seoul")
    //@Scheduled(cron = "0 0 2 * * *", zone = "Asia/Seoul")
    public void scheduleBestSellerBatch() {
        RecommendRequest recommendReq = RecommendRequest.create(categoryService);

        List<AladinBook> aladinBookList = Collections.emptyList();
        int retry = 0;
        int maxRetry = 5;              // 최대 5회 재시도
        var aleadyRegisteredBooks = aladinBookRepository.findAll().stream().map(AladinBook::getItemId).collect(Collectors.toSet());
        while(retry < maxRetry) {
            aladinBookList = this.getAladinBookList(recommendReq);
            var newAladinBooks = aladinBookList.stream().filter(i -> !aleadyRegisteredBooks.contains(i.getItemId())).toList();
            var newFilteredNewAladinBooks = recommendService.filter(newAladinBooks, recommendReq);
            if (ObjectUtils.isEmpty(newFilteredNewAladinBooks)){
                retry ++;
                recommendReq.nextPage();
                continue;
            } else {
                retry = 0;
            }
            this.saveNewAladinBooks(newFilteredNewAladinBooks);
            recommendReq.nextPage();
        }
    }

    private void saveNewAladinBooks(List<AladinBook> newAladinBooks) {
         if (!newAladinBooks.isEmpty()) {
            List<AladinBook> aladinDetailList = new ArrayList<>();
            newAladinBooks.forEach( aladinBook -> {
                var aladinDetail = aladinService.bookDetail(AladinRequest.create(aladinBook.getIsbn13()));
                aladinDetailList.add(aladinDetail);
            });
            aladinBookRepository.saveAll(aladinDetailList);
        }
    }

    public List<AladinBook> getAladinBookList(RecommendRequest recommendReq) {
        AladinRequest aladinRequest = AladinRequest.create(recommendReq);
        return aladinService.bookListForBatch(aladinRequest);
    }
}
