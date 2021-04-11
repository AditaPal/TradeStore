package com.Trade.Strore.Service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.OptionalInt;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Trade.Strore.Dao.ITradeDao;
import com.Trade.Strore.Pojo.Trade;

@Service
public class TradeServiceImpl implements ITradeService {

	@Autowired
	ITradeDao tradeDao;

	@Override
	public String save(Trade trade) {
		String msg = "Saved Successfully";
		tradeDao.save(trade);
		return msg;
	}

	@Override
	public int getMaxVersion(String tradeId, String counterPartyId) {
		OptionalInt version = getTradesById(tradeId).stream()
				.filter(trade -> counterPartyId.equalsIgnoreCase(trade.getCounterPartyId())).mapToInt(Trade::getVersion)
				.max();

		if (version.isPresent()) {
			return version.getAsInt();
		} else {
			return 0;

		}
	}

	@Override
	public List<Trade> getTradesById(String tradeId) {
		List<Trade> list = tradeDao.findByTradeId(tradeId);
		return list;
	}

	private List<Trade> getTrades() {

		Iterable<Trade> list = tradeDao.findAll();
		List<Trade> tradeList = new ArrayList<>();
		list.forEach(trade -> tradeList.add(trade));

		return tradeList;
	}

	private String updateTrade(List<Trade> list) {
		String msg = "Saved Successfully";
		try {
			tradeDao.saveAll(list);

		} catch (Exception e) {
			msg = "Error Occured while Saving" + e.getMessage();
		}
		return msg;

	}

	@Override
	public String autoExpireTrades() {
		List<Trade> tradeList = getTrades();

		try {
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String date = sdf.format(new Date());
			Date currdate = sdf.parse(date);

			List<Trade> filterList = tradeList.stream()
					.filter(trade -> trade.getMaturedate().before(currdate) && !trade.isExpired())
					.collect(Collectors.toList());
			filterList.forEach(trade -> trade.setExpired(true));
			updateTrade(filterList);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public List<Trade> getAllTrades() {
		// TODO Auto-generated method stub
		return null;
	}

}
