package model;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import controller.ControllerImp;


/**
 * Represents a single stock in a portfolio, including its symbol, number of shares, and prices.
 */
public class Stock {
  private final String symbol;
  private double initialPrice;
  private double shares;
  private LocalDate timestamp;
  private double price;

  /**
   * Constructs a new Stock instance with initial settings.
   * This constructor is private to enforce the use of the StockBuilder.
   *
   * @param symbol The symbol representing the stock in the market.
   * @param shares The number of shares held.
   */
  private Stock(String symbol, double shares) {
    this.shares = shares;
    this.symbol = symbol;
    this.timestamp = LocalDate.now();
    this.price = 0;
    update();
    initialPrice = price;
  }

  /**
   * Constructs a detailed Stock instance including initial price and purchase date.
   * This constructor is private to enforce the use of the StockBuilder.
   *
   * @param symbol       The symbol representing the stock.
   * @param shares       The number of shares held.
   * @param price        The current price of the stock.
   * @param d            The date when the stock was purchased or last updated.
   * @param initialPrice The price of the stock at the time of purchase.
   */
  private Stock(String symbol, double shares, double price, LocalDate d, double initialPrice) {
    this.price = price;
    this.shares = shares;
    this.symbol = symbol;
    this.timestamp = d;
    this.initialPrice = initialPrice;
  }

  /**
   * Retrieves a StockBuilder instance to facilitate the construction of a Stock object.
   *
   * @return A new instance of StockBuilder.
   */
  public static StockBuilder getBuilder() {
    return new StockBuilder();
  }

  /**
   * Fetches stock data from the AlphaVantage API.
   *
   * @param symbol The stock symbol.
   * @return The raw data string if successful, otherwise null.
   */
  public static String getD(String symbol) {
    String found = "success";
    String get = ControllerImp.getData(symbol);

    if (get == null || get.isEmpty()) {
      found = ControllerImp.storeData(symbol);
      if (found != null && found.equals("success")) {
        get = ControllerImp.getData(symbol);
      } else {
        return null;
      }
    }
    if (get == null || get.isEmpty() || get.contains("Error")
            || get.contains("Information") || get.contains("Note")) {
      return null;
    }
    return get;
  }

  /**
   * Retrieves the trading volume of a specific stock symbol from the fetched data.
   *
   * @param symbol The stock symbol.
   * @param time   the time
   * @return The trading volume as a long, or -1 if the data could not be fetched.
   * @throws IllegalArgumentException if Illegal argument happens
   */
  public static long getVolume(String symbol, LocalDate time) throws IllegalArgumentException {

    String get = getD(symbol);
    if (get == null) {
      throw new IllegalArgumentException("No such stock symbol's data.");
    }

    String[] lines = get.split("\n");
    for (String line : lines) {
      String[] l = line.split(",");
      try {
        if (l[0] != null && LocalDate.parse(l[0]).isEqual(time)) {
          return Long.parseLong(l[5]);
        }
      } catch (DateTimeParseException e) {
        continue;
      }
    }

    throw new IllegalArgumentException("No such date.");
  }

  /**
   * Retrieves the current price of the stock.
   *
   * @return The current price of the stock.
   */
  public double getPrice() {
    return price;
  }

  /**
   * set price of this stock.
   *
   * @param price setting price
   */
  public void setPrice(double price) {
    this.price = price;
  }

  /**
   * Retrieves the number of shares of the stock.
   *
   * @return The number of shares.
   */
  public double getShares() {
    return shares;
  }

  /**
   * set shares of this stock.
   *
   * @param shares shares number
   */
  public void setShares(double shares) {
    this.shares = shares;
  }

  /**
   * Calculates the current market value of the stock
   * based on its current price and the number of shares.
   *
   * @return The market value of the stock.
   */
  public double evaluate() {

    double result = price * shares * 100;
    result = Math.round(result) / 100.0;
    return result;
  }

  /**
   * Retrieves the symbol of the stock.
   *
   * @return The stock symbol.
   */
  public String getSymbol() {
    return symbol;
  }

  /**
   * Retrieves the timestamp when the stock was last updated or bought.
   *
   * @return The timestamp as a LocalDate.
   */
  public LocalDate getTimestamp() {
    return timestamp;
  }

  /**
   * set the time stamp of this stock.
   *
   * @param time time stamp
   */
  public void setTimestamp(LocalDate time) {
    this.timestamp = time;
  }

  /**
   * Provides a string representation of the stock, including its symbol,
   * price, shares, initial price, and purchase timestamp.
   *
   * @return A string describing the stock.
   */
  public String toString() {
    return symbol + " price:" + getPrice() + " shares:" + getShares()
            + " initial price:" + getInitialPrice()
            + " trade time:" + getTimestamp().toString();
  }

  /**
   * Updates the price of the stock by fetching the latest data.
   *
   * @throws IllegalArgumentException if the stock price does not update for more than 10 days
   */
  public void update() throws IllegalArgumentException {

    String get = getD(this.symbol);
    if (get == null) {
      return;
    }
    String[] lines = get.split("\n");

    String[] line = lines[1].split(",");
    if (LocalDate.parse(line[0]).plusDays(10).isAfter(LocalDate.now())) {
      this.price = Double.parseDouble(line[4]);
    } else {
      throw new IllegalArgumentException("The stock " + this.symbol + " may have quit the market."
              + "stock prices haven't updated for 10 days.");
    }
  }

  /**
   * Retrieves the historical price of the stock on a given date.
   *
   * @param date The date for which to retrieve the price.
   * @return The historical price on exact day or before that day
   * @throws IllegalArgumentException if anything goes wrong
   */
  public double getHistoricalPrice(LocalDate date) throws IllegalArgumentException {
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("Cannot get future prices.");
    }
    String get = getD(this.symbol);
    if (get == null) {
      throw new IllegalArgumentException("Data not found for this stock symbol " + this.symbol);
    }
    String[] lines = get.split("\n");
    if (LocalDate.parse(lines[1].split(",")[0])
            .plusDays(10).isBefore(date)) {
      throw new IllegalArgumentException("The stock may have quit the market before date:" + date
              + "\nNo price update for more than 10 days.");
    }
    for (String line : lines) {
      String[] l = line.split(",");
      if (l[0] != null && l[0].equals(date.toString())) {
        return Double.parseDouble(l[4]);
      }
      try {
        if (l[0] != null && LocalDate.parse(l[0]).isBefore(date)) {
          return Double.parseDouble(l[4]);
        }
      } catch (DateTimeParseException e) {
        continue;
      }
    }
    throw new IllegalArgumentException("Data for this date is unavailable.");
  }

  /**
   * Retrieves the initial price of the stock at the time of purchase.
   *
   * @return The initial price of the stock.
   */
  public double getInitialPrice() {
    return this.initialPrice;
  }

  /**
   * set initial price of this stock.
   *
   * @param price setting price
   */
  public void setInitialPrice(double price) {
    this.initialPrice = price;
  }

  /**
   * check if gain or lose on specified day.
   *
   * @param time the specified time
   * @return String the info of result
   * @throws IllegalArgumentException if Illegal argument happens
   */
  public String gainOrLoseOnGivenDay(LocalDate time) throws IllegalArgumentException {
    String get = getD(this.symbol);
    if (get == null) {
      throw new IllegalArgumentException("No data found for this stock symbol:" + this.symbol);
    }
    String[] lines = get.split("\n");
    for (String line : lines) {
      String[] l = line.split(",");
      if (l[0] != null && l[0].equals(time.toString())) {
        double result = Double.parseDouble(l[4]) - Double.parseDouble(l[1]);
        result = Math.round(result * 100) / 100.0;
        if (result > 0) {
          return "Gain:" + result;
        } else if (result < 0) {
          return "Lose:" + (-result);
        } else {
          return "Equal 0";
        }
      }
    }
    throw new IllegalArgumentException("Data not found for specified date:" + time);
  }

  /**
   * check if gain or lose over periods of time for a stock.
   *
   * @param start start of period
   * @param end   end of period
   * @return String info of result
   * @throws IllegalArgumentException if anything goes wrong
   */
  public String gainOrLoseOverPeriodOfTime(LocalDate start, LocalDate end)
          throws IllegalArgumentException {
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start date is after end date.");
    }
    double startPrice;
    double endPrice;

    String get = getD(this.symbol);
    if (get == null) {
      throw new IllegalArgumentException("No data get for this stock symbol.");
    }
    String[] lines = get.split("\n");
    startPrice = getPriceFromDateAndLines(start, lines);
    if (Double.isNaN(startPrice)) {
      throw new IllegalArgumentException("No data found for this start date.");
    }
    endPrice = getPriceFromDateAndLines(end, lines);
    if (Double.isNaN(endPrice)) {
      throw new IllegalArgumentException("No data found for this end date.");
    }
    double v = Math.round((endPrice - startPrice) * 100) / 100.0;
    if (startPrice < endPrice) {
      return "Gain: " + v + ",start price:" + startPrice + ",end price:" + endPrice;
    } else if (startPrice > endPrice) {
      return "Lose: " + v + ",start price:" + startPrice + ",end price:" + endPrice;
    } else {
      return "Equal 0 profit,start price:" + startPrice + ",end price:" + endPrice;
    }

  }

  /**
   * get price from date and lines, a helper function.
   *
   * @param date  the date
   * @param lines the data
   * @return closing price of specified date
   * @throws IllegalArgumentException if date is after today
   */
  private double getPriceFromDateAndLines(LocalDate date, String[] lines)
          throws IllegalArgumentException {
    double price = Double.NaN;
    if (date.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("The specified date is beyond today:" + date);
    }
    if (LocalDate.parse(lines[1].split(",")[0]).plusDays(10)
            .isBefore(date)) {
      throw new IllegalArgumentException("The stock may have quit the market before date:" + date);
    }
    for (String line : lines) {
      String[] l = line.split(",");
      if (l[0] != null && l[0].equals(date.toString())) {
        price = Double.parseDouble(l[4]);
        break;
      }
      try {
        if (l[0] != null && LocalDate.parse(l[0]).isBefore(date)) {

          price = Double.parseDouble(l[4]);
          break;
        }
      } catch (DateTimeParseException e) {
        continue;
      }
    }
    return price;
  }

  /**
   * calculate x day moving average.
   *
   * @param start start of x day, calculate backwards.
   * @param x     the x day
   * @return double the calculated value
   * @throws IllegalArgumentException if illegal argument happens
   */
  public double xDayMovingAverage(LocalDate start, int x) throws IllegalArgumentException {
    if (start.isAfter(LocalDate.now())) {
      throw new IllegalArgumentException("x day moving avg start date is after current date:"
              + start);
    }
    String get = getD(this.symbol);
    if (get == null) {
      throw new IllegalArgumentException("No data get for this stock symbol.");
    }
    String[] lines = get.trim().split("\n");
    int counter = 0;
    double sum = 0;
    for (String line : lines) {
      String[] l = line.split(",");
      try {
        if (LocalDate.parse(l[0]).isEqual(start) || LocalDate.parse(l[0]).isBefore(start)) {
          counter++;
          sum += Double.parseDouble(l[4]);
        }
        if (counter == x) {
          break;
        }
      } catch (DateTimeParseException e) {
        continue;
      }
    }
    if (counter < x) {
      throw new IllegalArgumentException("Not enough data for " + x + " day moving average.");
    } else {
      return Math.round(sum / x * 100) / 100.0;
    }
  }

  /**
   * calculate crossovers based on start,end.
   *
   * @param start start of period
   * @param end   end of period
   * @return String the result info
   * @throws IllegalArgumentException if illegal argument happens
   */
  public String crossovers(LocalDate start, LocalDate end) throws IllegalArgumentException {
    StringBuilder log = new StringBuilder();
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start date is after end date.");
    }
    String get = getD(this.symbol);
    if (get == null) {
      throw new IllegalArgumentException("No data get for this stock symbol.");
    }
    String[] lines = get.trim().split("\n");
    assignStartAndEnd(lines, start, end);
    for (int i = 1; i < lines.length - 30; i++) {
      String[] l = lines[i].split(",");
      String[] previousDay = lines[i + 1].split(",");
      LocalDate currentDate = LocalDate.parse(l[0]);
      LocalDate previousDate = LocalDate.parse(previousDay[0]);
      if ((currentDate.isAfter(start) || currentDate.isEqual(start))
              && (currentDate.isBefore(end) || currentDate.isEqual(end))) {
        double movingAvgToday = xDayMovingAverage(currentDate, 30);
        double movingAvgYesterday = xDayMovingAverage(previousDate, 30);
        double currentPrice = Double.parseDouble(l[4]);
        double previousPrice = Double.parseDouble(previousDay[4]);
        if (currentPrice > movingAvgToday && previousPrice < movingAvgYesterday) {
          log.append("positive crossover for:").append(currentDate)
                  .append(System.lineSeparator());
        }
        if (currentPrice < movingAvgToday && previousPrice > movingAvgYesterday) {
          log.append("negative crossover for:").append(currentDate)
                  .append(System.lineSeparator());
        }
      }
    }
    if (log.length() == 0) {
      log.append("No crossovers for given period.");
    }
    return log.toString();
  }

  /**
   * assign start and end helper function.
   *
   * @param lines data
   * @param start start
   * @param end   end
   * @throws IllegalArgumentException if illegal argument happens
   */
  private void assignStartAndEnd(String[] lines, LocalDate start, LocalDate end)
          throws IllegalArgumentException {
    if (lines.length < 32) {
      throw new IllegalArgumentException("This stock do not have 30 days data for two days.");
    }
    String[] beginningOfData = lines[1].split(",");
    String[] endOfData = lines[lines.length - 31].split(",");
    if (LocalDate.parse(beginningOfData[0]).isBefore(end)) {
      if (LocalDate.parse(beginningOfData[0]).isBefore(start)) {
        throw new IllegalArgumentException("No data found suitable for the date range ["
                + start + "," + end + "]");
      }
      end = LocalDate.parse(beginningOfData[0]);
    }
    if (LocalDate.parse(endOfData[0]).isAfter(start)) {
      if (LocalDate.parse(endOfData[0]).isAfter(end)) {
        throw new IllegalArgumentException("No data found suitable for the date range ["
                + start + "," + end + "]");
      }
      start = LocalDate.parse(endOfData[0]);
    }
  }

  /**
   * calculate moving crossovers for given period given x given y.
   *
   * @param start start of period
   * @param end   end of period
   * @param x     x day moving average x
   * @param y     y day moving average y
   * @return String the result info
   * @throws IllegalArgumentException if illegal argument happens
   */
  public String movingCrossovers(LocalDate start, LocalDate end, int x, int y)
          throws IllegalArgumentException {
    StringBuilder log = new StringBuilder();
    if (start.isAfter(end)) {
      throw new IllegalArgumentException("Start date is after end date.");
    }
    if (x >= y) {
      throw new IllegalArgumentException("x cannot be bigger than or equal to y,x:"
              + x + ",y:" + y);
    }
    String get = getD(this.symbol);
    if (get == null) {
      throw new IllegalArgumentException("No data get for this stock symbol.");
    }
    String[] lines = get.trim().split("\n");
    assignStartAndEndBaseOnY(lines, y, start, end);
    processLog(log, lines, x, y, start, end);
    if (log.length() == 0) {
      log.append("No moving crossovers for given period.");
    }
    return log.toString();
  }

  /**
   * helper function assign start and end base on y.
   *
   * @param lines data
   * @param y     y day moving average y
   * @param start start of period
   * @param end   end of period
   * @throws IllegalArgumentException if illegal argument happens
   */
  private void assignStartAndEndBaseOnY(String[] lines, int y, LocalDate start, LocalDate end) {
    if (lines.length < (y + 2)) {
      throw new IllegalArgumentException("This stock do not have " + y
              + " days data for two days.");
    }
    String[] beginningOfData = lines[1].split(",");
    String[] endOfData = lines[lines.length - y - 1].split(",");
    if (LocalDate.parse(beginningOfData[0]).isBefore(end)) {
      if (LocalDate.parse(beginningOfData[0]).isBefore(start)) {
        throw new IllegalArgumentException("No suitable data for this range of time.");
      }
      end = LocalDate.parse(beginningOfData[0]);
    }
    if (LocalDate.parse(endOfData[0]).isAfter(start)) {
      if (LocalDate.parse(endOfData[0]).isAfter(end)) {
        throw new IllegalArgumentException("No suitable data for this range of time.");
      }
      start = LocalDate.parse(endOfData[0]);
    }
  }

  /**
   * helper function process log.
   *
   * @param log   log string builder
   * @param lines data
   * @param x     x day moving average x
   * @param y     y day moving average y
   * @param start start
   * @param end   end
   */
  private void processLog(StringBuilder log, String[] lines, int x, int y,
                          LocalDate start, LocalDate end) {
    for (int i = 1; i < lines.length - y; i++) {
      String[] l = lines[i].split(",");
      String[] previousDay = lines[i + 1].split(",");
      LocalDate currentDate = LocalDate.parse(l[0]);
      LocalDate yesterday = LocalDate.parse(previousDay[0]);
      if ((currentDate.isAfter(start) || currentDate.isEqual(start))
              && (currentDate.isBefore(end) || currentDate.isEqual(end))) {
        double yDayMovingAvgToday = xDayMovingAverage(currentDate, y);
        double yDayMovingAvgYesterday = xDayMovingAverage(yesterday, y);
        double currentXDayMovingAvg = xDayMovingAverage(currentDate, x);
        double previousDayXDayMovingAvg = xDayMovingAverage(yesterday, x);
        if (currentXDayMovingAvg > yDayMovingAvgToday
                && previousDayXDayMovingAvg < yDayMovingAvgYesterday) {
          log.append("positive moving crossover for:").append(currentDate)
                  .append(System.lineSeparator());
        }
        if (currentXDayMovingAvg < yDayMovingAvgToday
                && previousDayXDayMovingAvg > yDayMovingAvgYesterday) {
          log.append("negative moving crossover for:").append(currentDate)
                  .append(System.lineSeparator());
        }
      }
    }
  }

  /**
   * checks if a stock has data on given day.
   *
   * @param time date
   * @return double the stock value
   * @throws IllegalArgumentException if not found
   */
  public double hasDataOnGivenDay(LocalDate time) throws IllegalArgumentException {
    String get = getD(this.symbol);
    if (get == null) {
      throw new IllegalArgumentException("No data found for this stock symbol:" + this.symbol);
    }
    String[] lines = get.split("\n");
    for (String line : lines) {
      String[] l = line.split(",");
      if (l[0] != null && l[0].equals(time.toString())) {
        double result = Double.parseDouble(l[4]);
        result = Math.round(result * 100) / 100.0;
        return result;
      }
    }
    throw new IllegalArgumentException("Data not found for specified date and stock:"
            + time + " " + symbol);
  }

  /**
   * A builder class for creating {@code Stock} instances.
   * Provides a fluent API for setting the properties of a new {@code Stock} object.
   */
  public static class StockBuilder {
    private String symbol;
    private double shares;
    private double initialPrice;
    private LocalDate timestamp;
    private double price;

    /**
     * Private constructor for StockBuilder to prevent instantiation outside of getBuilder() method.
     * This ensures that Stock objects are only created through
     * the builder pattern for consistency and encapsulation.
     */
    private StockBuilder() {
      // Private constructor to enforce use through getBuilder()
    }

    /**
     * Sets the symbol for this stock.
     *
     * @param symbol The symbol of the stock.
     * @return The builder instance for chaining.
     */
    public StockBuilder symbol(String symbol) {
      this.symbol = symbol;
      return this;
    }

    /**
     * Sets the number of shares for this stock.
     *
     * @param shares The number of shares.
     * @return The builder instance for chaining.
     */
    public StockBuilder shares(double shares) {
      this.shares = shares;
      return this;
    }

    /**
     * Sets the initial price for this stock.
     *
     * @param initial The initial price of the stock.
     * @return The builder instance for chaining.
     */
    public StockBuilder initialPrice(double initial) {
      this.initialPrice = initial;
      return this;
    }

    /**
     * Sets the current price for this stock.
     *
     * @param price The current price of the stock.
     * @return The builder instance for chaining.
     */
    public StockBuilder price(double price) {
      this.price = price;
      return this;
    }

    /**
     * Sets the timestamp for when the stock was bought.
     *
     * @param timestamp The purchase date of the stock.
     * @return The builder instance for chaining.
     */
    public StockBuilder timeStamp(LocalDate timestamp) {
      this.timestamp = timestamp;
      return this;
    }

    /**
     * Creates a {@code Stock} instance using the builder's current settings.
     *
     * @return A new {@code Stock} instance.
     */
    public Stock build() {
      //use the currently set values to create the Customer object
      //with the only private constructor above.
      return new Stock(this.symbol, this.shares);
    }

    /**
     * Creates a detailed {@code Stock} instance using the builder's current settings,
     * including price and timestamp.
     *
     * @return A new detailed {@code Stock} instance.
     */
    public Stock build2() {
      return new Stock(this.symbol, this.shares, this.price, this.timestamp, this.initialPrice);
    }

  }
}
