package com.shsb.atfbook.batch;

import com.shsb.atfbook.application.category.CategoryService;
import com.shsb.atfbook.application.recommend.RecommendRequest;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BookBatchSchedulerTest {

    @Autowired
    private BookBatchScheduler bookBatchScheduler;

    @Autowired
    private CategoryService categoryService;

    @Test
    void getAladinBookList() {
        RecommendRequest recommendReq = RecommendRequest.create(categoryService);
        bookBatchScheduler.getAladinBookList(recommendReq);
    }

    @Test
    void 배치_실행_테스트() {
        bookBatchScheduler.scheduleBestSellerBatch();
    }
}