package view;

import java.io.PrintStream;

/**
 * The {@code ViewImp} class provides a command-line user interface for
 * the investment portfolio application.
 * This class implements all methods defined in the {@code View} interface to
 * interact with the user, displaying menus, messages, and collecting user inputs.
 */
public class ViewImp implements TextView {
  private final PrintStream out;

  /**
   * Constructs a new {@code ViewImp} instance with a {@code Scanner}
   * for reading user input from the command line.
   *
   * @param out the printStream
   */
  public ViewImp(PrintStream out) {
    // Note: All necessary initializations are assumed to be handled outside of this constructor
    // or not required at this stage.
    this.out = out;
  }

  /**
   * Displays the main menu with options for the user to choose from.
   */
  @Override
  public void showMenu() {
    this.out.println("\nPlease select an option:");
    this.out.println("1. Create a new simple portfolio");
    this.out.println("2. Create a flexible portfolio");
    this.out.println("3. View all portfolios");
    this.out.println("4. View specific portfolio details");
    this.out.println("5. Add stock to a flexible portfolio");
    this.out.println("6. Sell stock of a flexible portfolio");
    this.out.println("7. View cost basis of a flexible portfolio");
    this.out.println("8. View value of a flexible portfolio by date");
    this.out.println("9. Get value of simple portfolio at certain date");
    this.out.println("10. Sell a simple portfolio or Delete a flexible portfolio");
    this.out.println("11. Update portfolio values");
    this.out.println("12. save a portfolio");
    this.out.println("13. load a portfolio");
    this.out.println("14. Stock gain or lose on a certain day");
    this.out.println("15. Stock gain or lose over period of time");
    this.out.println("16. Stock x day moving average");
    this.out.println("17. Stock crossovers for price and 30 day moving average");
    this.out.println("18. Stock moving crossovers for x day and y day moving average");
    this.out.println("19. Generate value chart either for a stock or a portfolio");
    this.out.println("20. Weight Investment on specific day");
    this.out.println("21. Weight investment on given period with frequency");
    this.out.println("22. Exit");
  }

  /**
   * Prompts the user to choose an option from the main menu and returns the user's choice.
   */
  @Override
  public void getUserChoice() {
    this.out.print("Enter your choice: ");
  }

  /**
   * Displays information about all portfolios.
   *
   * @param portfoliosInfo A formatted string containing information about all portfolios.
   */
  @Override
  public void displayPortfolios(String portfoliosInfo) {
    this.out.println("\nAll Portfolios:");
    this.out.println(portfoliosInfo);
  }

  /**
   * Displays detailed information about a specific portfolio.
   *
   * @param portfolioDetails A formatted string containing detailed information
   *                         about a specific portfolio.
   */
  @Override
  public void displayPortfolioDetails(String portfolioDetails) {
    this.out.println("\nPortfolio Details:");
    this.out.println(portfolioDetails);
  }

  /**
   * Displays a generic message to the user.
   *
   * @param message The message to be displayed.
   */
  @Override
  public void displayMessage(String message) {
    this.out.println(message);
  }

  /**
   * Displays an error message to the user.
   *
   * @param errorMessage The error message to be displayed.
   */
  @Override
  public void displayErrorMessage(String errorMessage) {
    this.out.println("Error: " + errorMessage);
  }

  /**
   * Prompts the user for input with a specific prompt message and
   * returns the input as a string.
   *
   * @param prompt The prompt message to display to the user.
   */
  @Override
  public void readUserInput(String prompt) {
    this.out.print(prompt);
  }
}
