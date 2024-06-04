package com.alle.api.domain.board.dto.request;

import com.alle.api.global.domain.AbstractTimeStamp;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class BoardWriteReq extends AbstractTimeStamp {

    private String title;

    private String content;

    private String wirter;





}
