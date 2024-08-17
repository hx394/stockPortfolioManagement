package view;

/**
 * The {@code TextView} interface provides a command-line user interface for
 * the investment portfolio application.
 * This class extends all methods defined in the {@code View} interface to
 * interact with the user, displaying menus, messages, and collecting user inputs.
 */
public interface TextView extends View {
  /**
   * Displays the main menu with options for the user to choose from.
   */
  void showMenu();

  /**
   * Prompts the user to choose an option from the main menu and returns the user's choice.
   */
  void getUserChoice();
}
