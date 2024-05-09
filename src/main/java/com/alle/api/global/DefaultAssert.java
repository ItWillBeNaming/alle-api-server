package com.alle.api.global;

import com.alle.api.global.exception.*;
import org.springframework.util.Assert;

import java.util.Optional;

public class DefaultAssert extends Assert{


    //o
    public static void isOptionalPresent(Optional<?> value){
        if(!value.isPresent()){
            throw new AlleException(ExceptionCode.INVALID_PARAMETER);
        }
    }



}

