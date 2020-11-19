package com.acme.statusmgr.beans;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class IllegalParameterException extends RuntimeException {
    public IllegalParameterException(String reason) {
        super(reason);
    }
}

