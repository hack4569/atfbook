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
import com.shsb.atfbook.domain.recommend.RcmdConst;
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
        List<RecommendView> recommendViewList = this.getAladinBookList(recommendRequest, loginMember);

        model.addAttribute("recommendList", recommendViewList);
        model.addAttribute("loginMember", loginMember);
        if (loginMember == null) {
            model.addAttribute("loginHistoryMsg", "로그인하시면 봤던 책정보는 보이지 않습니다.");
        }

        return "recommend/index";
    }

    private List<RecommendView> getAladinBookList(RecommendRequest recommendRequest, Member loginMember) {
        if (loginMember != null) {
            List<History> histories = historyRepository.findHistoriesByMemberId(loginMember.getId());
            recommendRequest.setHistories(histories);
        }

        var aladinBooks = aladinService.findAll();
        if (ObjectUtils.isEmpty(aladinBooks)) throw new AladinException("더이상 추천드릴 책이 없습니다.");

        recommendService.filterForUser(aladinBooks, recommendRequest);
        if (ObjectUtils.isEmpty(aladinBooks)) {
            if (loginMember != null) historyRepository.deleteHistoriesByMemberId(loginMember.getId());
            return this.getAladinBookList(recommendRequest, loginMember);
        }
        return this.showUserData(aladinBooks);
    }

    private List<RecommendView> showUserData(List<AladinBook> aladinBooks) {

        List<RecommendView> slideRecommendList = new ArrayList<>();
        for (int i = 0; i < RcmdConst.SHOW_BOOKS_COUNT; i++) {
            var book = aladinBooks.get(i);
            RecommendView recommendDto = RecommendView.builder()
                    .itemId(book.getItemId())
                    .title(book.getTitle())
                    .link(book.getLink())
                    .cover(book.getCover())
                    .recommendCommentList(book.getBookCommentList())
                    .author(book.getAuthor())
                    .categoryName(book.getCategoryName())
                    .build();
            slideRecommendList.add(recommendDto);
        }
        return slideRecommendList;

    }


}
