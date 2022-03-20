package com.hck.exchange.supertraders.service;

import com.hck.exchange.supertraders.dto.*;
import com.hck.exchange.supertraders.entity.*;
import com.hck.exchange.supertraders.exception.TradeException;
import com.hck.exchange.supertraders.exception.TradeExceptions;
import com.hck.exchange.supertraders.repository.*;
import com.hck.exchange.supertraders.type.TradeType;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by @hck
 */

@Log4j2
@Service
@Transactional
@AllArgsConstructor
public class TradeService {

    private UserRepository userRepository;
    private ShareRepository shareRepository;
    private TradeRepository tradeRepository;
    private ShareUpdateRepository shareUpdateRepository;
    private StockPortfolioRepository stockPortfolioRepository;
    private ModelMapper modelMapper;

    public void registerShare(ShareDTO shareDTO) throws TradeException {
        String symbol = shareDTO.getSymbol().trim();
        if (symbol.length() != 3) {
            log.debug("Symbol length is not proper!");
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_SHARE, "Symbol length is not proper!");
        }
        if (shareRepository.findBySymbol(symbol.toUpperCase()).isPresent()) {
            log.debug("Symbol already exist! {}", symbol);
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_SHARE, "Symbol already exist!");
        }
        Share share = shareRepository.save(convertToEntity(shareDTO));
        log.info("new share added: {}", share);
    }

    public ShareUpdate updateShareRate(ShareUpdateDTO shareUpdateDTO) throws TradeException {
        String symbol = shareUpdateDTO.getSymbol().trim().toUpperCase();

        Optional<Share> optional = shareRepository.findBySymbol(symbol);
        if (optional.isEmpty()) {
            log.debug("Share don't exist, with given symbol! {}", symbol);
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_SHARE, "Share don't exist, with given symbol!");
        }

        Share share = optional.get();
        Optional<ShareUpdate> shareUpdateOptional = share.getShareUpdate();
        ShareUpdate shareUpdate;
        if (shareUpdateOptional.isPresent()) {
            shareUpdate = shareUpdateOptional.get();
            shareUpdate.setNewRate(shareUpdateDTO.getNewRate());
            shareUpdateRepository.save(shareUpdate);
            return shareUpdate;
        }

        shareUpdate = new ShareUpdate();
        shareUpdate.setShare(share);
        shareUpdate.setNewRate(shareUpdateDTO.getNewRate());
        return shareUpdateRepository.save(shareUpdate);
    }

    public void setNewShareRate(ShareUpdate shareUpdate) {
        Share share = shareUpdate.getShare();
        share.setRate(shareUpdate.getNewRate());
        share.setShareUpdate(null);
        shareRepository.saveAndFlush(share);
        shareUpdateRepository.delete(shareUpdate);
    }

    public List<ShareUpdate> getAllShareUpdates() {
        return shareUpdateRepository.findAll();
    }

    public UserDTO addUser(UserDTO userDTO) throws TradeException {
        String userId = userDTO.getUserId();
        if (userId == null || userId.trim().length() == 0) {
            log.debug("UserId is null!");
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_USER, "UserId is null!");
        }
        if (userRepository.findByUserId(userId).isPresent()) {
            log.debug("User is already exist with user id {}", userId);
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_USER, "User is already exist with given user id!");
        }

        User user = new User();
        user.setUserId(userDTO.getUserId());
        user.setName(userDTO.getName());

        PortfolioDTO portfolioDTO = userDTO.getPortfolio();
        if (portfolioDTO != null) {
            Portfolio portfolio = new Portfolio();
            portfolio.setIsRegistered(portfolioDTO.getIsRegistered());
            user.setPortfolio(portfolio);
        }
        user = userRepository.save(user);
        return convertToDTO(user);
    }

    public void buy(TradeDTO tradeDTO) throws TradeException {

        tradeValidation(tradeDTO);

        final Integer amount = tradeDTO.getAmount();

        User user = userRepository.findByUserId(tradeDTO.getUserId()).get();
        Portfolio portfolio = user.getPortfolio();

        Share share = shareRepository.findBySymbol(tradeDTO.getShare().trim().toUpperCase()).get();

        portfolio.getStockPortfolio().stream()
                .filter(s -> s.getShare().getSymbol().equals(share.getSymbol()))
                .findAny()
                .ifPresentOrElse(
                        sp -> {
                            sp.buy(amount);
                            stockPortfolioRepository.save(sp);
                        },
                        () -> {
                            StockPortfolio stockPortfolio = new StockPortfolio();
                            stockPortfolio.setPortfolio(portfolio);
                            stockPortfolio.setShare(share);
                            stockPortfolio.setAmount(amount);
                            stockPortfolioRepository.save(stockPortfolio);
                        });

        Trade trade = new Trade();
        trade.setType(TradeType.BUY);
        trade.setUser(user);
        trade.setShare(share);
        trade.setRate(share.getRate());
        trade.setAmount(amount);
        trade.setDate(tradeDTO.getDate());
        tradeRepository.save(trade);
    }

    public void sell(TradeDTO tradeDTO) throws TradeException {

        tradeValidation(tradeDTO);

        final Integer amount = tradeDTO.getAmount();

        User user = userRepository.findByUserId(tradeDTO.getUserId()).get();
        Portfolio portfolio = user.getPortfolio();

        Share share = shareRepository.findBySymbol(tradeDTO.getShare().trim().toUpperCase()).get();

        StockPortfolio stockPortfolio = portfolio.getStockPortfolio().stream()
                .filter(s -> s.getShare().getSymbol().equals(share.getSymbol()))
                .findAny()
                .orElseThrow(() -> {
                    log.debug("User doesn't have given share to sell! {}", share.getSymbol());
                    return TradeExceptions.exceptionWithPayload(TradeExceptions.SHARE_NOT_FOUND, "User doesn't have given share to sell");
                });

        if (stockPortfolio.getAmount() < amount) {
            log.debug("User doesn't have enough share to sell!");
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.SHARE_NOT_ENOUGH, "User doesn't have enough share to sell");
        }

        stockPortfolio.sell(amount);
        if (stockPortfolio.getAmount() == 0) {
            portfolio.removeStockPortfolio(stockPortfolio);
            stockPortfolioRepository.delete(stockPortfolio);
        } else {
            stockPortfolioRepository.save(stockPortfolio);
        }

        Trade trade = new Trade();
        trade.setType(TradeType.SELL);
        trade.setUser(user);
        trade.setShare(share);
        trade.setRate(share.getRate());
        trade.setAmount(amount);
        trade.setDate(tradeDTO.getDate());
        tradeRepository.save(trade);
    }

    public void bulkTrade(List<TradeDTO> tradeDTOs) throws TradeException {

        List<TradeDTO> buyTradeDTOs = new ArrayList<>();
        List<TradeDTO> sellTradeDTOs = new ArrayList<>();

        for (TradeDTO tradeDTO : tradeDTOs) {
            if (tradeDTO.getType().equals(TradeType.BUY)) {
                buyTradeDTOs.add(tradeDTO);
            } else if (tradeDTO.getType().equals(TradeType.SELL)) {
                sellTradeDTOs.add(tradeDTO);
            }
        }
        if (!buyTradeDTOs.isEmpty()) {
            bulkBuy(buyTradeDTOs);
        }
        if (!sellTradeDTOs.isEmpty()) {
            bulkSell(sellTradeDTOs);
        }
    }

    public void bulkBuy(List<TradeDTO> tradeDTOs) {

        Map<String, List<TradeDTO>> groups = groupByUser(tradeDTOs);
        groups.entrySet().forEach(s -> {
            try {
                bulkBuyForUser(s.getValue());
            } catch (TradeException e) {
                log.error("Trade error", e);
            }
        });
    }

    public void bulkSell(List<TradeDTO> tradeDTOs) {

        Map<String, List<TradeDTO>> groups = groupByUser(tradeDTOs);
        groups.entrySet().forEach(s -> {
            try {
                bulkSellForUser(s.getValue());
            } catch (TradeException e) {
                log.error("Trade error", e);
            }
        });
    }

    private Map<String, List<TradeDTO>> groupByUser(List<TradeDTO> tradeDTOs) {
        return tradeDTOs.stream()
                .collect(Collectors
                        .groupingBy(TradeDTO::getUserId));
    }

    private void bulkBuyForUser(List<TradeDTO> tradeDTOs) throws TradeException {
        userValidation(tradeDTOs.get(0));

        User user = userRepository.findByUserId(tradeDTOs.get(0).getUserId()).get();
        Portfolio portfolio = user.getPortfolio();

        List<Trade> tradeList = new ArrayList<>();
        for (TradeDTO tradeDTO : tradeDTOs) {
            try {
                shareValidation(tradeDTO);
                final Integer amount = tradeDTO.getAmount();
                Share share = shareRepository.findBySymbol(tradeDTO.getShare().trim().toUpperCase()).get();
                portfolio.getStockPortfolio().stream()
                        .filter(s -> s.getShare().getSymbol().equals(share.getSymbol()))
                        .findAny()
                        .ifPresentOrElse(
                                sp -> {
                                    sp.buy(amount);
                                    stockPortfolioRepository.save(sp);
                                },
                                () -> {
                                    StockPortfolio stockPortfolio = new StockPortfolio();
                                    stockPortfolio.setPortfolio(portfolio);
                                    stockPortfolio.setShare(share);
                                    stockPortfolio.setAmount(amount);
                                    stockPortfolioRepository.save(stockPortfolio);
                                });

                Trade trade = new Trade();
                trade.setType(TradeType.BUY);
                trade.setUser(user);
                trade.setShare(share);
                trade.setRate(share.getRate());
                trade.setAmount(amount);
                trade.setDate(tradeDTO.getDate());
                tradeList.add(trade);
            } catch (TradeException e) {
                log.error("Trade error", e);
            }
        }
        tradeRepository.saveAll(tradeList);
    }


    private void bulkSellForUser(List<TradeDTO> tradeDTOs) throws TradeException {
        userValidation(tradeDTOs.get(0));

        User user = userRepository.findByUserId(tradeDTOs.get(0).getUserId()).get();
        Portfolio portfolio = user.getPortfolio();

        List<Trade> tradeList = new ArrayList<>();
        for (TradeDTO tradeDTO : tradeDTOs) {
            try {
                shareValidation(tradeDTO);
                final Integer amount = tradeDTO.getAmount();

                Share share = shareRepository.findBySymbol(tradeDTO.getShare().trim().toUpperCase()).get();

                StockPortfolio stockPortfolio = portfolio.getStockPortfolio().stream()
                        .filter(s -> s.getShare().getSymbol().equals(share.getSymbol()))
                        .findAny()
                        .orElseThrow(() -> {
                            log.debug("User doesn't have given share to sell! {}", share.getSymbol());
                            return TradeExceptions.exceptionWithPayload(TradeExceptions.SHARE_NOT_FOUND, "User doesn't have given share to sell");
                        });

                if (stockPortfolio.getAmount() < amount) {
                    log.debug("User doesn't have enough share to sell!");
                    throw TradeExceptions.exceptionWithPayload(TradeExceptions.SHARE_NOT_ENOUGH, "User doesn't have enough share to sell");
                }

                stockPortfolio.sell(amount);
                if (stockPortfolio.getAmount() == 0) {
                    portfolio.removeStockPortfolio(stockPortfolio);
                    stockPortfolioRepository.delete(stockPortfolio);
                } else {
                    stockPortfolioRepository.save(stockPortfolio);
                }

                Trade trade = new Trade();
                trade.setType(TradeType.SELL);
                trade.setUser(user);
                trade.setShare(share);
                trade.setRate(share.getRate());
                trade.setAmount(amount);
                trade.setDate(tradeDTO.getDate());
                tradeList.add(trade);

            } catch (TradeException e) {
                log.error("Trade error", e);
            }
        }
        tradeRepository.saveAll(tradeList);
    }


    private void tradeValidation(TradeDTO tradeDTO) throws TradeException {
        userValidation(tradeDTO);
        shareValidation(tradeDTO);
    }

    private void userValidation(TradeDTO tradeDTO) throws TradeException {
        String userId = tradeDTO.getUserId();
        if (userId == null) {
            log.debug("User is null!");
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_TRADE_USER, "User is null!");
        }
        Optional<User> optionalUser = userRepository.findByUserId(userId);
        if (optionalUser.isEmpty()) {
            log.debug("User don't exist, with given user id! {}", userId);
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_TRADE_USER, "User don't exist, with given user id!");
        }
        if (!optionalUser.get().haveRegisteredPortfolio()) {
            log.debug("User has not registered portfolio!");
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_TRADE_USER, "User has not registered portfolio!");
        }
    }

    private void shareValidation(TradeDTO tradeDTO) throws TradeException {
        String shareSymbol = tradeDTO.getShare();
        if (shareSymbol == null) {
            log.debug("Share is null!");
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_TRADE_SHARE, "Share is null!");
        }
        shareSymbol = shareSymbol.trim().toUpperCase();
        Optional<Share> optionalShare = shareRepository.findBySymbol(shareSymbol);
        if (optionalShare.isEmpty()) {
            log.debug("Share don't exist, with given symbol! {}", shareSymbol);
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_TRADE_SHARE, "Share don't exist, with given symbol!");
        }
        if (!optionalShare.get().getIsRegistered()) {
            log.debug("Share isn't registered {}", shareSymbol);
            throw TradeExceptions.exceptionWithPayload(TradeExceptions.INVALID_TRADE_SHARE, "Share isn't registered!");
        }
    }

    private Share convertToEntity(ShareDTO shareDTO) {
        return modelMapper.map(shareDTO, Share.class);
    }

    private User convertToEntity(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    private Portfolio convertToEntity(PortfolioDTO portfolioDTO) {
        return modelMapper.map(portfolioDTO, Portfolio.class);
    }

    private UserDTO convertToDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
}
