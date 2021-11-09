package fr.srosoft.wineyard.core.model.beans;

import java.util.ArrayList;
import java.util.List;

public class StockSummary {

	private List<Stock> stocks;

	public List<Stock> getStocks() {
		return stocks;
	}

	public void setStocks(List<Stock> stocks) {
		this.stocks = stocks;
	}
	
	public void addStock(Stock stock) {
		if (this.stocks == null) this.stocks = new ArrayList<>();
		this.stocks.add(stock);
	}
}
