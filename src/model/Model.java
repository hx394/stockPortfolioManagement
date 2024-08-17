package model;

import org.w3c.dom.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The {@code Model} interface defines the core functionality of the investment portfolio model,
 * including operations to manage portfolios such as adding, showing, selling, and updating.
 */
public interface Model {

  /**
   * Adds a new portfolio to the collection of portfolios.
   *
   * @return new portfolio
   */
  Portfolio addPortfolio();

  /**
   * Adds a new super portfolio to the collection of super portfolios.
   *
   * @return new super portfolio
   */
  SuperPortfolio addSuperPortfolio();

  /**
   * Provides a string representation of all portfolios managed by the model.
   *
   * @return A formatted string containing information about all portfolios.
   */
  String showAllPortfolios();

  /**
   * Sells a specific portfolio based on its index and returns information about the sale.
   *
   * @param number The index (1-based) of the portfolio to sell.
   * @return A string message detailing the outcome of the sale.
   */
  String sellPortfolio(int number);

  /**
   * Retrieves detailed information about a specific portfolio.
   *
   * @param number The index (1-based) of the portfolio for which information is requested.
   * @return A formatted string containing the detailed information of the specified portfolio.
   */
  String getSpecificPortfolioInfo(int number);

  /**
   * Updates the information of all portfolios,
   * typically reflecting changes in stock prices or portfolio composition.
   */
  void update();

  /**
   * Loads a portfolio from an XML document.
   * This method parses the provided XML document and creates a portfolio based on its content.
   *
   * @param d         The XML document from which the portfolio is to be loaded.
   * @param selection the selected portfolio type to load
   * @throws Exception If there is an issue parsing the XML document
   *                   or the document's structure does not match the expected format.
   */
  void loadPortfolio(Document d, String selection) throws Exception;

  /**
   * Calculates the total value of a specific portfolio at a given date.
   * This method takes into account only the stocks present in the portfolio on the specified date
   * and uses their values at that date to calculate the total value.
   *
   * @param p    The portfolio num for which the value is calculated.
   * @param date The date at which the portfolio's value is to be evaluated.
   * @return The total value of the portfolio at the specified date,
   *          or -1 if the value cannot be calculated due to missing stock price data.
   */
  double getValueAtCertainDate(int p, LocalDate date);

  /**
   * Saves the specified portfolio to a persistent storage mechanism.
   * This could involve serializing the portfolio object to a file,
   * database, or other storage systems.
   *
   * @param p The portfolio num to be saved.
   * @return true if the portfolio is successfully saved, false otherwise.
   */
  boolean save(int p);

  /**
   * Retrieves the total number of portfolios managed by the model.
   *
   * @return The number of portfolios currently managed.
   */
  int getSize();

  /**
   * add a stock to the super portfolio.
   *
   * @param number portfolio number
   * @param symbol stock symbol
   * @param shares number of shares
   * @param time   buy time
   * @throws IllegalArgumentException if illegal argument happens
   */
  void superPortfolioAddStock(int number, String symbol, long shares, LocalDate time)
          throws IllegalArgumentException;

  /**
   * sell a stock of the super portfolio.
   *
   * @param number portfolio number
   * @param symbol stock symbol
   * @param shares number of shares
   * @param time   sell time
   * @throws IllegalArgumentException if illegal argument happens
   */
  void superPortfolioSellStock(int number, String symbol, long shares, LocalDate time)
          throws IllegalArgumentException;

  /**
   * calculate the cost basis for a super portfolio based on time.
   *
   * @param number portfolio number
   * @param time   time
   * @return double the cost basis
   * @throws IllegalArgumentException if illegal argument happens
   */
  double superPortfolioCostBasis(int number, LocalDate time)
          throws IllegalArgumentException;

  /**
   * present the value of a super portfolio.
   *
   * @param number portfolio number
   * @param time   time
   * @return double the value
   * @throws IllegalArgumentException from base level
   */
  double superPortfolioTimeBoundaryValue(int number, LocalDate time)
          throws IllegalArgumentException;

  /**
   * tells if a stock gain or lose in price on given day.
   *
   * @param symbol Stock symbol
   * @param time   time
   * @return String  info result
   * @throws IllegalArgumentException from base level
   */
  String stockGainOrLoseOnGivenDay(String symbol, LocalDate time) throws IllegalArgumentException;

  /**
   * tells a stock gain or lose over period of time.
   *
   * @param symbol stock symbol
   * @param start  start
   * @param end    end
   * @return String the result info
   * @throws IllegalArgumentException from base level
   */
  String stockGainOrLoseOverPeriodOfTime(String symbol, LocalDate start, LocalDate end)
          throws IllegalArgumentException;

  /**
   * calculate the x day moving average of a stock.
   *
   * @param symbol stock symbol
   * @param time   time
   * @param x      x day
   * @return double the result
   * @throws IllegalArgumentException from base level
   */
  double stockXDayMovingAverage(String symbol, LocalDate time, int x)
          throws IllegalArgumentException;

  /**
   * tells the crossovers of a stock.
   *
   * @param symbol stock symbol
   * @param start  start
   * @param end    end
   * @return String the result info
   * @throws IllegalArgumentException from base level
   */
  String stockCrossovers(String symbol, LocalDate start, LocalDate end)
          throws IllegalArgumentException;

  /**
   * tells the moving crossovers of a stock.
   *
   * @param symbol stock symbol
   * @param start  start
   * @param end    end
   * @param x      x day
   * @param y      y day
   * @return String result info
   * @throws IllegalArgumentException from base level
   */
  String stockMovingCrossovers(String symbol, LocalDate start, LocalDate end, int x, int y)
          throws IllegalArgumentException;

  /**
   * generate chart for stock.
   *
   * @param symbol    stock symbol
   * @param startDate start
   * @param endDate   end
   * @return String the chart
   * @throws IllegalArgumentException if Illegal argument happens
   */
  String generateChartForStock(String symbol, LocalDate startDate, LocalDate endDate)
          throws IllegalArgumentException;

  /**
   * generate chart for portfolio.
   *
   * @param portfolioNum portfolio num
   * @param startDate    start
   * @param endDate      end
   * @return String the chart
   * @throws IllegalArgumentException if illegal argument happens
   */
  String generateChartForPortfolio(int portfolioNum, LocalDate startDate, LocalDate endDate)
          throws IllegalArgumentException;

  /**
   * get the size of simple portfolios.
   *
   * @return size
   */
  int getSizeOfSimplePortfolio();

  /**
   * get the size of super portfolios.
   *
   * @return size
   */
  int getSizeOfSuperPortfolio();

  /**
   * weight investment on a list of companies on specific day.
   *
   * @param portfolioNum portfolio number
   * @param map          mapping from company to percent
   * @param date         investment date
   * @param money        money amount
   * @throws IllegalArgumentException if illegal argument happens
   */
  void weightInvestment(int portfolioNum, HashMap<String, Double> map, LocalDate date,
                        double money) throws IllegalArgumentException;

  /**
   * weight investment on a list of companies on a period with frequency.
   *
   * @param portfolioNum portfolio number
   * @param map          mapping from company to percent
   * @param start        start date
   * @param end          end date
   * @param money        money amount
   * @param period       frequency
   * @throws IllegalArgumentException if illegal argument happens
   */
  void weightInvestmentStartToFinish(int portfolioNum, HashMap<String, Double> map,
                                     LocalDate start, LocalDate end,
                                     double money, int period)
          throws IllegalArgumentException;

  /**
   * Retrieves a list of current investments.
   *
   * @return an {@code ArrayList<String>} containing the identifiers of all investments.
   */
  ArrayList<String> getMyInvestments();

  /**
   * Retrieves a list of super investments.
   * Super investments are considered to have higher importance
   * or benefits compared to regular investments.
   *
   * @return an {@code ArrayList<String>} containing the identifiers of all super investments.
   */
  ArrayList<String> getMySuperInvestments();
}
