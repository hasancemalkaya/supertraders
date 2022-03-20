package com.hck.exchange.supertraders.controller;

import com.hck.exchange.supertraders.dto.*;
import com.hck.exchange.supertraders.exception.TradeException;
import com.hck.exchange.supertraders.service.TradeService;
import com.hck.exchange.supertraders.util.RestResponseUtil;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by @hck
 */

@RestController
@RequestMapping("/supertraders")
@AllArgsConstructor
public class TradeRestController {

    private TradeService tradeService;

    @PostMapping("/addUser")
    public RestResponse addUser(@RequestBody @Valid UserDTO userDTO) {
        try {
            return RestResponseUtil.success(tradeService.addUser(userDTO)).build();
        } catch (TradeException e) {
            return RestResponseUtil.error(e.getMessage(), e.getPayload()).build();
        }
    }

    @PostMapping("/registerShare")
    public RestResponse registerShare(@RequestBody @Valid ShareDTO shareDTO) {
        try {
            tradeService.registerShare(shareDTO);
            return RestResponseUtil.success().build();
        } catch (TradeException e) {
            return RestResponseUtil.error(e.getMessage(), e.getPayload()).build();
        }
    }

    @PostMapping("/updateShareRate")
    public RestResponse updateShareRate(@RequestBody @Valid ShareUpdateDTO shareUpdateDTO) {
        try {
            tradeService.updateShareRate(shareUpdateDTO);
            return RestResponseUtil.success().build();
        } catch (TradeException e) {
            return RestResponseUtil.error(e.getMessage(), e.getPayload()).build();
        }
    }

    @PostMapping("/buy")
    public RestResponse buy(@RequestBody @Valid TradeDTO tradeDTO) {
        try {
            tradeService.buy(tradeDTO);
            return RestResponseUtil.success().build();
        } catch (TradeException e) {
            return RestResponseUtil.error(e.getMessage(), e.getPayload()).build();
        }
    }

    @PostMapping("/sell")
    public RestResponse sell(@RequestBody @Valid TradeDTO tradeDTO) {
        try {
            tradeService.sell(tradeDTO);
            return RestResponseUtil.success().build();
        } catch (TradeException e) {
            return RestResponseUtil.error(e.getMessage(), e.getPayload()).build();
        }
    }

    @PostMapping("/bulk/trade")
    public RestResponse sell(@RequestBody List<TradeDTO> tradeDTO) {

        try {
            tradeService.bulkTrade(tradeDTO);
            return RestResponseUtil.success().build();
        } catch (TradeException e) {
            return RestResponseUtil.error(e.getMessage(), e.getPayload()).build();
        }
    }

}
