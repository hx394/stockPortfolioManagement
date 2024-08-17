package model;

import org.w3c.dom.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * this is a mock model for model interface that implements all methods with log appending actions.
 * It returns unique code with appropriate methods.
 */
public class MockModel implements Model {
  private final StringBuilder log;
  private final int uniqueCode;

  /**
   * constructor of mock model, using string builder and unique code.
   *
   * @param log        string builder
   * @param uniqueCode unique code
   */
  public MockModel(StringBuilder log, int uniqueCode) {
    this.log = log;
    this.uniqueCode = uniqueCode;
  }

  /**
   * mocks addPortfolio method.
   *
   * @return new Portfolio
   */
  @Override
  public Portfolio addPortfolio() {
    log.append("addPortfolio called\n");
    return new Portfolio();
  }

  /**
   * mocks add super portfolio method.
   *
   * @return new SuperPortfolio
   */
  @Override
  public SuperPortfolio addSuperPortfolio() {
    log.append("addSuperPortfolio called\n");
    return new SuperPortfolio();
  }

  /**
   * mocks show all portfolio method.
   *
   * @return info
   */
  @Override
  public String showAllPortfolios() {
    log.append("showAllPortfolios called\n");
    return "Mock portfolios list " + uniqueCode; // 在返回字符串中包含uniqueCode
  }

  /**
   * mocks sell portfolio method.
   *
   * @return info
   */
  @Override
  public String sellPortfolio(int number) {
    log.append("sellPortfolio called with number: ").append(number).append("\n");
    return "Portfolio " + number + " sold " + uniqueCode; // 在返回消息中包含uniqueCode
  }

  /**
   * mocks get specific portfolio info method.
   *
   * @return info
   */
  @Override
  public String getSpecificPortfolioInfo(int number) {
    log.append("getSpecificPortfolioInfo called with number: ").append(number).append("\n");
    return "Mock portfolio info for " + number + " " + uniqueCode; // 在返回信息中包含uniqueCode
  }

  /**
   * mocks update method.
   */
  @Override
  public void update() {
    log.append("update called\n");
  }

  /**
   * mocks load portfolio method.
   */
  @Override
  public void loadPortfolio(Document d, String selection) {
    log.append("loadPortfolio called with selection: ").append(selection).append("\n");
  }

  /**
   * mocks get value at certain date method.
   *
   * @return unique code
   */
  @Override
  public double getValueAtCertainDate(int p, LocalDate date) {
    log.append("getValueAtCertainDate called with portfolioNum: ").append(p).append(" and date: ")
            .append(date).append("\n");
    return uniqueCode;
  }

  /**
   * mocks save portfolio method.
   *
   * @return false
   */
  @Override
  public boolean save(int p) {
    log.append("save called with portfolioNum: ").append(p).append("\n");

    return false;
  }

  /**
   * mocks get size method.
   *
   * @return unique code
   */
  @Override
  public int getSize() {
    log.append("getSize called\n");
    return uniqueCode;
  }

  /**
   * mocks add stock to super portfolio method.
   */
  @Override
  public void superPortfolioAddStock(int number, String symbol, long shares, LocalDate time) {
    log.append("superPortfolioAddStock called with number: ").append(number)
            .append(", symbol: ").append(symbol)
            .append(", shares: ").append(shares)
            .append(", time: ").append(time).append("\n");
  }

  /**
   * mocks sell stock of super portfolio method.
   */
  @Override
  public void superPortfolioSellStock(int number, String symbol, long shares, LocalDate time) {
    log.append("superPortfolioSellStock called with number: ").append(number)
            .append(", symbol: ").append(symbol)
            .append(", shares: ").append(shares)
            .append(", time: ").append(time).append("\n");
  }

  /**
   * mocks cost basis of super portfolio method.
   *
   * @return unique code
   */
  @Override
  public double superPortfolioCostBasis(int number, LocalDate time) {
    log.append("superPortfolioCostBasis called with number: ").append(number)
            .append(", time: ").append(time).append("\n");
    return uniqueCode;
  }

  /**
   * mocks value of super portfolio method.
   *
   * @return unique code
   */
  @Override
  public double superPortfolioTimeBoundaryValue(int number, LocalDate time) {
    log.append("superPortfolioTimeBoundaryValue called with number: ").append(number)
            .append(", time: ").append(time).append("\n");
    return uniqueCode;
  }

  /**
   * mocks stock gain or lose on given day method.
   *
   * @return info
   */
  @Override
  public String stockGainOrLoseOnGivenDay(String symbol, LocalDate time) {
    log.append("stockGainOrLoseOnGivenDay called with symbol: ").append(symbol)
            .append(", time: ").append(time).append("\n");
    return "Mock gain or lose info " + uniqueCode; // 返回包含uniqueCode的模拟字符串
  }

  /**
   * mocks stock gain or lose over period method.
   *
   * @return info
   */
  @Override
  public String stockGainOrLoseOverPeriodOfTime(String symbol, LocalDate start, LocalDate end) {
    log.append("stockGainOrLoseOverPeriodOfTime called with symbol: ").append(symbol)
            .append(", start: ").append(start)
            .append(", end: ").append(end).append("\n");
    return "Mock period gain or lose info " + uniqueCode; // 返回包含uniqueCode的模拟字符串
  }

  /**
   * mocks stock x day moving average method.
   *
   * @return unique code
   */
  @Override
  public double stockXDayMovingAverage(String symbol, LocalDate time, int x) {
    log.append("stockXDayMovingAverage called with symbol: ").append(symbol)
            .append(", time: ").append(time)
            .append(", x: ").append(x).append("\n");
    return uniqueCode;
  }

  /**
   * mocks stock crossover method.
   *
   * @return info
   */
  @Override
  public String stockCrossovers(String symbol, LocalDate start, LocalDate end) {
    log.append("stockCrossovers called with symbol: ").append(symbol)
            .append(", start: ").append(start)
            .append(", end: ").append(end).append("\n");
    return "Mock crossovers info " + uniqueCode; // 返回包含uniqueCode的模拟字符串
  }

  /**
   * mocks stock moving crossover method.
   *
   * @return info
   */
  @Override
  public String stockMovingCrossovers(String symbol, LocalDate start, LocalDate end, int x, int y) {
    log.append("stockMovingCrossovers called with symbol: ").append(symbol)
            .append(", start: ").append(start)
            .append(", end: ").append(end)
            .append(", x: ").append(x)
            .append(", y: ").append(y).append("\n");
    return "Mock moving crossovers info " + uniqueCode; // 返回包含uniqueCode的模拟字符串
  }

  /**
   * mocks stock generate chart method.
   *
   * @return info
   */
  @Override
  public String generateChartForStock(String symbol, LocalDate startDate, LocalDate endDate) {
    log.append("generateChartForStock called with symbol: ").append(symbol)
            .append(", startDate: ").append(startDate)
            .append(", endDate: ").append(endDate).append("\n");
    return "Mock chart for stock " + symbol + " " + uniqueCode;
  }

  /**
   * mocks portfolio generate chart method.
   *
   * @return info
   */
  @Override
  public String generateChartForPortfolio(int portfolioNum, LocalDate startDate,
                                          LocalDate endDate) {
    log.append("generateChartForPortfolio called with portfolioNum: ").append(portfolioNum)
            .append(", startDate: ").append(startDate)
            .append(", endDate: ").append(endDate).append("\n");
    return "Mock chart for portfolio " + portfolioNum + " " + uniqueCode;
  }

  /**
   * mocks get size of simple portfolio method.
   *
   * @return 0
   */
  @Override
  public int getSizeOfSimplePortfolio() {
    return 0;
  }

  /**
   * mocks get size of super portfolio method.
   *
   * @return 0
   */
  @Override
  public int getSizeOfSuperPortfolio() {
    return 0;
  }

  /**
   * mocks weight investment on a list of companies on specific day.
   *
   * @param portfolioNum portfolio number
   * @param map          stock percent mapping
   * @param date         investment date
   * @param money        money amount to invest
   * @throws IllegalArgumentException if illegal argument happens
   */
  @Override
  public void weightInvestment(int portfolioNum, HashMap<String, Double> map, LocalDate date,
                               double money) throws IllegalArgumentException {
    log.append(portfolioNum);
    for (Map.Entry<String, Double> entry : map.entrySet()) {
      log.append(entry.getKey()).append(entry.getValue());
    }
    log.append(date).append(money);
  }

  /**
   * mocks weight investment on a list of companies on specific period with frequency.
   *
   * @param portfolioNum portfolio number
   * @param map          mapping of stock to percent
   * @param start        start date
   * @param end          end date
   * @param money        money amount
   * @param period       period frequency
   * @throws IllegalArgumentException if illegal argument happens
   */
  @Override
  public void weightInvestmentStartToFinish(int portfolioNum, HashMap<String, Double> map,
                                            LocalDate start, LocalDate end,
                                            double money, int period)
          throws IllegalArgumentException {
    log.append(portfolioNum);
    for (Map.Entry<String, Double> entry : map.entrySet()) {
      log.append(entry.getKey()).append(entry.getValue());
    }
    log.append(start).append(end).append(money).append(period);
  }

  @Override
  public ArrayList<String> getMyInvestments() {
    return null;
  }

  @Override
  public ArrayList<String> getMySuperInvestments() {
    return null;
  }

  /**
   * Get string out of log method.
   *
   * @return log.toString()
   */
  public String getLog() {
    return log.toString();
  }

}
