package fr.srosoft.wineyard.core.model.beans;

import java.io.Serializable;

import fr.srosoft.wineyard.core.model.entities.Cuvee;

@SuppressWarnings("serial")
public class CuveeSummary implements Serializable{

	private Cuvee cuvee;
	private StockSummary stockSummary = new StockSummary();
	private BookingSummary bookingSummary = new BookingSummary();
	
	public Cuvee getCuvee() {
		return cuvee;
	}
	public void setCuvee(Cuvee cuvee) {
		this.cuvee = cuvee;
	}
	public StockSummary getStockSummary() {
		return stockSummary;
	}
	public void setStockSummary(StockSummary stockSummary) {
		this.stockSummary = stockSummary;
	}
	public BookingSummary getBookingSummary() {
		return bookingSummary;
	}
	public void setBookingSummary(BookingSummary bookingSummary) {
		this.bookingSummary = bookingSummary;
	}
}
