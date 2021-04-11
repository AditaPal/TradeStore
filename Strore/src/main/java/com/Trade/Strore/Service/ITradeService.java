package com.Trade.Strore.Service;

import java.util.List;

import com.Trade.Strore.Pojo.Trade;

public interface ITradeService {

	String save(Trade trade);

	List<Trade> getTradesById(String tradeId);

	String autoExpireTrades();

	List<Trade> getAllTrades();

	int getMaxVersion(String tradeId, String counterPartyId);

}
