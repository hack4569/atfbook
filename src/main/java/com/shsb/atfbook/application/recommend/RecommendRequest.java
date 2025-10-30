package com.shsb.atfbook.application.recommend;

import com.shsb.atfbook.domain.aladin.AladinRequest;
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
    private List<History> histories;
    private HashSet<Integer> cids;
    private String queryType = "bestseller";
    private int start;

    public static RecommendRequest create(CategoryService categoryService) {
        RecommendRequest request = new RecommendRequest();
        List<Category> categories = categoryService.findAcceptedCategories();
        request.setCids(categories.stream().map(Category::getCid).collect(Collectors.toCollection(HashSet::new)));
        return request;
    }

    public void nextPage() {
        start+= 1;
    }
}
