package com.hck.exchange.supertraders.service;

import com.hck.exchange.supertraders.dto.ShareDTO;
import com.hck.exchange.supertraders.dto.ShareUpdateDTO;
import com.hck.exchange.supertraders.entity.ShareUpdate;
import com.hck.exchange.supertraders.repository.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Created by @hck
 */

@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ComponentScan(value = "com.hck.exchange.supertraders.configuration")
public class TradeServiceTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ShareRepository shareRepository;
    @Autowired
    private TradeRepository tradeRepository;
    @Autowired
    private ShareUpdateRepository shareUpdateRepository;
    @Autowired
    private StockPortfolioRepository stockPortfolioRepository;
    @Autowired
    private ModelMapper modelMapper;

    private TradeService tradeService;

    @BeforeEach
    public void setUp() {
        tradeService = new TradeService(userRepository,
                shareRepository,
                tradeRepository,
                shareUpdateRepository,
                stockPortfolioRepository,
                modelMapper);
    }

    @Test
    @SneakyThrows
    void registerShareTest() {
        ShareDTO shareDTO = ShareDTO.builder()
                .symbol("TST")
                .rate(new BigDecimal(15.156))
                .isRegistered(Boolean.TRUE)
                .build();
        tradeService.registerShare(shareDTO);
        assertThat(shareRepository.findBySymbol("TST"));
    }

    @Test
    @SneakyThrows
    void updateShareRateTest() {
        ShareDTO shareDTO = ShareDTO.builder()
                .symbol("TST")
                .rate(new BigDecimal(15.156))
                .isRegistered(Boolean.TRUE)
                .build();
        tradeService.registerShare(shareDTO);

        ShareUpdateDTO shareUpdateDTO = ShareUpdateDTO.builder()
                .symbol("TST")
                .newRate(new BigDecimal(20.00))
                .build();

        tradeService.updateShareRate(shareUpdateDTO);
        assertThat(shareRepository.findBySymbol("TST").isPresent() ?
                shareRepository.findBySymbol("TST").get().getShareUpdate() : Optional.ofNullable(null));
    }


    @Test
    @SneakyThrows
    void setNewShareRateTest() {
        ShareDTO shareDTO = ShareDTO.builder()
                .symbol("TST")
                .rate(new BigDecimal(15.156))
                .isRegistered(Boolean.TRUE)
                .build();
        tradeService.registerShare(shareDTO);
        shareRepository.flush();

        ShareUpdateDTO shareUpdateDTO = ShareUpdateDTO.builder()
                .symbol("TST")
                .newRate(new BigDecimal(20.00))
                .build();

        ShareUpdate shareUpdate = tradeService.updateShareRate(shareUpdateDTO);
        shareUpdateRepository.flush();

        BigDecimal newRate = shareUpdate.getNewRate().setScale(2, RoundingMode.HALF_UP);
        tradeService.setNewShareRate(shareUpdate);
        assertThat(shareRepository.findBySymbol("TST").get().getRate().equals(newRate));

    }


}
