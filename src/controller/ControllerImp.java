package controller;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.io.PrintStream;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Model;
import model.Portfolio;
import model.Stock;
import model.SuperPortfolio;
import view.TextView;
import view.ViewImp;

/**
 * An implementation of the {@code Controller} interface that coordinates interactions
 * between the model and view.
 */
public class ControllerImp implements Controller {
  private static TextView view = new ViewImp(System.out);
  private static DataSourceAPI api = new AlphaVantageDemo();
  private final Model model;
  private final Scanner in;

  /**
   * Constructs a new {@code ControllerImp} instance, initializing it with the given model, view,
   * input stream, and output stream. This controller facilitates interactions between the model
   * and view components, processes user inputs, and updates the view accordingly.
   *
   * @param model     The model component responsible for managing investment data and business
   *                  logic.
   * @param viewView  The view component responsible for interacting with the user,
   *                  displaying menus, messages, and collecting user inputs.
   * @param input     The InputStream from which the controller reads user inputs.
   *                  Typically, this is System.in, but it can be redirected for testing purposes.
   * @param out       The PrintStream to which the controller writes outputs.
   *                  Typically, this is System Out, but it can be redirected for testing purposes.
   * @param apiForUse the input data source
   */
  public ControllerImp(Model model, TextView viewView, InputStream input, PrintStream out,
                       DataSourceAPI apiForUse) {
    this.model = model;
    view = viewView;
    //private final Scanner in;
    this.in = new Scanner(input);
    api = apiForUse;
  }

  /**
   * Store data into csv files by having a stock symbol.
   * calls to  api as of now.
   * but can handle more sources in the future.
   *
   * @param stock stock symbol
   * @return String success or other
   */
  public static String storeData(String stock) {

    try {

      return api.storeData(stock);
    } catch (Exception e) {
      view.displayErrorMessage(e.toString());
      return null;
    }
  }

  /**
   * Get data from csv files by having a stock symbol.
   * calls to api as of now.
   * but can handle more sources in the future.
   *
   * @param stock stock symbol
   * @return String the data
   */
  public static String getData(String stock) {
    String s;
    try {

      s = api.getData(stock);
    } catch (Exception e) {
      view.displayErrorMessage(e.toString());
      return null;
    }
    return s;
  }

  /**
   * run the controller.
   */
  @Override
  public void toGo() {

    boolean running = true;
    while (running) {
      int choice = mainMenu();
      in.nextLine();

      switch (choice) {
        case 1:
          case1();
          break;
        case 2:
          case2();
          break;
        case 3:
          case3();
          break;
        case 4:
          case4();
          break;
        case 5:
          case5();
          break;
        case 6:
          case6();
          break;
        case 7:
          case7();
          break;
        case 8:
          case8();
          break;
        case 9:
          case9();
          break;
        case 10:
          case10();
          break;
        case 11:
          case11();
          break;
        case 12:
          case12();
          break;
        case 13:
          case13();
          break;
        case 14:
          case14();
          break;
        case 15:
          case15();
          break;
        case 16:
          case16();
          break;
        case 17:
          case17();
          break;
        case 18:
          case18();
          break;
        case 19:
          case19();
          break;
        case 20:
          case20();
          break;
        case 21:
          case21();
          break;
        case 22:
          running = false;
          view.displayMessage("Exiting...");
          break;
        default:
          view.displayErrorMessage("Invalid option, please try again.");
      }
    }
  }

  /**
   * show main menu and get choice from user.
   *
   * @return int the choice number
   */
  private int mainMenu() {
    int choice;
    while (true) {
      view.showMenu();
      view.getUserChoice();
      try {
        choice = in.nextInt();
        view.displayMessage("Input number is:" + choice);
        break;
      } catch (InputMismatchException e) {
        view.displayErrorMessage("No valid input.");
        in.nextLine();
      }

    }
    return choice;

  }

  /**
   * Initiates the process to create and set up a new portfolio,
   * including naming the portfolio and adding stocks to it.
   * Users are prompted to input a name for the portfolio and then enter stock
   * ticker symbols and share numbers to add to the portfolio.
   * The method handles user input validation and provides feedback through the view.
   */
  private void case1() {
    Portfolio newP = model.addPortfolio();
    String name = "";
    while (name.isEmpty()) {
      view.displayMessage("Please input name for this portfolio:");
      name = in.nextLine();
    }
    newP.setPortfolioName(name);
    view.displayMessage("New portfolio added.");
    enterStockTickerSymbol(newP);

  }

  /**
   * Enter stock ticker symbol and check if valid.
   *
   * @param newP new portfolio created
   */
  private void enterStockTickerSymbol(Portfolio newP) {
    while (true) {
      view.displayMessage("Please enter Stock ticker symbol,enter q to stop:");
      String symbol = in.nextLine();
      if (symbol.equals("q")) {
        break;
      } else {
        symbol = symbol.toUpperCase();
      }
      if (newP.hasCompany(symbol)) {
        view.displayMessage("This company is already recorded.");
        continue;
      }
      String data = ControllerImp.getData(symbol);
      String found = "success";
      if (data == null) {
        found = ControllerImp.storeData(symbol);

      }
      if (found == null || !found.equals("success")) {
        view.displayErrorMessage("Ticker symbol not found.");
        view.displayErrorMessage(found);
      } else {
        enterShareNum(symbol, newP);
      }
    }
  }

  /**
   * enter share number and check if valid.
   *
   * @param symbol Stock symbol
   * @param newP   new Portfolio created
   */
  private void enterShareNum(String symbol, Portfolio newP) {
    while (true) {
      long shares = makeSureSharesIsPositiveNumber();
      if (shares == -1) {
        continue;
      }
      LocalDate date = inputTime();
      int response = compareSharesWithVolume(symbol, shares, date);
      if (response == 1) {
        continue;
      } else if (response == 2) {
        break;
      } else if (response == 3) {
        Stock temp = newP.addStock(symbol, shares);
        temp.setTimestamp(date);
        temp.setInitialPrice(temp.hasDataOnGivenDay(date));
        break;
      }
    }

  }

  /**
   * compare shares with volume and do the next step.
   *
   * @param symbol stock symbol
   * @param shares shares to buy
   * @return 1 if continue the outer loop, 2 if break the outer loop
   */
  private int compareSharesWithVolume(String symbol, long shares, LocalDate time) {
    long v;
    try {
      v = Stock.getVolume(symbol, time);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
      return 1;
    }
    if (v == 0) {
      view.displayMessage("The volume is 0,cannot buy.");
      return 2;
    }
    if (shares > v) {
      view.displayErrorMessage("Maximum exceeded:" + v);
      return 1;
    }
    view.displayMessage("shares:" + shares);

    return 3;
  }

  /**
   * Initiates the process to create and set up a new super portfolio,
   * including naming the portfolio and adding to the model.
   * Users are prompted to input a name for the portfolio.
   * The method handles user input validation and provides feedback through the view.
   */
  private void case2() {
    SuperPortfolio newP = model.addSuperPortfolio();
    String name = "";
    while (name.isEmpty()) {
      view.displayMessage("Please input name for this portfolio:");
      name = in.nextLine();
    }
    newP.setPortfolioName(name);
    view.displayMessage("New flexible portfolio added.");
  }

  /**
   * Displays all portfolios managed by the model.
   * Information about each portfolio is retrieved from
   * the model and presented to the user via the view.
   */
  private void case3() {
    String portfolios = model.showAllPortfolios();
    view.displayPortfolios(portfolios);
  }

  /**
   * Displays detailed information about a specific portfolio selected by the user.
   * Users are prompted to select a portfolio,
   * and detailed information about the chosen portfolio is displayed.
   */
  private void case4() {
    int portfolioNumber = inputPortfolioNumber();
    if (portfolioNumber != 0) {
      String portfolioInfo = model.getSpecificPortfolioInfo(portfolioNumber);
      view.displayPortfolioDetails(portfolioInfo);
    }
  }

  /**
   * buy a specified number of shares of specified stock for a super portfolio.
   */
  private void case5() {
    buyOrSellSuperPortfolioStock("b");
  }

  /**
   * helper function to check if a num is a super portfolio num.
   *
   * @param num number
   * @return true or false
   */
  private boolean checkSuperPortfolioNum(int num) {
    return num > model.getSizeOfSimplePortfolio() && num <= model.getSize();
  }

  /**
   * buy or sell specified number of shares of specified stock for a super portfolio.
   *
   * @param buyOrSell "b" is buy, "s" is sell
   */
  private void buyOrSellSuperPortfolioStock(String buyOrSell) {
    int portfolioNumber = inputPortfolioNumber();
    if (portfolioNumber == 0) {
      return;
    }
    if (!checkSuperPortfolioNum(portfolioNumber)) {
      view.displayErrorMessage("Not a flexible portfolio.");
      return;
    }
    String symbol = inputSymbol();
    if (symbol.equals("q")) {
      return;
    }
    LocalDate time;
    long shares;
    while (true) {
      time = inputTime();
      shares = inputShares(symbol, time);
      if (shares == -1) {
        return;
      } else if (shares == -2) {
        continue;
      } else {
        break;
      }
    }
    try {
      if (portfolioNumber != 0 && buyOrSell.equals("b")) {
        model.superPortfolioAddStock(portfolioNumber, symbol, shares, time);
      } else if (portfolioNumber != 0 && buyOrSell.equals("s")) {
        model.superPortfolioSellStock(portfolioNumber, symbol, shares, time);
      }
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * input a stock symbol and check if valid.
   *
   * @return String stock symbol
   */
  private String inputSymbol() {
    while (true) {
      view.displayMessage("Please enter Stock ticker symbol,enter q to stop:");
      String symbol = in.nextLine();
      if (symbol.equals("q")) {
        return "q";

      } else {
        symbol = symbol.toUpperCase();
      }
      String data = ControllerImp.getData(symbol);
      String found = "success";
      if (data == null) {
        found = ControllerImp.storeData(symbol);

      }
      if (found == null || !found.equals("success")) {
        view.displayErrorMessage("Ticker symbol not found.");
        view.displayErrorMessage(found);
      } else {
        return symbol;
      }
    }
  }

  /**
   * input shares for a stock symbol. check the volume of the time.
   *
   * @param symbol stock symbol
   * @param time   time
   * @return shares
   */
  private long inputShares(String symbol, LocalDate time) {
    while (true) {
      long shares = makeSureSharesIsPositiveNumber();
      if (shares == -1) {
        continue;
      }

      int response = compareSharesWithVolume(symbol, shares, time);
      if (response == 1) {
        return -2;
      } else if (response == 2) {
        return -1;
      } else if (response == 3) {
        return shares;
      }
    }
  }

  /**
   * make sure shares is positive number.
   *
   * @return shares if success, -1 if invalid input
   */
  private long makeSureSharesIsPositiveNumber() {
    view.displayMessage("Please enter shares number,must be integer:");
    long shares;
    try {
      shares = in.nextLong();
    } catch (InputMismatchException e) {
      view.displayErrorMessage("No valid input.");
      in.nextLine();
      return -1;
    }
    in.nextLine();
    if (shares <= 0) {
      view.displayErrorMessage("cannot buy or sell 0 or less share of a company.");
      return -1;
    }
    return shares;
  }

  /**
   * input time of YYYY-MM-DD format.
   *
   * @return LocalDate the time
   */
  private LocalDate inputTime() {
    LocalDate date;
    while (true) {
      try {
        view.displayMessage("Please input date as:YYYY-MM-DD");
        String strDate = in.nextLine();
        date = LocalDate.parse(strDate);
        view.displayMessage("Date: " + date);
        break;
      } catch (DateTimeParseException e) {
        view.displayErrorMessage(e.getMessage());
      }
    }
    return date;
  }

  /**
   * sell a stock for super portfolio.
   */
  private void case6() {
    buyOrSellSuperPortfolioStock("s");
  }

  /**
   * calculate cost basis for super portfolio.
   */
  private void case7() {
    int portfolioNum = inputPortfolioNumber();
    if (portfolioNum == 0) {
      return;
    }
    LocalDate time = inputTime();
    try {
      double costBasis = model.superPortfolioCostBasis(portfolioNum, time);
      view.displayMessage("The cost basis for flexible portfolio " + portfolioNum + " at time "
              + time
              + " is:" + costBasis);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * Print value at specified time for super portfolio.
   */
  private void case8() {
    int number = inputPortfolioNumber();
    if (number == 0) {
      return;
    }
    LocalDate time = inputTime();
    try {
      double value = model.superPortfolioTimeBoundaryValue(number, time);
      view.displayMessage("Value at time " + time + " for flexible portfolio " + number + " is:"
              + value);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }

  }

  /**
   * Displays the value of a specific portfolio on a certain date, as entered by the user.
   * Users are prompted to select a portfolio and enter a date,
   * and the portfolio's value on that date is displayed.
   */
  private void case9() {
    int portfolioNum = inputPortfolioNumber();
    if (portfolioNum == 0) {
      return;
    }
    LocalDate date = inputTime();
    double value;
    try {
      value = model.getValueAtCertainDate(portfolioNum, date);
      if (value == -1) {
        view.displayErrorMessage("Some stock prices at that date are not recorded.");
      } else {
        view.displayMessage("The value of portfolio at date " + date.toString() + " is:" + value);
      }
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }

  }

  /**
   * Handles the process of selling a specific simple portfolio or deleting a specific super
   * portfolio chosen by the user.
   * Users are prompted to select a portfolio to sell or delete,
   * and feedback about the sale is provided through the view.
   */
  private void case10() {
    int sellNumber = inputPortfolioNumber();
    if (sellNumber != 0) {
      String sellInfo = model.sellPortfolio(sellNumber);
      view.displayMessage(sellInfo);
    }
  }

  /**
   * Triggers an update across all portfolios managed by the model.
   * This typically involves refreshing stock data or recalculating portfolio values.
   */
  private void case11() {
    model.update();
    view.displayMessage("All portfolios updated.");
  }

  /**
   * Saves a specific portfolio chosen by the user to a persistent storage.
   * Users are prompted to select a portfolio to save,
   * and feedback about the save operation is provided through the view.
   */
  private void case12() {
    int input = inputPortfolioNumber();
    if (input != 0) {
      boolean saved = model.save(input);
      if (saved) {
        view.displayMessage("saved successfully");
      } else {
        view.displayErrorMessage("save fails.");
      }
    }
  }

  /**
   * Loads a portfolio from an XML file specified by the user.
   * Users are prompted to enter the path to an XML file,
   * and the portfolio contained within is loaded into the model.
   */
  private void case13() {
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    Document document = null;
    try {
      factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();

      while (document == null) {
        view.displayMessage("Please enter the path of xml object:");
        String inputPath = in.nextLine();
        document = builder.parse(inputPath);
        document.getDocumentElement().normalize();
      }
    } catch (Exception e) {
      view.displayErrorMessage("Cannot load xml files.");
      return;
    }
    String selection = "no selection";
    while (!selection.equals("s") && !selection.equals("f")) {
      view.displayMessage("Enter s for simple portfolio, or f for flexible portfolio:");
      selection = in.nextLine();
    }
    try {
      model.loadPortfolio(document, selection);
    } catch (Exception e) {
      view.displayErrorMessage(e.toString());
      view.displayErrorMessage("The file have wrong format of json object.");
    }

  }

  /**
   * Prompts the user to select a portfolio number from the portfolios displayed.
   * Validates the user's input to ensure a valid portfolio number is selected.
   *
   * @return The selected portfolio number, or 0 if no valid selection is made.
   */
  private int inputPortfolioNumber() {
    int portfolioNumber = 0;
    if (model.getSize() == 0) {
      view.displayErrorMessage("There are no portfolios.");
      return 0;
    }
    while (true) {
      view.displayPortfolios(model.showAllPortfolios());
      view.readUserInput("Enter portfolio number: ");
      try {
        portfolioNumber = in.nextInt();
      } catch (InputMismatchException e) {
        view.displayErrorMessage("No valid input.");

        in.nextLine();
        continue;
      }
      in.nextLine();
      view.displayMessage("Input number is:" + portfolioNumber);
      if (portfolioNumber > model.getSize() || portfolioNumber <= 0) {
        view.displayErrorMessage("Invalid portfolio number.");

      } else {
        break;
      }
    }
    return portfolioNumber;
  }

  /**
   * check a stock gain or lose on given day.
   */
  private void case14() {
    String symbol = inputSymbol();
    if (symbol.equals("q")) {
      return;
    }
    LocalDate time = inputTime();
    try {
      view.displayMessage(model.stockGainOrLoseOnGivenDay(symbol, time));

    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * check a stock gain or lose over period of time.
   */
  private void case15() {
    String symbol = inputSymbol();
    if (symbol.equals("q")) {
      return;
    }
    view.displayMessage("Please input start date.");
    LocalDate start = inputTime();
    view.displayMessage("Please input end date.");
    LocalDate end = inputTime();
    try {
      view.displayMessage(model.stockGainOrLoseOverPeriodOfTime(symbol, start, end));
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * Print out the x day moving average for stock.
   */
  private void case16() {
    String symbol = inputSymbol();
    if (symbol.equals("q")) {
      return;
    }
    LocalDate time = inputTime();
    int x = enterDays("x");
    try {
      String result = "The " + x + " days moving average for stock " + symbol + " till time "
              + time + " is:";
      view.displayMessage(result + model.stockXDayMovingAverage(symbol, time, x));

    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * Prompt the user to enter days for moving average or other uses.
   *
   * @param name name
   * @return int the days
   */
  private int enterDays(String name) {
    int x = 0;
    while (x <= 0) {
      try {
        view.displayMessage("Please enter the number of days for " + name
                + " days moving average:");
        x = in.nextInt();
        in.nextLine();
      } catch (InputMismatchException e) {
        in.nextLine();
        continue;
      }
    }
    return x;
  }

  /**
   * find crossovers for stock during a period of time.
   */
  private void case17() {
    String symbol = inputSymbol();
    if (symbol.equals("q")) {
      return;
    }
    view.displayMessage("Please input start date.");
    LocalDate start = inputTime();
    view.displayMessage("Please input end date.");
    LocalDate end = inputTime();
    try {
      String result = "The crossovers for stock" + symbol + " from " + start + " till " + end
              + " listed:"
              + System.lineSeparator();
      view.displayMessage(result + model.stockCrossovers(symbol, start, end));

    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * find moving crossovers for stock during a period with x day and y day specified.
   */
  private void case18() {

    String symbol = inputSymbol();
    if (symbol.equals("q")) {
      return;
    }
    view.displayMessage("Please input start date.");
    LocalDate start = inputTime();
    view.displayMessage("Please input end date.");
    LocalDate end = inputTime();
    int x = enterDays("x");
    int y = enterDays("y");
    try {
      String result = "The moving crossovers for stock" + symbol + " from " + start + " till "
              + end
              + " for " + x + " days, " + y + " days " + " listed:"
              + System.lineSeparator();
      view.displayMessage(result + model.stockMovingCrossovers(symbol, start, end, x, y));

    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * display a stock or portfolio in chart.
   */
  private void case19() {
    int choice = 0;
    while (choice != 1 && choice != 2) {
      try {
        view.displayMessage("Please enter 1 for stock, 2 for portfolio:");
        choice = in.nextInt();
      } catch (InputMismatchException e) {
        in.nextLine();
        continue;
      }
      in.nextLine();
    }
    if (choice == 1) {
      String symbol = inputSymbol();
      if (symbol.equals("q")) {
        return;
      }
      view.displayMessage("Please input start time.");
      LocalDate start = inputTime();
      view.displayMessage("Please input end time.");
      LocalDate end = inputTime();
      try {
        view.displayMessage(model.generateChartForStock(symbol, start, end));
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }
    } else {
      int number = inputPortfolioNumber();
      if (number == 0) {
        return;
      }

      view.displayMessage("Please input start time.");
      LocalDate start = inputTime();
      view.displayMessage("Please input end time.");
      LocalDate end = inputTime();
      try {
        view.displayMessage(model.generateChartForPortfolio(number, start, end));
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }

    }
  }

  /**
   * invest in weight of each company at a specific time.
   */
  private void case20() {

    int portfolioNum = inputPortfolioNumber();
    if (portfolioNum == 0) {
      return;
    }
    if (portfolioNum <= model.getSizeOfSimplePortfolio()) {
      view.displayErrorMessage("This is not a flexible portfolio number.");
      return;
    }


    HashMap<String, Double> symbolPercent = inputSymbolPercent();


    double money = inputMoney();

    LocalDate selectionDate = inputTime();
    try {
      model.weightInvestment(portfolioNum, symbolPercent, selectionDate, money);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * input the total money amount.
   *
   * @return double the input
   */
  private double inputMoney() {
    double money;
    while (true) {
      view.displayMessage("Please input money amount to invest.");

      try {
        money = in.nextDouble();
      } catch (InputMismatchException e) {
        in.nextLine();
        view.displayErrorMessage("Not a double.");
        continue;
      }
      in.nextLine();
      if (money <= 0) {
        view.displayErrorMessage("Invalid money amount entered.");
        continue;
      }
      break;
    }
    return money;
  }

  /**
   * input the symbol percent hashmap.
   *
   * @return hashmap of String Double
   */
  private HashMap<String, Double> inputSymbolPercent() {
    HashMap<String, Double> symbolPercent = new HashMap<>();
    ArrayList<String> symbols = new ArrayList<>();
    String symbol = "NotAStock";
    while (!symbol.equals("q")) {
      symbol = inputSymbol();
      if (!symbol.equals("q") && !symbols.contains(symbol)) {
        symbols.add(symbol);
      }
    }
    double totalPercent = 1;
    for (int i = 0; i < symbols.size() - 1; i++) {
      while (true) {
        view.displayMessage("Please input percentage in decimal for stock " + symbols.get(i));
        double percentage;
        try {
          percentage = in.nextDouble();

        } catch (InputMismatchException e) {
          in.nextLine();
          continue;
        }
        in.nextLine();
        if (percentage < 0 || percentage > totalPercent) {
          view.displayErrorMessage("Cannot use this percentage because out of bound:" + percentage);
          continue;
        }
        totalPercent = totalPercent - percentage;
        symbolPercent.put(symbols.get(i), percentage);
        break;
      }
    }
    symbolPercent.put(symbols.get(symbols.size() - 1), totalPercent);
    view.displayMessage("The last stock " + symbols.get(symbols.size() - 1)
            + " is automatically set to:"
            + totalPercent);
    return symbolPercent;
  }

  /**
   * invest in weight of each company over a period of time.
   */
  private void case21() {
    int portfolioNum = inputPortfolioNumber();
    if (portfolioNum == 0) {
      return;
    }
    if (portfolioNum <= model.getSizeOfSimplePortfolio()) {
      view.displayErrorMessage("This is not a flexible portfolio number.");
      return;
    }


    HashMap<String, Double> symbolPercent = inputSymbolPercent();


    double money = inputMoney();

    view.displayMessage("Please input start time.");
    LocalDate selectionDate1 = inputTime();
    view.displayMessage("Please input end time.");
    LocalDate selectionDate2 = inputTime();
    int period = inputPeriod();
    try {
      model.weightInvestmentStartToFinish(portfolioNum, symbolPercent, selectionDate1,
              selectionDate2, money, period);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
  }

  /**
   * input period frequency.
   *
   * @return int days
   */
  private int inputPeriod() {
    int period;
    while (true) {
      view.displayMessage("Please input period.");
      try {
        period = in.nextInt();
      } catch (InputMismatchException e) {
        view.displayErrorMessage(e.getMessage());
        continue;
      }
      if (period < 1) {
        view.displayErrorMessage("Cannot use period less than 1 day.");
        continue;
      }
      break;
    }
    return period;
  }
}
