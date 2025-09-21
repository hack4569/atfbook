package com.shsb.atfbook.application.recommend;

import com.shsb.atfbook.domain.aladin.AladinItemListRequest;
import com.shsb.atfbook.application.category.CategoryService;
import com.shsb.atfbook.domain.category.Category;
import com.shsb.atfbook.domain.history.History;
import com.shsb.atfbook.domain.member.Member;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RecommendRequest {
    private Member member;
    //private CategoryDto categoryDto;
    private int slideN = 1;
    private List<History> histories;
    private HashSet<Integer> cids;
    private String queryType;
    private int startIdx;

    public int getSlideN() {
        return slideN == 0 ? 1 : slideN;
    }

    public RecommendRequest create(CategoryService categoryService) {
        AladinItemListRequest aladinItemListRequest = new AladinItemListRequest();
        List<Category> categories = categoryService.findAcceptedCategories();
        this.cids = categories.stream().map(Category::getCid).collect(Collectors.toCollection(HashSet::new));
        return this;
    }
}
