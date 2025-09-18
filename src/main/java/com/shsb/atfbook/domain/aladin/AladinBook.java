package com.shsb.atfbook.domain.aladin;

import com.shsb.atfbook.application.gpt.GptService;
import com.shsb.atfbook.domain.gpt.GptResponse;
import com.shsb.atfbook.domain.shared.BookRecommendUtil;
import com.shsb.atfbook.domain.shared.RcmdConst;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="aladin_book")
public class AladinBook {

    @Id
    private int itemId;
    private String title;
    private String link;
    private String author;
    private String pubDate;
    private String description;
    private String isbn;
    private String isbn13;
    private int priceSales;
    private int priceStandard;
    private String mallType;
    private String stockStatus;
    private int mileage;
    private String cover;
    private int categoryId;
    private String categoryName;
    private String publisher;
    private int salesPoint;
    private Boolean adult;
    private Boolean fixedPrice;
    private int customerReviewRank;
    private int bestRank;

    @Transient
    private SubInfo subInfo;
    //프리미엄
    private String fullDescription;
    private String fullDescription2;
//    @OneToMany(mappedBy = "aladinBook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    public List<Review> reviewList;
    private String toc;
    @OneToMany(mappedBy = "aladinBook", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BookComment> bookCommentList;

    public void setBookCommentList(GptService gptService) {
        List<BookComment> bookCommentList = new ArrayList<>();

        //책소개
        this.setUserBookDesc(bookCommentList);
        //편집자 추천
        this.setUserMdRecommend(bookCommentList);
        //Gpt 추천
        this.setUserGptComment(gptService, bookCommentList);
        //책 속에서
        this.setUserPhrase(bookCommentList);
        //목차
        this.setUserToc(bookCommentList);
    }

    private void setUserMdRecommend(List<BookComment> bookCommentList) {
        List<MdRecommend> mdRecommendList = this.getSubInfo().getMdRecommendList();
        if (!ObjectUtils.isEmpty(mdRecommendList)) {
            for (MdRecommend mdRecommend : mdRecommendList) {
                this.filterDescription(mdRecommend.getComment(), bookCommentList, "mdRecommend");
            }
        }
    }

    private void setUserBookDesc(List<BookComment> bookCommentList) {
        String fullDescription = StringUtils.hasText(this.getFullDescription()) ? this.getFullDescription() : this.getFullDescription2();
        if (StringUtils.hasText(fullDescription)) {
            this.filterDescription(fullDescription, bookCommentList, "description");
        }
    }

    private void setUserToc(List<BookComment> bookCommentList) {
        String toc = this.getSubInfo().getToc();
        if (StringUtils.hasText(toc)) {
            toc = toc.replaceAll("<(/)?([pP]*)(\\s[pP]*=[^>]*)?(\\s)*(/)?>", "");
            bookCommentList.add(BookComment.create(toc, "toc"));
        }
    }

    private void setUserPhrase(List<BookComment> bookCommentList) {
        Phrase phrase;
        if (!ObjectUtils.isEmpty(this.getSubInfo().getPhraseList())) {
            int phraseLen = this.getSubInfo().getPhraseList().size();
            //j==0일 경우 이미지 확률이 높음
            for (int j = 1; j < phraseLen; j++) {

                phrase = this.getSubInfo().getPhraseList().get(j);
                String filteredPhrase = BookRecommendUtil.filterStr(phrase.getPhrase());
                if (!StringUtils.hasText(filteredPhrase)) {
                    continue;
                }
                String[] phraseArr = filteredPhrase.split("\\.");
                StringBuilder phraseContent = new StringBuilder();
                int phraseArrLen = phraseArr.length < RcmdConst.paragraphSlide ? phraseArr.length : RcmdConst.paragraphSlide;
                for (int k = 0; k < phraseArrLen; k++) {
                    phraseContent.append(phraseArr[k])
                            .append(". ");
                }
                bookCommentList.add(BookComment.create(phraseContent.toString(), "phrase"));
            }
        }
    }


    private void setUserGptComment(GptService gptService, List<BookComment> bookCommentList) {
        //gpt 책 속 명언 추출
        String bookName = this.getTitle() + " 책 속 명언 3개만 찾아서 '{quote1 : '명언1', quote2:'명언2'}'형태로 출력해줘. 없으면 주제나 메세지에 대해 출력해줘.";

        GptResponse gptResponse = gptService.chatGpt(bookName);

        String input = gptResponse != null && gptResponse.getChoices().size() > 0 ? gptResponse.getChoices().get(0).getMessage().getContent() : "";

        // 문자열을 Map<String, String>으로 변환
        input = input.replaceAll("[{}]", "");
        String[] pairs = input.split(", (?=quote)" ); // 정규 표현식 수정하여 올바르게 분리

        StringBuilder gptQuoteSb = new StringBuilder();
        for (String pair : pairs) {
            String[] entry = pair.split(": ", 2); // 두 번째 요소부터 모든 문자열 포함
            if (entry.length == 2) {
                String gptQuote = entry[1].replaceAll("\"", "").trim();
                gptQuoteSb.append(gptQuote);
                gptQuoteSb.append("<br>");
            }
        }
        if (gptQuoteSb.length() != 0) {
            bookCommentList.add(BookComment.create(gptQuoteSb.toString(), "quote"));
        }
    }
    private void filterDescription(String description, List<BookComment> recommendCommentList, String type) {
        //설명 첫번재 문단은 삭제
        String descriptionParagraph = descriptionParagraphFunc(description);

        //모든 태그 제거
        String filteredDescriptionParagraph = BookRecommendUtil.filterStr(descriptionParagraph);
        String[] descriptionArr = filteredDescriptionParagraph.split("\\.");
        List<String> descriptionList = Arrays.asList(descriptionArr);
        //글자가 많을 경우 2개 또는 ... 처리
        int introduceSlide = descriptionList.size() >= 3 && filteredDescriptionParagraph.length() > RcmdConst.strMaxCount * 2 ? RcmdConst.introduceSlide : 1;
        int slide = 0;
        for (int i = 0; i < introduceSlide; i++) {
            StringBuilder content = new StringBuilder();
            for (int j = 0; content.length() < RcmdConst.strMaxCount; j++) {
                if (descriptionList.size() <= slide ) {
                    break;
                }
                //int startIdx = descriptionList.size() >= 3 ? slide + 1 : slide;


                content.append(descriptionList.get(slide))
                        .append(". ");
                slide++;
            }
            String contentValue = content.toString();
            if (StringUtils.hasText(content)) {
                recommendCommentList.add(BookComment.create(contentValue, type));
            }
        }

    }

    private String descriptionParagraphFunc(String originParagraph) {

        if (StringUtils.hasText(originParagraph)) {
            if (originParagraph.toLowerCase().contains("<br>")) {
                List<String> paragraphList = Arrays.stream(originParagraph.toLowerCase().split("<br>"))
                        .filter(p -> StringUtils.hasText(p) && p.length() > 10)
                        .collect(Collectors.toList());
                //paragraphList.stream().collect(Collectors.joining(".")).toString();
                if (originParagraph.length() < 100 & paragraphList.size() < 4) {
                    return originParagraph;
                }else {
                    paragraphList.remove(0);
                    return paragraphList.stream().collect(Collectors.joining("<BR>")).toString();
                }
            }
        }
        return originParagraph;
    }
}