package controller;

/**
 * this is a data source api that represent the data source of stock prices.
 * supports alpha vantage api and other forms of data in csv.
 */
public interface DataSourceAPI {

  /**
   * get the data from a stock symbol.
   *
   * @param stockSymbol stock symbol
   * @return String data
   * @throws Exception if anything goes wrong
   */
  String getData(String stockSymbol) throws Exception;

  /**
   * store the data from a stock symbol.
   *
   * @param stockSymbol stock symbol
   * @return success if success
   * @throws Exception if anything goes wrong
   */
  String storeData(String stockSymbol) throws Exception;
}
