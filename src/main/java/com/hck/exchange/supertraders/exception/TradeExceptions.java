package com.hck.exchange.supertraders.exception;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Created by @hck
 */

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class TradeExceptions extends TradeException {

    public static final TradeException INVALID_SHARE = new TradeException(601, "INVALID_SHARE");
    public static final TradeException INVALID_USER = new TradeException(602, "INVALID_USER");
    public static final TradeException INVALID_TRADE_USER = new TradeException(603, "INVALID_TRADE_USER");
    public static final TradeException INVALID_TRADE_SHARE = new TradeException(604, "INVALID_TRADE_SHARE");
    public static final TradeException SHARE_NOT_FOUND = new TradeException(605, "SHARE_NOT_FOUND");
    public static final TradeException SHARE_NOT_ENOUGH = new TradeException(606, "SHARE_NOT_ENOUGH");

    public static TradeException exceptionWithPayload(TradeException tradeException, String payload) {
        return new TradeException(tradeException.getExceptionCode(), tradeException.getMessage(), payload);
    }
}
