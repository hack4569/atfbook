package com.shsb.atfbook.application.dto;

import com.shsb.atfbook.domain.gpt.GptMessage;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GptParamDto {
    private boolean store;
    private List<GptMessage> gptMessageList;
}
