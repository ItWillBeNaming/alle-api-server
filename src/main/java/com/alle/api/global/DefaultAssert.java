package com.alle.api.global;

import com.alle.api.global.exception.*;
import org.springframework.util.Assert;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

public class DefaultAssert extends Assert{


    //o
    public static void isOptionalPresent(Optional<?> value){
        if(!value.isPresent()){
            throw new DefaultException(ErrorCode.INVALID_PARAMETER);
        }
    }



}

