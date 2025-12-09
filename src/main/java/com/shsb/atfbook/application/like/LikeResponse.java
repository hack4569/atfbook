package com.shsb.atfbook.application.like;

import com.shsb.atfbook.application.common.AtfBookResponse;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class LikeResponse extends AtfBookResponse {
    public LikeResponse(Boolean resultCd, String message) {
        this.resultCd = resultCd;
        this.message = message;
    }
    public LikeResponse(Boolean resultCd) {
        this.resultCd = resultCd;
    }
}
