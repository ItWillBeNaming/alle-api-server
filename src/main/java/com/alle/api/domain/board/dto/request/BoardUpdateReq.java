package com.alle.api.domain.board.dto.request;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardUpdateReq {

    private Long id;
    private String title;
    private String content;

}
