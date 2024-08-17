package controller;

import java.util.HashMap;

/**
 * The Features interface defines a set of operations
 * that can be performed by a controller within an application.
 * These operations encompass a wide range of functionalities including
 * querying data, executing investment strategies, managing portfolios,
 * and interacting with the user for input and displaying information.
 */
public interface Features {
  /**
   * Executes a specific query based on a query number.
   *
   * @param queryNum the number identifying the specific query to execute.
   */
  void query(int queryNum);

  /**
   * Queries and displays statistical information.
   */
  void queryStatistics();

  /**
   * Performs dollar cost averaging strategy.
   */
  void dollarCostAveraging();

  /**
   * Executes a weighted investment strategy.
   */
  void weightInvestment();

  /**
   * Loads a portfolio from persistent storage.
   */
  void loadPortfolio();

  /**
   * Saves a portfolio to persistent storage.
   */
  void savePortfolio();

  /**
   * Displays details of a specific portfolio.
   *
   * @param portfolioNumber the identifier of the portfolio to view.
   */
  void viewPortfolioDetails(int portfolioNumber);

  /**
   * Prompts for and inputs a name for a new super portfolio.
   */
  void inputNameForSuperPortfolio();

  /**
   * Adds a new super portfolio.
   */
  void addSuperPortfolio();

  /**
   * Displays all portfolios.
   */
  void viewAllPortfolios();

  /**
   * Queries the value of a portfolio at a specific date.
   */
  void valueAtDate();

  /**
   * Calculates and displays the cost basis of a portfolio.
   */
  void costBasis();

  /**
   * Sells stock from a portfolio.
   */
  void sellStock();

  /**
   * Buys stock for a portfolio.
   */
  void buyStock();

  /**
   * Retrieves a string input from the user.
   */
  void getStringInput();

  /**
   * Saves the specified portfolio number for later use.
   *
   * @param portfolioNum the portfolio number to save.
   */
  void savePortfolioNum(int portfolioNum);

  /**
   * Inputs a stock symbol for either buying or selling order.
   *
   * @param order the order type ("buy" or "sell").
   */
  void inputSymbol(String order);

  /**
   * Inputs a list of stock symbols for investment.
   */
  void inputSymbolList();

  /**
   * Prompts for and inputs a portfolio number.
   *
   * @param order the context of the portfolio number request.
   */
  void inputPortfolioNumber(String order);

  /**
   * Sets the month for a date input.
   *
   * @param month the month to set.
   */
  void setMonth(int month);

  /**
   * Sets the day for a date input.
   *
   * @param day the day to set.
   */
  void setDay(int day);

  /**
   * Saves the details of a portfolio.
   *
   * @param portfolioNum the portfolio number whose details are to be saved.
   */
  void savePortfolioDetails(int portfolioNum);

  /**
   * Inputs the amount of money for investment.
   */
  void inputMoney();

  /**
   * Sets the percentage list of symbols for weighted investment.
   *
   * @param hm a hashmap containing symbols and their respective weights.
   */
  void setSymbolPercentList(HashMap<String, Double> hm);

  /**
   * Sets the year for a date input.
   *
   * @param year the year to set.
   */
  void setYear(int year);

  /**
   * Sets the complete date for an operation.
   */
  void setDate();

  /**
   * Saves a stock symbol for later use.
   */
  void saveStockSymbol();

  /**
   * Inputs a time for scheduling operations.
   */
  void inputTime();

  /**
   * Prompts the user to input a time.
   */
  void askInputTime();

  /**
   * Allows the user to choose between "Yes" or "No".
   *
   * @param num the number corresponding to the choice.
   */
  void chooseYesNo(int num);

  /**
   * Inputs the period frequency for an operation.
   */
  void inputPeriod();

  /**
   * Sets the period for an operation.
   */
  void setPeriod();

  /**
   * Inputs the number of shares for a stock operation.
   */
  void inputShares();
}

