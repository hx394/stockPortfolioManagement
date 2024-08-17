package controller;

import org.w3c.dom.Document;

import java.awt.event.ActionEvent;
import java.io.File;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.InputMismatchException;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import model.Model;
import model.Stock;
import model.SuperPortfolio;
import view.GUI;

/**
 * Implements controller logic for a GUI application managing investment portfolios.
 * It serves as an intermediary between the GUI view and the model,
 * processing user actions and updating the model and view accordingly.
 * Integrates financial data fetching, portfolio management,
 * and user interaction through the GUI, supporting operations like querying data,
 * executing investment strategies, and portfolio management.
 */
public class GuiControllerImpl implements GUIController, Features {
  private static DataSourceAPI api = new AlphaVantageDemo();
  private final Model model;
  private GUI view;
  private String stringInput = "";
  private int portfolioNum;
  private String stockSymbol;
  private int year = 0;
  private int month = 0;
  private int day = 0;
  private String order;
  private LocalDate date;
  private ArrayList<String> symbolList;
  private HashMap<String, Double> symbolPercentList;
  private double moneyAmount;
  private int period = 0;
  private LocalDate endDate;
  private int period2 = 0;

  /**
   * Constructs the controller with a model and an API for data fetching.
   *
   * @param m      The model for data manipulation.
   * @param newApi The API for financial data fetching.
   */
  public GuiControllerImpl(Model m, DataSourceAPI newApi) {
    model = m;
    api = newApi;
  }

  @Override
  public void setView(GUI v) {
    view = v;
    //provide view with all the callbacks
    view.addFeatures(this);
    view.setListener(this);
  }

  @Override
  public void query(int queryNum) {
    switch (queryNum) {
      case 0:
        this.order = "gainOrLoseOnCertainDay";
        break;
      case 1:
        this.order = "gainOrLoseOverPeriod";
        break;
      case 2:
        this.order = "xDayMovingAverage";
        break;
      case 3:
        this.order = "crossoversForPrice";
        break;
      case 4:
        this.order = "movingCrossovers";
        break;
      default:
        view.displayErrorMessage("Not a query that I can understand.");
        view.showMenu();
        cleanUp();
        return;
    }
    inputSymbol(order);
  }

  @Override
  public void queryStatistics() {

    this.order = "query";
    ArrayList<String> selections = new ArrayList<>();
    selections.add("Stock gain or lose on a certain day");
    selections.add("Stock gain or lose over period of time");
    selections.add("Stock x day moving average");
    selections.add("Stock crossovers for price and 30 day moving average");
    selections.add("Stock moving crossovers for x day and y day moving average");
    view.createButtonPanel(order, "selection", selections, this, 0);
  }

  @Override
  public void dollarCostAveraging() {
    this.order = "weightInvestmentOverPeriod";
    inputPortfolioNumber(this.order);
  }

  @Override
  public void weightInvestment() {
    this.order = "weightInvestment";
    inputPortfolioNumber(this.order);
  }

  @Override
  public void loadPortfolio() {
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    Document document = null;

    JFileChooser fileChooser = new JFileChooser();
    File defaultDirectory = new File("./savedPortfolios");
    fileChooser.setCurrentDirectory(defaultDirectory);
    fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    int result = fileChooser.showOpenDialog((JFrame) view);
    File selectedFile;
    if (result == JFileChooser.APPROVE_OPTION) {
      selectedFile = fileChooser.getSelectedFile();
      System.out.println("Selected file: " + selectedFile.getAbsolutePath());
    } else {
      cleanUp();
      view.showMenu();
      return;
    }
    try {
      factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();


      document = builder.parse(selectedFile.getAbsolutePath());
      document.getDocumentElement().normalize();

    } catch (Exception e) {
      view.displayErrorMessage("Cannot load xml files.");
      cleanUp();
      view.showMenu();
      return;
    }
    try {
      model.loadPortfolio(document, "f");
    } catch (Exception e) {
      view.displayErrorMessage(e.toString());
      view.displayErrorMessage("The file have wrong format of json object.");
    }
    cleanUp();
    view.showMenu();
    view.displayMessage("loaded flexible portfolio successfully");
  }

  @Override
  public void savePortfolio() {
    this.order = "savePortfolio";
    inputPortfolioNumber("savePortfolio");
  }

  @Override
  public void viewPortfolioDetails(int portfolioNumber) {

    if (portfolioNumber != 0) {
      String portfolioInfo = model.getSpecificPortfolioInfo(portfolioNumber);
      view.displayPortfolioDetails(portfolioInfo);

    }
    view.removeNewButtonPanel();
    view.showMenu();
    cleanUp();
  }

  @Override
  public void inputNameForSuperPortfolio() {
    view.displayMessage("Please input name for this portfolio:");
    view.createTextField("addSuperPortfolio", "name");
  }

  @Override
  public void addSuperPortfolio() {

    String name = stringInput;
    SuperPortfolio newP = model.addSuperPortfolio();

    newP.setPortfolioName(name);
    view.displayMessage("New flexible portfolio added.");
    view.shutTextField();
    view.showMenu();
    cleanUp();
  }

  @Override
  public void viewAllPortfolios() {
    if (model.getSize() == 0) {
      view.displayMessage("There are no portfolios.");
      view.showMenu();
      cleanUp();
      return;
    }
    String portfolios = model.showAllPortfolios();
    view.displayPortfolios(portfolios);
    view.showMenu();
    cleanUp();
  }

  @Override
  public void valueAtDate() {
    this.order = "valueAtSpecificDate";
    inputPortfolioNumber("valueAtSpecificDate");
  }

  @Override
  public void costBasis() {
    this.order = "costBasis";
    inputPortfolioNumber("costBasis");
  }

  @Override
  public void sellStock() {

    this.order = "sellStock";
    inputPortfolioNumber("sellStock");
  }

  @Override
  public void buyStock() {
    this.order = "buyStock";
    inputPortfolioNumber("buyStock");
  }

  @Override
  public void getStringInput() {


    String input = view.getInputString();
    if (!input.trim().isEmpty()) { // Check if the input is not empty
      stringInput = input;

    } else {
      view.displayErrorMessage("input string cannot be empty");
    }
  }

  @Override
  public void savePortfolioNum(int portfolioNum) {
    this.portfolioNum = portfolioNum;
  }

  @Override
  public void inputSymbol(String order) {

    view.displayMessage("Please input stock symbol, q for quit:");
    view.createTextField(order, "stockSymbol");

  }

  @Override
  public void inputSymbolList() {
    symbolList = new ArrayList<>();
    inputSymbol(this.order);
  }

  @Override
  public void inputPortfolioNumber(String order) {
    if (model.getSize() == 0) {
      view.displayMessage("There are no portfolios.");
      view.showMenu();
      cleanUp();
      return;
    }
    if (model.getSizeOfSuperPortfolio() == 0) {
      if (order.equals("buyStock")
              || order.equals("sellStock")
              || order.equals("costBasis")
              || order.equals("valueAtSpecificDate")
              || order.equals("weightInvestment")
              || order.equals("weightInvestmentOverPeriod")) {
        view.displayMessage("There are no flexible portfolios.");
        view.showMenu();
        cleanUp();
        return;
      }
    }
    int portfolioNumber = 0;
    String type;
    if (order.equals("buyStock")
            || order.equals("sellStock")
            || order.equals("costBasis")
            || order.equals("valueAtSpecificDate")
            || order.equals("weightInvestment")
            || order.equals("weightInvestmentOverPeriod")) {
      type = "superPortfolioNumber";
      view.displayMessage("Choose flexible portfolio:");

      ArrayList<String> portfolioList = model.getMySuperInvestments();
      int countOfSimple = model.getMyInvestments().size();

      view.createButtonPanel(order, type, portfolioList, this, countOfSimple);
    } else {
      type = "portfolioNumber";
      view.displayPortfolios(model.showAllPortfolios() + "\nChoose portfolio: ");

      ArrayList<String> portfolioList = model.getMyInvestments();
      int countOfSimple = portfolioList.size();
      portfolioList.addAll(model.getMySuperInvestments());
      view.createButtonPanel(order, type, portfolioList, this, countOfSimple);
    }
  }

  @Override
  public void savePortfolioDetails(int portfolioNum) {
    boolean saved = model.save(portfolioNum);
    if (saved) {
      view.displayMessage("saved successfully");
      view.removeNewButtonPanel();
      cleanUp();
      view.showMenu();
    } else {
      view.displayErrorMessage("save fails.");
      view.removeNewButtonPanel();
      cleanUp();
      view.showMenu();
    }
  }

  @Override
  public void inputMoney() {
    view.displayMessage("Please input money amount for single investment:");
    view.createTextField(order, "money");
  }

  @Override
  public void setSymbolPercentList(HashMap<String, Double> hm) {
    this.symbolPercentList = hm;
  }

  /**
   * Initiates the input process for specifying percentage allocations
   * for a list of stock symbols.
   * Clears the current display, disables the text field to prevent input,
   * and creates multiple text fields
   * for entering percentages associated with each stock symbol.
   * This method is used in scenarios such as weighted investment
   * where users allocate a specific percentage of total investment to different stocks.
   */
  private void inputPercent() {
    view.clearDisplay();
    view.shutTextField();
    view.createMultipleTextField(order, symbolList, "percent", this);
  }

  @Override
  public void setYear(int year) {
    this.year = year;
  }

  @Override
  public void setMonth(int month) {
    this.month = month;
  }

  @Override
  public void setDay(int day) {
    this.day = day;
  }

  @Override
  public void setDate() {
    String m = month + "";
    if (m.length() == 1) {
      m = 0 + m;
    }
    String d = day + "";
    if (d.length() == 1) {
      d = 0 + d;
    }
    String y = year + "";
    while (y.length() < 4) {
      y = 0 + y;
    }
    if (date == null) {
      this.date = LocalDate.parse(y + "-" + m + "-" + d);
    } else if (endDate == null) {
      this.endDate = LocalDate.parse(y + "-" + m + "-" + d);
    }
  }

  /**
   * helper function to get portfolioNumber.
   * return boolean if success or not
   */
  private boolean getPortfolioNum() {
    if (view.getTextType().equals("portfolioNumber")) {
      int portfolioNumber;
      try {
        portfolioNumber = Integer.parseInt(view.getInputString());
      } catch (InputMismatchException ex) {
        view.displayErrorMessage("No valid input for portfolio number.");
        return false;
      }
      view.displayMessage("Input number is:" + portfolioNumber);
      if (portfolioNumber > model.getSize() || portfolioNumber <= 0) {
        view.displayErrorMessage("Invalid portfolio number.");
        return false;
      }
      this.portfolioNum = portfolioNumber;

    }
    return true;
  }

  /**
   * helper function to get stock symbol.
   *
   * @return true if passed, false if need to return.
   */
  private boolean getStockSymbol() {
    if (view.getTextType().equals("stockSymbol")) {
      saveStockSymbol();
      if (stockSymbol.equals("q")) {
        if ((order.equals("weightInvestment")
                || order.equals("weightInvestmentOverPeriod"))
                && symbolList.size() > 0) {
          inputPercent();
          return false;
        } else {
          view.shutTextField();
          view.clearDisplay();
          view.showMenu();
          cleanUp();
          return false;
        }
      } else {
        this.stockSymbol = this.stockSymbol.toUpperCase();
      }
      String data = this.getData(this.stockSymbol);
      String found = "success";
      if (data == null) {
        found = this.storeData(this.stockSymbol);
      }
      boolean helper = helperGetSymbol(found);
      return helper;
    }
    return true;
  }

  /**
   * helper of helper to get symbol.
   *
   * @param found the result of get and store data
   * @return boolean what to do
   */
  private boolean helperGetSymbol(String found) {
    if (found == null || !found.equals("success")) {
      view.displayErrorMessage("Ticker symbol not found.\n" + found);
      if (order.equals("weightInvestment")
              || order.equals("weightInvestmentOverPeriod")) {
        view.displayMessage(view.getDisplayMessage() + "\nPlease input stock symbol.");
        return false;
      }
      cleanUp();
      view.shutTextField();
      view.showMenu();
      return false;
    } else {
      view.displayMessage("Stock symbol is:" + this.stockSymbol);
      view.shutTextField();
      if (order.equals("weightInvestment")
              || order.equals("weightInvestmentOverPeriod")) {
        if (!symbolList.contains(this.stockSymbol)) {
          symbolList.add(this.stockSymbol);
        }
        this.stockSymbol = null;
        view.shutTextField();
        inputSymbol(order);
        return false;
      }
    }
    return true;
  }

  /**
   * helper function to get shares.
   *
   * @return boolean depending on situation
   */
  private boolean getShares() {
    int shares;
    if (view.getTextType().equals("shares")) {
      try {
        shares = Integer.parseInt(view.getInputString());
      } catch (NumberFormatException ex) {
        view.displayErrorMessage("this is not a integer");
        inputShares();
        return false;
      }
      if (shares <= 0) {
        view.displayErrorMessage("Cannot buy 0 or less shares");
        inputShares();
        return false;
      }
      int response = compareSharesWithVolume(stockSymbol, shares, date);
      if (response == 1) {
        view.shutTextField();
        view.showMenu();
        cleanUp();
        return false;
      } else if (response == 2) {
        view.shutTextField();
        view.showMenu();
        cleanUp();
        return false;

      } else if (response == 3) {
        return helperResponse3(shares);
      }
    }
    return true;
  }

  /**
   * helper of helper get shares.
   *
   * @param shares shares
   * @return boolean false always
   */
  private boolean helperResponse3(int shares) {
    try {
      if (this.portfolioNum != 0 && order.equals("buyStock")) {
        model.superPortfolioAddStock(portfolioNum,
                this.stockSymbol, shares, this.date);
      } else if (this.portfolioNum != 0 && order.equals("sellStock")) {
        model.superPortfolioSellStock(portfolioNum,
                this.stockSymbol, shares, this.date);
      }
    } catch (IllegalArgumentException ex) {
      view.displayErrorMessage(ex.getMessage());
      cleanUp();
      view.shutTextField();
      view.showMenu();
      return false;
    }
    view.displayMessage("successfully " + order);
    cleanUp();
    view.shutTextField();
    view.showMenu();
    return false;
  }

  /**
   * helper function to get money.
   *
   * @return true or false
   */
  private boolean getMoney() {
    double money;
    if (view.getTextType().equals("money")) {
      try {
        money = Double.parseDouble(view.getInputString());
        if (money <= 0) {
          view.displayErrorMessage(
                  "Cannot be zero. \nPlease input suitable money amount.");
          return false;
        }
      } catch (NumberFormatException ex) {
        view.displayErrorMessage("Not a double.\nPlease input money as double.");
        return false;
      }
      this.moneyAmount = money;
      view.shutTextField();
    }
    return true;
  }

  /**
   * get period helper function.
   *
   * @return boolean depending on situation
   */
  private boolean getPeriod() {
    if (view.getTextType().equals("period")) {
      setPeriod();

      if (!helperGetPeriodxDayMovingAverage()) {
        return false;
      }
      if (!helperGetPeriodMovingCrossovers()) {
        return false;
      }
      return helperGetPeriodweightInvestmentOverPeriod();
    }
    return true;
  }

  /**
   * helper on period weight investment over period.
   *
   * @return boolean depending
   */
  private boolean helperGetPeriodweightInvestmentOverPeriod() {
    if (order.equals("weightInvestmentOverPeriod")) {
      try {
        model.weightInvestmentStartToFinish(portfolioNum,
                this.symbolPercentList, this.date,
                this.endDate, this.moneyAmount, this.period);
        view.displayMessage("successfully invested.");
      } catch (IllegalArgumentException ex) {
        view.displayErrorMessage(ex.getMessage());

      }
      view.shutTextField();
      view.showMenu();
      cleanUp();
      return false;
    }
    return true;
  }

  /**
   * helper on period x day moving average.
   *
   * @return boolean depending
   */
  private boolean helperGetPeriodxDayMovingAverage() {
    if (order.equals("xDayMovingAverage")) {
      try {
        String result = "The " + this.period + " days moving average for stock "
                + this.stockSymbol + " till time "
                + this.date + " is:";
        view.displayMessage(result + model.stockXDayMovingAverage(this.stockSymbol,
                this.date, this.period));

      } catch (IllegalArgumentException ex) {
        view.displayErrorMessage(ex.getMessage());
      }
      view.shutTextField();
      view.showMenu();
      cleanUp();
      return false;
    }
    return true;
  }

  /**
   * helper on period moving crossovers.
   *
   * @return boolean depending
   */
  private boolean helperGetPeriodMovingCrossovers() {
    if (order.equals("movingCrossovers")) {
      if (this.period2 == 0) {
        inputPeriod();
        return false;
      } else {
        try {
          String result = "The moving crossovers for stock" + this.stockSymbol
                  + " from " + this.date + " till "
                  + this.endDate
                  + " for " + this.period + " days, " + this.period2
                  + " days " + " listed:"
                  + System.lineSeparator();
          view.displayMessage(result + model.stockMovingCrossovers(this.stockSymbol,
                  this.date, this.endDate, this.period, this.period2));

        } catch (IllegalArgumentException ex) {
          view.displayErrorMessage(ex.getMessage());
        }
        view.shutTextField();
        view.showMenu();
        cleanUp();
        return false;
      }
    }
    return true;
  }

  @Override
  public void actionPerformed(ActionEvent e) {

    this.order = e.getActionCommand();

    String input = view.getInputString();
    if (!input.trim().isEmpty()) {
      this.getStringInput();
      boolean successOfPortfolioNum = getPortfolioNum();
      if (!successOfPortfolioNum) {
        return;
      }
      if (!getStockSymbol()) {
        return;
      }
      if (!getShares()) {
        return;
      }
      if (!getMoney()) {
        return;
      }
      if (!getPeriod()) {
        return;
      }
      switchCaseForCommand(e);

    } else {
      view.displayErrorMessage("Cannot use empty name.\nPlease input name for this portfolio:");
    }
  }

  /**
   * switch case for command.
   *
   * @param e Action event the order
   */
  private void switchCaseForCommand(ActionEvent e) {
    switch (e.getActionCommand()) {
      case "addSuperPortfolio":
        this.addSuperPortfolio();
        break;
      case "viewPortfolioDetail":
        this.viewPortfolioDetails(portfolioNum);
        break;
      case "buyStock":
      case "sellStock":
        if (date == null) {
          this.inputTime();
        }
        break;
      case "weightInvestment":
      case "weightInvestmentOverPeriod":
      case "gainOrLoseOnCertainDay":
      case "gainOrLoseOverPeriod":
      case "xDayMovingAverage":
      case "crossoversForPrice":
      case "movingCrossovers":
        inputTime();
        break;
      default:
        view.displayErrorMessage("No setting for this button.");
    }
  }

  /**
   * Resets all member variables to their default state.
   * This method is typically called after completing an operation to ensure
   * the controller starts fresh for the next user interaction.
   * It clears inputs related to portfolio management, stock transactions,
   * and date selections.
   */
  private void cleanUp() {
    //System.out.println("cleanUP called");
    this.portfolioNum = 0;
    this.order = null;
    this.year = 0;
    this.month = 0;
    this.stockSymbol = null;
    this.stringInput = null;
    this.date = null;
    this.day = 0;
    this.symbolList = null;
    this.symbolPercentList = null;
    this.moneyAmount = 0;
    this.period = 0;
    this.period2 = 0;
    this.endDate = null;
  }

  /**
   * Compares the requested number of shares to buy with
   * the available volume for a given stock symbol on a specific date.
   * It ensures that the buying request does not exceed the stock's trading volume,
   * providing feedback to the user through the view's display methods.
   *
   * @param symbol The stock symbol.
   * @param shares The number of shares intended to buy.
   * @param time   The date for which the volume is checked.
   * @return An integer code representing the outcome:
   *          1 for errors or exceeding volume,
   *          2 if volume is 0 (cannot buy), and
   *          3 for a valid purchase request.
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

  @Override
  public void saveStockSymbol() {
    this.stockSymbol = view.getInputString();
  }

  /**
   * helper for year=0.
   */
  private void caseYear0() {
    view.displayMessage("please select year");

    int currentYear = LocalDate.now().getYear();
    ArrayList<String> arrayList = new ArrayList<>();
    int lowerBoundary = currentYear - 30;
    for (int i = currentYear; i > lowerBoundary; i--) {
      arrayList.add(i + "");
    }

    view.createButtonPanel(order, "year", arrayList, this, 0);
  }

  /**
   * helper for month=0.
   */
  private void caseMonth0() {
    view.displayMessage("please select month");
    ArrayList<String> arrayList = new ArrayList<>();
    for (int i = 1; i <= 12; i++) {
      arrayList.add(i + "");
    }
    view.createButtonPanel(order, "month", arrayList, this, 0);
  }

  /**
   * case for day=0.
   */
  private void caseDay0() {
    view.displayMessage("please select day");
    ArrayList<String> arrayList = new ArrayList<>();
    YearMonth yearMonth = YearMonth.of(year, month);
    int daysInMonth = yearMonth.lengthOfMonth();
    for (int i = 1; i <= daysInMonth; i++) {
      arrayList.add("" + i);
    }
    view.createButtonPanel(order, "day", arrayList, this, 0);
  }

  @Override
  public void inputTime() {
    //System.out.println(year+" "+month+" "+day);

    if (year == 0) {
      caseYear0();
    } else if (month == 0) {
      caseMonth0();
    } else if (day == 0) {
      caseDay0();
    } else {
      setDate();

      year = 0;
      month = 0;
      day = 0;
      view.displayMessage("Input date is:" + date);
      if (switchCaseForCommandForSetDate()) {
        return;
      }
    }
    if ((order.equals("weightInvestmentOverPeriod") || order.equals("movingCrossovers")
            || order.equals("crossoversForPrice") || order.equals("gainOrLoseOverPeriod"))
            && date == null) {
      view.displayMessage(view.getDisplayMessage() + "\nPlease input for start time.");
    }
    if ((order.equals("weightInvestmentOverPeriod") || order.equals("movingCrossovers")
            || order.equals("crossoversForPrice") || order.equals("gainOrLoseOverPeriod"))
            && date != null) {
      view.displayMessage(view.getDisplayMessage() + "\nPlease input for end time.");
    }
  }

  /**
   * switch case helper function.
   *
   * @return boolean to tell what to do
   */
  private boolean switchCaseForCommandForSetDate() {
    switch (order) {
      case "buyStock":
      case "sellStock":
        this.inputShares();
        return true;
      case "costBasis":
        caseCostBasisSetDate();
        return true;
      case "valueAtSpecificDate":
        caseValueAtSpecificDateSetDate();
        return true;
      case "weightInvestment":
        caseWeightInvestmentSetDate();
        return true;

      case "weightInvestmentOverPeriod":
        if (endDate == null) {
          this.askInputTime();
          return true;
        } else {
          this.inputPeriod();
          return true;
        }
      case "gainOrLoseOnCertainDay":
        caseGainOrLoseOnCertainDateSetDate();
        return true;

      case "gainOrLoseOverPeriod":
        caseGainOrLoseOverPeriodSetDate();
        return true;
      case "xDayMovingAverage":
        this.inputPeriod();
        return true;
      case "crossoversForPrice":
        caseCrossoverSetDate();
        return true;
      case "movingCrossovers":
        if (endDate == null) {
          inputTime();
          return true;
        } else {
          inputPeriod();
          return true;
        }
      default:
        return false;
    }
  }

  /**
   * case helper function.
   */
  private void caseWeightInvestmentSetDate() {
    try {
      model.weightInvestment(portfolioNum, symbolPercentList, date, moneyAmount);
      view.displayMessage("Successfully weight investing.");
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
    cleanUp();
    view.showMenu();
  }

  /**
   * case helper function.
   */
  private void caseValueAtSpecificDateSetDate() {
    try {
      double value = model.superPortfolioTimeBoundaryValue(portfolioNum, date);
      view.displayMessage("Value at time " + date + " for flexible portfolio "
              + portfolioNum + " is:"
              + value);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
    cleanUp();
    view.showMenu();
  }

  /**
   * case helper function.
   */
  private void caseCostBasisSetDate() {
    try {
      double costBasis = model.superPortfolioCostBasis(portfolioNum, date);
      view.displayMessage("The cost basis for flexible portfolio "
              + portfolioNum + " at time "
              + date
              + " is:" + costBasis);
    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
    cleanUp();
    view.showMenu();
  }

  /**
   * case helper function.
   */
  private void caseGainOrLoseOnCertainDateSetDate() {
    try {
      view.displayMessage(model.stockGainOrLoseOnGivenDay(this.stockSymbol, this.date));

    } catch (IllegalArgumentException e) {
      view.displayErrorMessage(e.getMessage());
    }
    cleanUp();
    view.showMenu();
  }

  /**
   * case helper function.
   */
  private void caseGainOrLoseOverPeriodSetDate() {
    if (endDate == null) {
      inputTime();

    } else {
      try {
        view.displayMessage(model.stockGainOrLoseOverPeriodOfTime(this.stockSymbol,
                this.date, this.endDate));
      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }
      view.showMenu();
      cleanUp();
    }
  }

  /**
   * case helper function.
   */
  private void caseCrossoverSetDate() {
    if (endDate == null) {
      this.inputTime();
    } else {
      try {
        String result = "The crossovers for stock" + this.stockSymbol + " from " + this.date
                + " till " + this.endDate
                + " listed:"
                + System.lineSeparator();
        view.displayMessage(result + model.stockCrossovers(this.stockSymbol,
                this.date, this.endDate));

      } catch (IllegalArgumentException e) {
        view.displayErrorMessage(e.getMessage());
      }
      view.showMenu();
      cleanUp();
    }
  }

  @Override
  public void askInputTime() {
    if (order.equals("weightInvestmentOverPeriod") && date != null && endDate == null) {
      view.displayMessage("Do you want continuous investment?\nNot setting end date?");
      ArrayList<String> list = new ArrayList<>();
      list.add("yes");
      list.add("no");
      view.createButtonPanel(order, "YesNo", list, this, 0);
    }

  }

  @Override
  public void chooseYesNo(int num) {
    if (num == 0) {
      endDate = LocalDate.now();
      inputPeriod();
    } else if (num == 1) {
      inputTime();
    }
  }

  @Override
  public void inputPeriod() {
    view.shutTextField();
    view.displayMessage("please input period as days:");
    if (order.equals("movingCrossovers")) {
      if (period == 0) {
        view.displayMessage("Please input x days:");
      } else {
        view.displayMessage("Please input y days:");
      }
    }
    view.createTextField(order, "period");
  }

  @Override
  public void setPeriod() {
    try {
      int p = Integer.parseInt(view.getInputString());
      if (p <= 0) {
        view.displayErrorMessage("Cannot use period less than 1 day.\nPlease input period:");
        return;
      }
      if (this.period == 0) {
        this.period = p;
      } else if (this.period2 == 0) {
        this.period2 = p;
      }
    } catch (NumberFormatException e) {
      view.displayMessage("this is not integer.\nPlease input days as integer:");
    }
  }

  @Override
  public void inputShares() {
    view.shutTextField();
    view.displayMessage("please input shares number:");

    view.createTextField(order, "shares");
  }

  /**
   * Store data into csv files by having a stock symbol.
   * calls to  api as of now.
   * but can handle more sources in the future.
   *
   * @param stock stock symbol
   * @return String success or other
   */
  public String storeData(String stock) {

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
  public String getData(String stock) {
    String s;
    try {

      s = api.getData(stock);
    } catch (Exception e) {
      view.displayErrorMessage(e.toString());
      return null;
    }
    return s;
  }
}
