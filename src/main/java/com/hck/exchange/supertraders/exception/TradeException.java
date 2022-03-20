package com.hck.exchange.supertraders.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by @hck
 */
public class TradeException extends Exception{

    @Getter
    private final int exceptionCode;
    @Getter
    @Setter
    private String payload = null;

    protected TradeException() {
        super();
        this.exceptionCode = 0;
    }

    protected TradeException(int exceptionCode) {
        super();
        this.exceptionCode = exceptionCode;
    }

    protected TradeException(int exceptionCode, String message) {
        super(message);
        this.exceptionCode = exceptionCode;
    }

    protected TradeException(int exceptionCode, String message, String payload) {
        this(exceptionCode, message);
        this.payload = payload;
    }

}
