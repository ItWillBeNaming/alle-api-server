package com.alle.api.domain.board.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardResponse {

    private Long boardId;
    private String title;
    private String content;
    private int viewCount;
    private int likeCount;


}
