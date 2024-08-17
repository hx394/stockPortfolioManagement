import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import view.ViewImp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Provides unit tests for {@link ViewImp} to verify its message display functionalities,
 * including menus, error messages, and user prompts.
 */
public class ViewImpTest {
  private final PrintStream originalOut = System.out;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private ViewImp view;

  /**
   * Sets up the test environment by redirecting System.out to capture output for analysis.
   */
  @Before
  public void setUpStreams() {
    System.setOut(new PrintStream(outContent));
    view = new ViewImp(System.out);
  }

  /**
   * Restores System.out after tests are done to avoid affecting other tests.
   */
  @After
  public void restoreStreams() {
    System.setOut(originalOut);
  }

  /**
   * Tests if the main menu is displayed correctly.
   */
  @Test
  public void testShowMenu() {
    view.showMenu();
    String expectedOutput = "Please select an option:\n"
            + "1. Create a new simple portfolio\n"
            + "2. Create a flexible portfolio\n"
            + "3. View all portfolios\n"
            + "4. View specific portfolio details\n"
            + "5. Add stock to a flexible portfolio\n"
            + "6. Sell stock of a flexible portfolio\n"
            + "7. View cost basis of a flexible portfolio\n"
            + "8. View value of a flexible portfolio by date\n"
            + "9. Get value of simple portfolio at certain date\n"
            + "10. Sell a simple portfolio or Delete a flexible portfolio\n"
            + "11. Update portfolio values\n"
            + "12. save a portfolio\n"
            + "13. load a portfolio\n"
            + "14. Stock gain or lose on a certain day\n"
            + "15. Stock gain or lose over period of time\n"
            + "16. Stock x day moving average\n"
            + "17. Stock crossovers for price and 30 day moving average\n"
            + "18. Stock moving crossovers for x day and y day moving average\n"
            + "19. Generate value chart either for a stock or a portfolio\n"
            + "20. Exit\n";
    assertEquals(
            "Menu should display all options correctly.",
            expectedOutput.trim(),
            outContent.toString().replace("\r\n", "\n").trim());
  }

  /**
   * Verifies that generic messages are displayed correctly.
   */
  @Test
  public void testDisplayMessage() {
    String testMessage = "This is a test message.";
    view.displayMessage(testMessage);
    assertEquals(
            "DisplayMessage should output the correct message.",
            testMessage,
            outContent.toString().trim());
  }

  /**
   * Checks the proper display of error messages.
   */
  @Test
  public void testDisplayErrorMessage() {
    String errorMessage = "This is a test error message.";
    view.displayErrorMessage(errorMessage);
    assertEquals(
            "DisplayErrorMessage should output the correct error message.",
            "Error: "
                    + errorMessage, outContent.toString().trim());
  }

  /**
   * Validates handling of displaying an empty message.
   */
  @Test
  public void testDisplayEmptyMessage() {
    view.displayMessage("");
    assertEquals(
            "DisplayMessage with empty string should produce no output.",
            "",
            outContent.toString().trim());
  }

  /**
   * Ensures null messages are handled gracefully, displaying 'null'.
   */
  @Test
  public void testDisplayNullMessage() {
    view.displayMessage(null);
    assertEquals(
            "DisplayMessage with null should produce 'null' output.",
            "null",
            outContent.toString().trim());
  }

  /**
   * Confirms that the user input prompt displays correctly.
   */
  @Test
  public void testUserInputPrompt() {
    String prompt = "Please enter a number:";
    view.readUserInput(prompt);
    assertTrue(
            "ReadUserInput should display the correct prompt.",
            outContent.toString().trim().endsWith(prompt));
  }

  /**
   * Tests display of portfolio details when the details string is empty.
   */
  @Test
  public void testDisplayPortfolioDetailsEmpty() {
    view.displayPortfolioDetails("");
    assertEquals(
            "DisplayPortfolioDetails with empty details should still display the header.",
            "Portfolio Details:",
            outContent.toString().trim());
  }

  /**
   * Ensures that displaying an empty portfolios info still shows the title.
   */
  @Test
  public void testDisplayPortfoliosEmpty() {
    view.displayPortfolios("");
    assertTrue(
            "DisplayPortfolios with empty info should still show the title.",
            outContent.toString().trim().contains("All Portfolios:"));
  }

  /**
   * Validates that an empty error message still shows the error prefix.
   */
  @Test
  public void testDisplayErrorMessageEmpty() {
    view.displayErrorMessage("");
    assertEquals(
            "DisplayErrorMessage with empty message should output 'Error: ' prefix only.",
            "Error: ",
            outContent.toString().trim() + " ");
  }

  /**
   * Checks if the show menu and user choice prompt are displayed sequentially.
   */
  @Test
  public void testShowMenuAndUserChoicePrompt() {
    view.showMenu();
    view.getUserChoice();
    assertTrue(
            "The output should contain menu followed by user choice prompt.",
            outContent.toString().contains("Please select an option:")
                    && outContent.toString().contains("Enter your choice:"));
  }
}
