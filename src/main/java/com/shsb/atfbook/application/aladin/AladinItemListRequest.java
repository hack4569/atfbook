package com.shsb.atfbook.application.aladin;

import com.shsb.atfbook.application.category.CategoryService;
import com.shsb.atfbook.application.recommend.RecommendRequest;
import com.shsb.atfbook.domain.category.Category;
import com.shsb.atfbook.domain.history.History;
import com.shsb.atfbook.domain.recommend.RcmdConst;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class AladinItemListRequest {
    private int startIdx = 1;
    private int startN = 1;
    private int maxResults = 10;
    private int showBooksCount = RcmdConst.SHOW_BOOKS_COUNT;
    private long memberId;
    private String queryType;
    private String filterType;
    private Category category;
    private HashSet<Integer> finalCids;
    private String anchorDate;
    private List<History> histories;

    public void setStartIdx(int startIdx) {
        if (getStartN() == startIdx % RcmdConst.THREAD_END_IDX + 1) {
            this.startIdx = startIdx;
        }else {
            setStartIdx(startIdx+1);
        }
    }

    public int getStartIdx() {
        return startIdx == 0 ? 1 : startIdx;
    }

    public int getStartN() {
        return startN == 0 ? 1 : startN;
    }
}
