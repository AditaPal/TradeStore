package com.Trade.Strore.Dao;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Trade.Strore.Pojo.Trade;

@Repository
public interface ITradeDao extends CrudRepository<Trade, Long> {

	List<Trade> getTradesById(String tradeId, String counterPartyId);

	@Query(value = "SELECT * FROM TRADE t WHERE t.tradeId = ?1", nativeQuery = true)
	List<Trade> findByTradeId(String tradeId);

	List<Trade> findAll();

}
