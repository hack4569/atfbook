package com.shsb.atfbook.application.recommend;

import com.shsb.atfbook.domain.aladin.AladinBook;
import com.shsb.atfbook.domain.history.History;
import com.shsb.atfbook.domain.shared.LocalDateUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

@Service
public class RecommendService {

    public void filter(List<AladinBook> newAladinBooks, RecommendRequest recommendRequest) {
        if (ObjectUtils.isEmpty(newAladinBooks)) return;
        newAladinBooks = newAladinBooks.stream()
                .filter(categoryFilter(recommendRequest.getCids()))
                .filter(publicationDateFilter(this.anchorDate()))
                .toList();

    }

    private String anchorDate() {
        //오늘 날짜
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, -1);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
        String today = dateFormatter.format(cal.getTime());
        return today;
    }

    private Predicate categoryFilter(Set finalCids) {
        if (finalCids == null) return book -> true;
        // 허용된 카테고리만 필터링하는 Predicate
        Predicate<AladinBook> categoryFilter = book ->
                finalCids.contains(book.getCategoryId());
        return categoryFilter;
    }

    private Predicate publicationDateFilter(String publicationDate) {
        if (!StringUtils.hasText(publicationDate)) return book -> false;
        // 1년 이내 출간된 책을 필터링하는 Predicate
        Predicate<AladinBook> publicationDateFilter = book ->
                Integer.parseInt(publicationDate) >
                        Integer.parseInt(LocalDateUtils.getCustomDate(book.getPubDate()));
        return publicationDateFilter;
    }

    private Predicate historyFilter(List<History> histories) {
        // 히스토리에 없는 책을 필터링하는 Predicate
        if (ObjectUtils.isEmpty(histories)) return book -> true;

        // 히스토리에 없는 책을 필터링하는 Predicate
        Predicate<AladinBook> historyFilter = book -> {

            return histories.stream().noneMatch(history ->
                    book.getItemId() == history.getItemId() &&
                            LocalDate.now().isEqual(history.getCreated().toLocalDate())
            );
        };
        return historyFilter;
    }

    public void filterForUser(List<AladinBook> aladinBooks, RecommendRequest recommendRequest) {
        if (ObjectUtils.isEmpty(aladinBooks)) return;
        aladinBooks = aladinBooks.stream()
                .filter(historyFilter(recommendRequest.getHistories()))
                .toList();

    }
}
