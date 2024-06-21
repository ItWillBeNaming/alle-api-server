package com.alle.api.domain.board.dto.request;

import com.alle.api.global.domain.AbstractTimeStamp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BoardWriteReq {

    private String title;

    private String content;
}
