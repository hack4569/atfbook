package com.shsb.atfbook.batch;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class BookBatchScheduler {

    //@Scheduled(cron = "0 */1 * * * ?")
    @Scheduled(cron = "0 30 2 * * *", zone = "Asia/Seoul")
    public void scheduleBestSellerBatch() {

    }
}
