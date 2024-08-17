package view;

/**
 * The {@code View} interface defines the methods that any view class must implement
 * to interact with users in the investment portfolio application.
 */
public interface View {


  /**
   * Displays information about all portfolios.
   *
   * @param portfoliosInfo A formatted string containing information about all portfolios.
   */
  void displayPortfolios(String portfoliosInfo);

  /**
   * Displays detailed information about a specific portfolio.
   *
   * @param portfolioDetails A formatted string containing detailed information
   *                         about a specific portfolio.
   */
  void displayPortfolioDetails(String portfolioDetails);

  /**
   * Displays a generic message to the user.
   *
   * @param message The message to be displayed.
   */
  void displayMessage(String message);

  /**
   * Displays an error message to the user.
   *
   * @param errorMessage The error message to be displayed.
   */
  void displayErrorMessage(String errorMessage);

  /**
   * Prompts the user for input with a specific prompt message and
   * returns the input as a string.
   *
   * @param prompt The prompt message to display to the user.
   */
  void readUserInput(String prompt);


}
