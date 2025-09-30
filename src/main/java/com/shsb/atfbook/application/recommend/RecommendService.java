package com.shsb.atfbook.application.recommend;

import com.shsb.atfbook.domain.aladin.AladinBook;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.List;

@Service
public class RecommendService {

    public void filter(List<AladinBook> newAladinBooks) {
        if (ObjectUtils.isEmpty(newAladinBooks)) return;


    }
}
