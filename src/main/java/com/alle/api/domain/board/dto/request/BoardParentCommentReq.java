package com.alle.api.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BoardParentCommentReq {

    @NotNull(message = "공백으로 댓글을 달 수 없습니다.")
    String comment;

}
