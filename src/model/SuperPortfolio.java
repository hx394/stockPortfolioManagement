package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a flexible portfolio called super portfolio that have all the features of portfolio
 * class but can also add or sell stocks on specified days. calculate cost basis and value at
 * given date.
 */
public class SuperPortfolio extends Portfolio {

  /**
   * default constructor that calls super class constructor.
   */
  public SuperPortfolio() {
    super();
  }

  /**
   * Constructs a new super Portfolio instance by parsing portfolio data from an XML Document.
   *
   * @param d The XML Document containing the portfolio data.
   * @throws Exception if there is an error during parsing.
   */
  public SuperPortfolio(Document d) throws Exception {

    this.portfolioName = d
            .getElementsByTagName("portfolio_name").item(0).getTextContent();
    this.sold = Boolean.parseBoolean(
            d.getElementsByTagName("sold").item(0).getTextContent());
    this.value = Double.parseDouble(
            d.getElementsByTagName("value").item(0).getTextContent());
    this.initialValue = Double.parseDouble(
            d.getElementsByTagName("initial_value").item(0).getTextContent());
    this.portfolio = new ArrayList<Stock>();

    NodeList stocks = d.getElementsByTagName("stocks").item(0).getChildNodes();


    for (int i = 0; i < stocks.getLength(); i++) {
      Node n = stocks.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) n;
        portfolio.add(getStockFromElement(element));
      }
    }

  }

  /**
   * Loads a super portfolio from an XML document.
   *
   * @param d The XML document containing the portfolio data.
   * @return A new super Portfolio instance populated with the data from the document.
   * @throws Exception if there is an error during the loading process.
   */
  public static SuperPortfolio load(Document d) throws Exception {

    return new SuperPortfolio(d);
  }

  /**
   * add a stock based on symbol shares and time.
   *
   * @param symbol stock symbol
   * @param shares share number
   * @param time   time of add
   */
  public void addStock(String symbol, double shares, LocalDate time)
          throws IllegalArgumentException {
    if (companies.contains(symbol)) {
      for (Stock s : portfolio) {
        if (s.getSymbol().equals(symbol) && s.getTimestamp().isEqual(time)) {
          s.setShares(s.getShares() + shares);
          return;
        }
      }
    }
    Stock newStock = Stock.getBuilder().symbol(symbol).shares(shares).timeStamp(time).build2();
    try {
      double initialPrice = newStock.hasDataOnGivenDay(newStock.getTimestamp());
      newStock.setInitialPrice(initialPrice);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    addAndSort(newStock);

    companies.add(symbol);
  }

  /**
   * sell a stock based on symbol shares time.
   *
   * @param symbol stock
   * @param shares number of shares
   * @param time   sell time
   * @throws IllegalArgumentException if Illegal argument happens
   */
  public void sellStock(String symbol, double shares, LocalDate time)
          throws IllegalArgumentException {


    if (companies.contains(symbol)) {

      Stock newStock = Stock.getBuilder().symbol(symbol).shares(-shares).timeStamp(time).build2();

      double sellPrice = newStock.hasDataOnGivenDay(newStock.getTimestamp());

      newStock.setInitialPrice(sellPrice);
      addAndSort(newStock);


      double total = 0;
      for (Stock s : portfolio) {
        if (s.getSymbol().equals(symbol)) {
          total = total + s.getShares();
          if (total < 0) {
            portfolio.remove(newStock);
            total = total - s.getShares();
            throw new IllegalArgumentException("Cannot sell amount " + s.getShares()
                    + " more than total amount of stock "
                    + symbol + " amount:" + total + " at the time:" + s.getTimestamp());
          }
        }
      }


    } else {
      throw new IllegalArgumentException("No shares of the company to sell: " + symbol);
    }
  }

  /**
   * helper function to sort the portfolio.
   *
   * @param newStock new stock
   */
  private void addAndSort(Stock newStock) {
    portfolio.add(newStock);
    portfolio.sort(new Comparator<Stock>() {
      @Override
      public int compare(Stock s1, Stock s2) {
        if (s1.getSymbol().equals(s2.getSymbol()) && s1.getTimestamp().isEqual(s2.getTimestamp())) {
          return Double.compare(s2.getShares(), s1.getShares());
        }
        return s1.getTimestamp().compareTo(s2.getTimestamp());
      }
    });
  }

  /**
   * calculate cost basis for a portfolio.
   *
   * @param time given time
   * @return double the value of cost basis
   */
  public double costBasis(LocalDate time) {
    double total = 0;
    for (Stock s : portfolio) {
      if ((s.getTimestamp().isBefore(time) || s.getTimestamp().isEqual(time))
              && s.getShares() > 0) {
        total = total + s.getInitialPrice() * s.getShares();
      }
    }
    total = Math.round(total * 100) / 100.0;
    return total;
  }

  /**
   * get value by date.
   *
   * @param time specified date
   * @return double the value
   * @throws IllegalArgumentException if a stock price is not found among the portfolio
   */
  public double getValueByDate(LocalDate time) throws IllegalArgumentException {
    double total = 0;

    for (Stock s : portfolio) {
      if (s.getTimestamp().isBefore(time) || s.getTimestamp().isEqual(time)) {
        double price;
        try {
          price = s.getHistoricalPrice(time);
        } catch (IllegalArgumentException e) {
          throw new IllegalArgumentException("Some stocks may have already quit the market."
                  + "No data found for time " + time + " of stock " + s.getSymbol() + ".");
        }
        total = total + price * s.getShares();
      }
    }

    total = Math.round(total * 100) / 100.0;
    return total;
  }

  /**
   * weight investment on a list of companies with specific date.
   *
   * @param map   mapping of companies to percentages
   * @param date  investment date
   * @param money money amount
   * @throws IllegalArgumentException if illegal argument happens
   */
  public void weightInvestment(HashMap<String, Double> map, LocalDate date, double money)
          throws IllegalArgumentException {

    ArrayList<String> symbols = new ArrayList<>();
    ArrayList<Double> shareNums = new ArrayList<>();
    ArrayList<LocalDate> dateArray = new ArrayList<>();
    try {
      for (Map.Entry<String, Double> entry : map.entrySet()) {
        Stock newStock = Stock.getBuilder().symbol(entry.getKey())
                .shares(0).timeStamp(date).build2();
        double price = newStock.hasDataOnGivenDay(date);
        double shares = Math.round(money * entry.getValue() / price * 100) / 100.0;
        symbols.add(entry.getKey());
        shareNums.add(shares);
        dateArray.add(date);
      }
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    for (int i = 0; i < symbols.size(); i++) {
      this.addStock(symbols.get(i), shareNums.get(i), dateArray.get(i));
    }
  }

  /**
   * weight investment on a period of time with a list of companies.
   *
   * @param map    mapping of companies to percentages
   * @param start  start date
   * @param end    end date
   * @param money  money amount
   * @param period period frequency
   * @throws IllegalArgumentException if illegal argument happens
   */
  public void weightInvestmentStartToFinish(HashMap<String, Double> map, LocalDate start,
                                            LocalDate end, double money, int period)
          throws IllegalArgumentException {
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start cannot be after end");
    }
    LocalDate current = start;
    while (!current.isAfter(end)) {
      try {
        weightInvestment(map, current, money);
      } catch (IllegalArgumentException e) {
        current = current.plusDays(1);
        continue;
      }
      current = current.plusDays(period);
    }
  }
}


