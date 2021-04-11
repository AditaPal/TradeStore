package com.Trade.Strore.Controller;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.Trade.Strore.Pojo.Trade;
import com.Trade.Strore.Service.ITradeService;

@Controller
public class TradeController {
	@Autowired
	ITradeService tradeService;

	@Scheduled(cron = "0 00 00 ? * *") // At 00:00:00am every day
	public void autoUpdateTrades() {
		tradeService.autoExpireTrades();
	}

	@RequestMapping(value = "/saveTrade", method = RequestMethod.POST)
	public void saveTrade(Trade trade) throws Exception {
		String result = null;
		try {
			int max = tradeService.getMaxVersion(trade.getTradeId(), trade.getCounterPartyId());
			DateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
			String date = sdf.format(new Date());
			Date currdate = sdf.parse(date);

			if ((max != 0 && max <= trade.getVersion() && trade.getMaturedate().after(currdate)) || max == 0) {
				result = tradeService.save(trade);
			} else {
				throw new Exception("Rejected");
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
