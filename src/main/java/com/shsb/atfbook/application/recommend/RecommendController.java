package com.shsb.atfbook.application.recommend;

import com.shsb.atfbook.application.aladin.AladinBookRepository;
import com.shsb.atfbook.application.aladin.AladinService;
import com.shsb.atfbook.application.history.HistoryRepository;
import com.shsb.atfbook.application.recommend.argumentresolver.Login;
import com.shsb.atfbook.domain.aladin.AladinBook;
import com.shsb.atfbook.domain.aladin.AladinException;
import com.shsb.atfbook.domain.aladin.AladinRequest;
import com.shsb.atfbook.domain.history.History;
import com.shsb.atfbook.domain.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class RecommendController {
    private final RecommendService recommendService;
    private final HistoryRepository historyRepository;
    private final AladinService aladinService;
    private final AladinBookRepository aladinBookRepository;

    @GetMapping("")
    public String index(Model model, RecommendRequest recommendRequest, @Login Member loginMember) {
        List<AladinBook> aladinBookList = this.getAladinBookList(recommendRequest, loginMember);
        return "";
    }

    private List<AladinBook> getAladinBookList(RecommendRequest recommendRequest, Member loginMember) {
        List<History> histories = historyRepository.findHistoriesByLoginId(loginMember.getLoginId());
        var aladinBooks = aladinBookRepository.findAll(Sort.by(Sort.Direction.DESC, "itemId"));
        if (ObjectUtils.isEmpty(aladinBooks)) throw new AladinException("더이상 추천드릴 책이 없습니다.");
        recommendRequest.setHistories(histories);
        recommendService.filterForUser(aladinBooks, recommendRequest);
        if (ObjectUtils.isEmpty(aladinBooks)) {
            historyRepository.deleteHistoriesByMemberId(loginMember.getLoginId());
            return this.getAladinBookList(recommendRequest, loginMember);
        }
        return aladinBooks;
        //AladinRequest aladinRequest = AladinRequest.create(recommendRequest, histories);
        //return aladinService.bookListForBatch(aladinRequest);
    }


}
