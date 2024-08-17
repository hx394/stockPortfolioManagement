package model;

import org.w3c.dom.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implements the {@code Model} interface, providing a concrete way to manage investment portfolios.
 * This implementation allows for the creation, management, and querying of investment portfolios,
 * as well as updating their contents based on stock market changes.
 */
public class ModelImp implements Model {

  private final ArrayList<Portfolio> myInvestments;
  private final ArrayList<SuperPortfolio> mySuperPortfolioInvestments;

  /**
   * Constructs a new {@code ModelImp} instance, initializing an empty list of portfolios.
   * This list will be used to store and manage individual investment portfolios.
   */
  public ModelImp() {
    this.myInvestments = new ArrayList<Portfolio>();
    this.mySuperPortfolioInvestments = new ArrayList<>();
  }

  /**
   * add a new empty portfolio to the myInvestments arraylist.
   *
   * @return Portfolio the new created one
   */
  @Override
  public Portfolio addPortfolio() {
    Portfolio newP = new Portfolio();
    myInvestments.add(newP);

    return newP;
  }

  /**
   * add a new empty super portfolio to the arraylist.
   *
   * @return SuperPortfolio the new created one
   */
  @Override
  public SuperPortfolio addSuperPortfolio() {
    SuperPortfolio newP = new SuperPortfolio();
    mySuperPortfolioInvestments.add(newP);
    return newP;
  }

  /**
   * show all portfolios by their names.
   *
   * @return String the list of names of portfolios and their numbers
   */
  @Override
  public String showAllPortfolios() {
    int number = 0;
    StringBuilder info = new StringBuilder();
    info.append("simple portfolios:").append(System.lineSeparator());
    for (Portfolio myInvestment : myInvestments) {
      number++;
      info.append(number).append(" ").append(myInvestment.getPortfolioName())
              .append(System.lineSeparator());
    }
    if (myInvestments.size() == 0) {
      info.append("None").append(System.lineSeparator());
    }
    info.append("flexible portfolios:").append(System.lineSeparator());
    for (SuperPortfolio myInvestment : mySuperPortfolioInvestments) {
      number++;
      info.append(number).append(" ").append(myInvestment.getPortfolioName())
              .append(System.lineSeparator());
    }
    if (mySuperPortfolioInvestments.size() == 0) {
      info.append("None").append(System.lineSeparator());
    }
    return info.toString();
  }

  /**
   * sell a portfolio and figure out the profits.
   *
   * @param number the portfolio number
   * @return String profit or no such portfolio if not found
   */
  @Override
  public String sellPortfolio(int number) {
    if (number - 1 < myInvestments.size() && number > 0) {
      Portfolio soldPortfolio = myInvestments.remove((number - 1));
      return soldPortfolio.sellPortfolio();
    } else if (number - 1 - myInvestments.size() >= 0
            && number - 1 - myInvestments.size() < mySuperPortfolioInvestments.size()) {
      SuperPortfolio soldPortfolio = mySuperPortfolioInvestments
              .remove(number - 1 - myInvestments.size());
      soldPortfolio.sellPortfolio();
      return "success in delete";
    } else {
      return "No such portfolio.";
    }
  }

  /**
   * get specific portfolio information by given a number.
   *
   * @param number the portfolio number
   * @return String the information
   */
  @Override
  public String getSpecificPortfolioInfo(int number) {
    if (number - 1 < myInvestments.size() && number > 0) {
      return myInvestments.get(number - 1).showPortfolio();
    } else if (number - 1 - myInvestments.size() >= 0
            && number - 1 - myInvestments.size() < mySuperPortfolioInvestments.size()) {
      return mySuperPortfolioInvestments.get(number - 1 - myInvestments.size()).showPortfolio();
    } else {
      return "No such portfolio.";
    }
  }

  /**
   * update all portfolios.
   *
   * @throws IllegalArgumentException if update fails
   */
  @Override
  public void update() throws IllegalArgumentException {
    for (Portfolio p : myInvestments) {
      p.update();
    }
    for (SuperPortfolio sp : mySuperPortfolioInvestments) {
      sp.update();
    }
  }

  /**
   * load a portfolio from xml file.
   *
   * @param d the document got from xml reading
   * @throws Exception if load fails
   */
  @Override
  public void loadPortfolio(Document d, String selection) throws Exception {
    if (selection.equals("s")) {
      this.myInvestments.add(Portfolio.load(d));
    } else if (selection.equals("f")) {
      this.mySuperPortfolioInvestments.add(SuperPortfolio.load(d));
    }
  }

  /**
   * get value of a portfolio at certain date.
   *
   * @param num  the portfolio num which is integer
   * @param date the date
   * @return double the value
   * @throws IllegalArgumentException if illegal argument happens
   */
  @Override
  public double getValueAtCertainDate(int num, LocalDate date) throws IllegalArgumentException {
    Portfolio p = this.getPortfolio(num);
    if (p != null) {
      return p.getValueAtCertainDate(date);
    }
    throw new IllegalArgumentException("portfolio not found");
  }

  /**
   * save a portfolio to xml file.
   *
   * @param num portfolio num which is a integer
   * @return boolean true if success, false otherwise
   */
  @Override
  public boolean save(int num) {
    Portfolio p = this.getPortfolio(num);
    if (p != null) {
      return p.save();
    }
    return false;
  }

  /**
   * get size of current investments, see how many portfolios are there.
   *
   * @return int the size
   */
  @Override
  public int getSize() {
    return myInvestments.size() + mySuperPortfolioInvestments.size();
  }

  /**
   * get portfolio by giving a number.
   *
   * @param number portfolio number
   * @return Portfolio the corresponding portfolio
   */
  private Portfolio getPortfolio(int number) {
    // Convert from 1-based index to 0-based index for internal list access
    int index = number - 1;

    // Check if the index is out of bounds
    if (index < 0 || index >= myInvestments.size() + mySuperPortfolioInvestments.size()) {
      // Return null to indicate that the requested portfolio does not exist
      return null;
    }
    if (index < myInvestments.size()) {
      // Return the requested portfolio
      return myInvestments.get(index);
    } else if (index < myInvestments.size() + mySuperPortfolioInvestments.size()) {
      return mySuperPortfolioInvestments.get(index - myInvestments.size());
    }
    return null;
  }

  /**
   * add a stock to the super portfolio.
   *
   * @param number portfolio number
   * @param symbol stock symbol
   * @param shares number of shares
   * @param time   buy time
   * @throws IllegalArgumentException if illegal argument happens
   */
  @Override
  public void superPortfolioAddStock(int number, String symbol, long shares, LocalDate time)
          throws IllegalArgumentException {
    if (number <= myInvestments.size() || number > getSize()) {
      throw new IllegalArgumentException("This number is not a super portfolio.");
    }
    SuperPortfolio sp = (SuperPortfolio) getPortfolio(number);
    if (sp != null) {
      sp.addStock(symbol, shares, time);
      return;
    }
    throw new IllegalArgumentException("Cannot found the super portfolio.");
  }

  /**
   * sell a stock of the super portfolio.
   *
   * @param number portfolio number
   * @param symbol stock symbol
   * @param shares number of shares
   * @param time   sell time
   * @throws IllegalArgumentException if illegal argument happens
   */
  @Override
  public void superPortfolioSellStock(int number, String symbol, long shares, LocalDate time)
          throws IllegalArgumentException {
    if (number <= myInvestments.size() || number > getSize()) {
      throw new IllegalArgumentException("This number is not a super portfolio.");
    }
    SuperPortfolio sp = (SuperPortfolio) getPortfolio(number);
    if (sp != null) {
      sp.sellStock(symbol, shares, time);
      return;
    }
    throw new IllegalArgumentException("Cannot found the super portfolio.");
  }

  /**
   * calculate the cost basis for a super portfolio based on time.
   *
   * @param number portfolio number
   * @param time   time
   * @return double the cost basis
   * @throws IllegalArgumentException if illegal argument happens
   */
  @Override
  public double superPortfolioCostBasis(int number, LocalDate time)
          throws IllegalArgumentException {
    if (number <= myInvestments.size() || number > getSize()) {
      throw new IllegalArgumentException("This number is not a flexible portfolio.");
    }
    SuperPortfolio sp = (SuperPortfolio) getPortfolio(number);
    if (sp != null) {
      return sp.costBasis(time);
    }
    throw new IllegalArgumentException("Cannot found the flexible portfolio.");
  }

  /**
   * present the value of a super portfolio.
   *
   * @param number portfolio number
   * @param time   time
   * @return double the value
   * @throws IllegalArgumentException from base level
   */
  @Override
  public double superPortfolioTimeBoundaryValue(int number, LocalDate time)
          throws IllegalArgumentException {
    if (number <= myInvestments.size() || number > getSize()) {
      throw new IllegalArgumentException("This number is not a super portfolio.");
    }
    SuperPortfolio sp = (SuperPortfolio) getPortfolio(number);
    if (sp != null) {
      try {
        return sp.getValueByDate(time);
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }
    throw new IllegalArgumentException("Cannot found the super portfolio.");
  }

  /**
   * tells if a stock gain or lose in price on given day.
   *
   * @param symbol Stock symbol
   * @param time   time
   * @return String  info result
   * @throws IllegalArgumentException from base level
   */
  @Override
  public String stockGainOrLoseOnGivenDay(String symbol, LocalDate time)
          throws IllegalArgumentException {
    Stock temp = Stock.getBuilder().symbol(symbol).shares(0).build();
    return temp.gainOrLoseOnGivenDay(time);
  }

  /**
   * tells a stock gain or lose over period of time.
   *
   * @param symbol stock symbol
   * @param start  start
   * @param end    end
   * @return String the result info
   * @throws IllegalArgumentException from base level
   */
  @Override
  public String stockGainOrLoseOverPeriodOfTime(String symbol, LocalDate start, LocalDate end)
          throws IllegalArgumentException {
    Stock temp = Stock.getBuilder().symbol(symbol).shares(0).build();
    return temp.gainOrLoseOverPeriodOfTime(start, end);
  }

  /**
   * calculate the x day moving average of a stock.
   *
   * @param symbol stock symbol
   * @param time   time
   * @param x      x day
   * @return double the result
   * @throws IllegalArgumentException from base level
   */
  @Override
  public double stockXDayMovingAverage(String symbol, LocalDate time, int x)
          throws IllegalArgumentException {
    Stock temp = Stock.getBuilder().symbol(symbol).shares(0).build();
    return temp.xDayMovingAverage(time, x);
  }

  /**
   * tells the crossovers of a stock.
   *
   * @param symbol stock symbol
   * @param start  start
   * @param end    end
   * @return String the result info
   * @throws IllegalArgumentException from base level
   */
  @Override
  public String stockCrossovers(String symbol, LocalDate start, LocalDate end)
          throws IllegalArgumentException {
    Stock temp = Stock.getBuilder().symbol(symbol).shares(0).build();
    return temp.crossovers(start, end);
  }

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
  @Override
  public String stockMovingCrossovers(String symbol, LocalDate start, LocalDate end, int x, int y)
          throws IllegalArgumentException {
    Stock temp = Stock.getBuilder().symbol(symbol).shares(0).build();
    return temp.movingCrossovers(start, end, x, y);
  }

  /**
   * checks if a stock have value on given day for a stock.
   *
   * @param symbol stock symbol
   * @param time   time
   * @return double the value, -1 if not found
   * @throws IllegalArgumentException from base level
   */
  private double hasValueOnGivenDay(String symbol, LocalDate time) throws IllegalArgumentException {
    Stock temp = Stock.getBuilder().symbol(symbol).shares(0).build();
    try {
      return temp.hasDataOnGivenDay(time);
    } catch (IllegalArgumentException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * generate chart for stock.
   *
   * @param symbol    stock symbol
   * @param startDate start
   * @param endDate   end
   * @return String the chart
   * @throws IllegalArgumentException if Illegal argument happens
   */
  @Override
  public String generateChartForStock(String symbol, LocalDate startDate, LocalDate endDate)
          throws IllegalArgumentException {
    checkStartAndEnd(startDate, endDate);

    LinkedHashMap<LocalDate, Double> valueMap = new LinkedHashMap<>();
    LinkedHashMap<LocalDate, Double> valueMapMonth = new LinkedHashMap<>();
    LinkedHashMap<LocalDate, Double> valueMapYear = new LinkedHashMap<>();
    LocalDate currentDate = startDate;

    Stock temp = Stock.getBuilder().symbol(symbol).shares(1).build();
    while (!currentDate.isAfter(endDate)) {
      try {

        double value = temp.getHistoricalPrice(currentDate);

        valueMap.put(currentDate, value);

        checkEndOfMonthAndYear(valueMapMonth, valueMapYear, currentDate, value);
      } catch (IllegalArgumentException e) {
        currentDate = currentDate.plusDays(1);
        continue;
      }
      // Only advance to the next weekday
      currentDate = currentDate.plusDays(1);

    }

    // Use the size of valueMap as the totalDays to ensure we only consider trading days
    return getString(valueMap, "d")
            + getString(valueMapMonth, "m")
            + getString(valueMapYear, "y");
  }

  /**
   * get string result from value map. Helper function.
   *
   * @param valueMap the value map
   * @return String result
   * @throws IllegalArgumentException from base level
   */
  private String getString(LinkedHashMap<LocalDate, Double> valueMap, String type)
          throws IllegalArgumentException {
    long totalDays = valueMap.size();
    long interval;
    if (totalDays <= 30) {
      interval = 1;
    } else if (totalDays % 29 == 1) {
      interval = totalDays / 29;
    } else {
      interval = totalDays / 29 + 1;
    }
    if (totalDays < 5 && (type.equals("m") || type.equals("y"))) {
      return "";
    }
    return formatChart(valueMap, interval, type);
  }

  /**
   * check if start is after end.
   *
   * @param startDate start date
   * @param endDate   end date
   * @throws IllegalArgumentException if start is after end
   */
  private void checkStartAndEnd(LocalDate startDate, LocalDate endDate)
          throws IllegalArgumentException {
    if (startDate.isAfter(endDate)) {
      throw new IllegalArgumentException("Start date must be before end date.");
    }
  }

  /**
   * generate chart for portfolio.
   *
   * @param portfolioNum portfolio num
   * @param startDate    start
   * @param endDate      end
   * @return String the chart
   * @throws IllegalArgumentException if illegal argument happens
   */
  @Override
  public String generateChartForPortfolio(int portfolioNum, LocalDate startDate, LocalDate endDate)
          throws IllegalArgumentException {

    checkStartAndEnd(startDate, endDate);
    LocalDate currentDate = startDate;
    LinkedHashMap<LocalDate, Double> valueMap = new LinkedHashMap<>();
    LinkedHashMap<LocalDate, Double> valueMapMonth = new LinkedHashMap<>();
    LinkedHashMap<LocalDate, Double> valueMapYear = new LinkedHashMap<>();

    while (!currentDate.isAfter(endDate)) {
      try {
        double value;
        if (portfolioNum <= getSizeOfSimplePortfolio()) {
          value = getValueAtCertainDate(portfolioNum, currentDate);
          valueMap.put(currentDate, value);
        } else {
          value = superPortfolioTimeBoundaryValue(portfolioNum, currentDate);
          valueMap.put(currentDate, value);
        }
        checkEndOfMonthAndYear(valueMapMonth, valueMapYear, currentDate, value);

      } catch (IllegalArgumentException e) {
        currentDate = currentDate.plusDays(1);
        continue;
      }
      currentDate = currentDate.plusDays(1);
    }
    // Use the size of valueMap as the totalDays to ensure we only consider trading days
    return getString(valueMap, "d")
            + getString(valueMapMonth, "m")
            + getString(valueMapYear, "y");
  }

  /**
   * helper function to check month and year end.
   *
   * @param valueMapMonth value map for month
   * @param valueMapYear  value map for year
   * @param currentDate   current date
   * @param value         value
   */
  private void checkEndOfMonthAndYear(LinkedHashMap<LocalDate, Double> valueMapMonth,
                                      LinkedHashMap<LocalDate, Double> valueMapYear,
                                      LocalDate currentDate, double value) {
    boolean isEndOfMonth = currentDate
            .plusDays(1).getMonth() != currentDate.getMonth();

    if (isEndOfMonth) {

      valueMapMonth.put(currentDate, value);
    }

    boolean isEndOfYear = currentDate
            .plusDays(1).getYear() != currentDate.getYear();

    if (isEndOfYear) {
      valueMapYear.put(currentDate, value);
    }
  }

  /**
   * form a chart base on value map and interval.
   *
   * @param valueMap the value map
   * @param interval the interval
   * @return String the chart
   * @throws IllegalArgumentException if the value map is empty
   */
  private String formatChart(Map<LocalDate, Double> valueMap, long interval, String type) {
    Iterator<LocalDate> iterator = valueMap.keySet().iterator();
    if (!iterator.hasNext()) {
      throw new IllegalArgumentException("Invalid date range.");
    }
    LocalDate firstDate = iterator.next();
    StringBuilder chart = new StringBuilder();
    typeSpecific1(type, firstDate, valueMap, chart);
    double maxValue = Collections.max(valueMap.values());
    double scale = maxValue / 50;

    iterator = valueMap.keySet().iterator();
    LocalDate nextDate = null;

    if (iterator.hasNext()) {
      nextDate = iterator.next();
    }

    for (Map.Entry<LocalDate, Double> entry : valueMap.entrySet()) {

      if (nextDate != null && entry.getKey().isEqual(nextDate)) {

        typeSpecific2(chart, entry, type);
        int stars = (int) (entry.getValue() / scale);
        chart.append("*".repeat(Math.max(0, stars))).append("\n");
        for (int i = 0; i < interval; i++) {
          if (iterator.hasNext()) {
            nextDate = iterator.next();
          }
        }
      }
    }

    chart.append("Scale: * = ").append(scale).append(" units\n\n");
    return chart.toString();
  }

  /**
   * type specific helper function to append title to chart.
   *
   * @param type      type of chart
   * @param firstDate first date
   * @param valueMap  value map
   * @param chart     string builder
   */
  private void typeSpecific1(String type, LocalDate firstDate, Map<LocalDate, Double> valueMap,
                             StringBuilder chart) {
    LocalDate lastDay = (LocalDate) valueMap.keySet().toArray()[valueMap.size() - 1];
    if (type.equals("m")) {

      chart.append("Performance of Portfolio/Stock from ")
              .append(firstDate.getYear()).append(" ").append(firstDate.getMonth())
              .append(" to ")
              .append(lastDay.getYear()).append(" ").append(lastDay.getMonth())
              .append("\n");
    } else if (type.equals("y")) {
      chart.append("Performance of Portfolio/Stock from ")
              .append(firstDate.getYear())
              .append(" to ")
              .append(lastDay.getYear())
              .append("\n");
    } else {
      chart.append("Performance of Portfolio/Stock from ")
              .append(firstDate)
              .append(" to ")
              .append(valueMap.keySet().toArray()[valueMap.size() - 1])
              .append("\n");
    }
  }

  /**
   * type specific helper function2 to add row info.
   *
   * @param chart String builder
   * @param entry entry in map
   * @param type  type of chart
   */
  private void typeSpecific2(StringBuilder chart, Map.Entry<LocalDate, Double> entry, String type) {
    if (type.equals("m")) {
      chart.append(entry.getKey().getYear()).append(" ")
              .append(entry.getKey().getMonth().toString(), 0, 3)
              .append(": ");
    } else if (type.equals("y")) {
      chart.append(entry.getKey().getYear()).append(": ");
    } else {
      chart.append(entry.getKey()).append(": ");
    }

  }

  /**
   * get the size of simple portfolios.
   *
   * @return size
   */
  @Override
  public int getSizeOfSimplePortfolio() {
    return myInvestments.size();
  }

  /**
   * get the size of super portfolios.
   *
   * @return size
   */
  @Override
  public int getSizeOfSuperPortfolio() {
    return mySuperPortfolioInvestments.size();
  }

  /**
   * weight investment on a list of companies on specific day.
   *
   * @param portfolioNum portfolio number
   * @param map          mapping from company to percent
   * @param date         investment date
   * @param money        money amount
   * @throws IllegalArgumentException if illegal argument happens
   */
  @Override
  public void weightInvestment(int portfolioNum, HashMap<String, Double> map, LocalDate date,
                               double money) throws IllegalArgumentException {
    if (portfolioNum <= myInvestments.size() || portfolioNum > getSize()) {
      throw new IllegalArgumentException("This number is not a super portfolio.");
    }
    SuperPortfolio sp = (SuperPortfolio) getPortfolio(portfolioNum);
    if (sp != null) {
      try {
        sp.weightInvestment(map, date, money);
        return;
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }

    throw new IllegalArgumentException("Cannot found the super portfolio.");
  }

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
  @Override
  public void weightInvestmentStartToFinish(int portfolioNum, HashMap<String, Double> map,
                                            LocalDate start, LocalDate end,
                                            double money, int period)
          throws IllegalArgumentException {
    if (portfolioNum <= myInvestments.size() || portfolioNum > getSize()) {
      throw new IllegalArgumentException("This number is not a super portfolio.");
    }
    SuperPortfolio sp = (SuperPortfolio) getPortfolio(portfolioNum);
    if (sp != null) {
      try {
        sp.weightInvestmentStartToFinish(map, start, end, money, period);
        return;
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }
    throw new IllegalArgumentException("Cannot found the super portfolio.");
  }

  @Override
  public ArrayList<String> getMyInvestments() {
    ArrayList<String> list = new ArrayList<>();
    for (Portfolio port : myInvestments) {
      list.add(port.getPortfolioName());
    }
    return list;
  }

  @Override
  public ArrayList<String> getMySuperInvestments() {
    ArrayList<String> list = new ArrayList<>();
    for (SuperPortfolio sp : mySuperPortfolioInvestments) {
      list.add(sp.getPortfolioName());
    }
    return list;
  }
}
