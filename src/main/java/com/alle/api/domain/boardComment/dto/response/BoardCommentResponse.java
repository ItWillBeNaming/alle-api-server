package com.alle.api.domain.boardComment.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardCommentResponse {

    private Long id;
    private Long parentCommentId;
    private List<Long> childrenCommentsIds;
    private String content;

}
