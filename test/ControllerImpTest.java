import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.time.LocalDate;

import controller.AlphaVantageDemo;
import controller.Controller;
import controller.ControllerImp;
import model.Model;
import model.ModelImp;
import view.TextView;
import view.ViewImp;

import static org.junit.Assert.assertTrue;

/**
 * Test suite for the ControllerImp class.
 * Validates the integration and functionality of controller operations.
 */
public class ControllerImpTest {
  private final PrintStream originalOut = System.out;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private Model model;
  private TextView view;

  /**
   * Set up common test fixtures.
   * Initializes model and view, and redirects system output to capture results.
   */
  @Before
  public void setUp() {
    model = new ModelImp();
    view = new ViewImp(new PrintStream(outContent));
  }


  /**
   * Simulates user input and runs the controller to process the input.
   *
   * @param inputData A string representing user input.
   */
  private void simulateInputAndRunController(String inputData) {
    ByteArrayInputStream inContent = new ByteArrayInputStream(inputData.getBytes());
    Controller controller = new ControllerImp(model, view, inContent, new PrintStream(outContent),
            new AlphaVantageDemo());
    controller.toGo();
  }

  /**
   * Tests adding a new portfolio through the controller.
   */
  @Test
  public void testAddingNewPortfolio() {
    String input = "1\nPortfolio1\nq\n22\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    assertTrue(output.contains("New portfolio added"));
  }

  /**
   * Tests viewing all portfolios after adding a new one.
   */
  @Test
  public void testViewAllPortfoliosAfterAdding() {
    String input = "1\nPortfolio1\nq\n3\n22\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    assertTrue(output.contains("All Portfolios"));
    assertTrue(output.contains("Portfolio1"));
  }

  /**
   * Tests the process of selling a portfolio through the controller.
   */
  @Test
  public void testSellingAPortfolio() {
    String input = "1\nPortfolioToSell\nq\n10\n1\n22\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    assertTrue(output.contains("Portfolio sold"));
  }

  /**
   * Tests updating all portfolios through the controller.
   */
  @Test
  public void testUpdatingPortfolios() {
    String input = "11\n22\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    assertTrue(output.contains("All portfolios updated")
            || output.contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));
  }

  /**
   * Tests the handling of an invalid menu option.
   */
  @Test
  public void testInvalidOption() {
    String input = "999\n22\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    assertTrue(output.contains("Invalid option"));
    String input2 = "a\n22\n";
    simulateInputAndRunController(input2);
    output = outContent.toString().trim();
    assertTrue(output.contains("No valid input."));
  }

  /**
   * Tests adding a portfolio with an invalid stock symbol.
   */
  @Test
  public void testInvalidStockSymbolInPortfolio() {
    String input = "1\nInvalidPortfolio\nINVALIDSYMBOL\nq\n22\n";
    simulateInputAndRunController(input);
    assertTrue(outContent.toString().contains("Ticker symbol not found")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));
  }

  /**
   * Tests having negative,0,or more than volume shares.
   */
  @Test
  public void testInvalidNumberOfShares() {
    String input = "1\nInvalidPortfolio\nIBM\n0\n100\n2024-03-12\nq\n22\n";
    simulateInputAndRunController(input);

    boolean condition = outContent.toString()
            .contains("Error: cannot buy or sell 0 or less share of a company.")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day.");

    assertTrue(condition);


    String input3 = "1\nInvalidPortfolio\nIBM\n" + Long.MAX_VALUE + "\n2024-03-12\n100"
            + "\n2024-03-12\nq\n22\n";
    simulateInputAndRunController(input3);
    assertTrue(outContent.toString().contains("Maximum exceeded:")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));
  }

  /**
   * Tests having multiple insertion of same company.
   */
  @Test
  public void testDoubleInsertionOfSymbol() {
    String input = "1\nInvalidPortfolio\nIBM\n10\n2024-03-12\nIBM\nq\n22\n";
    simulateInputAndRunController(input);

    assertTrue(outContent.toString().contains("This company is already recorded.")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));

  }


  /**
   * Tests the successful saving of a portfolio.
   */
  @Test
  public void testSuccessfulPortfolioSaveAndLoadAndDetailInfo() {
    String input = "1\nPortfolio2\nAAPL\n100\n2024-03-12\nq\n12\n1\n22\n";
    simulateInputAndRunController(input);
    assertTrue(outContent.toString().contains("saved successfully")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));
    String input2 = "1\nPortfolio2\nAAPL\n100\n2024-03-12\nq\n13\nsavedPortfolios/Portfolio2"
            + LocalDate.now() + ".xml\nf\n4\n2\n22\n";
    simulateInputAndRunController(input2);

    assertTrue(outContent.toString()
            .contains(
                    "Portfolio Name:Portfolio2\n"));
    assertTrue(outContent.toString().contains("Value:"));
    assertTrue(outContent.toString().contains("Initial Value:"));
    assertTrue(outContent.toString().contains("Sold:"));
    assertTrue(outContent.toString().contains("Components:"));
    assertTrue(outContent.toString().contains("stock No.1"));
    assertTrue(outContent.toString().contains("AAPL"));


  }

  /**
   * Tests loading a portfolio from an invalid file path.
   */
  @Test
  public void testFailedPortfolioLoad() {
    String input = "13\ninvalidpath.xml\n22\n";
    simulateInputAndRunController(input);
    assertTrue(outContent.toString().contains("Cannot load xml files"));
  }

  /**
   * Tests the process of exiting the application.
   */
  @Test
  public void testExitingApplication() {
    String input = "22\n";
    simulateInputAndRunController(input);
    assertTrue(outContent.toString().contains("Exiting..."));
  }

  @Test
  public void testInquiryForDate() {
    String input = "1\nPortfolio 123\nIBM\n10\n2024-03-12\nq\n9\n1\n2024-03-12\n22\n";
    simulateInputAndRunController(input);
    assertTrue(outContent.toString().contains("The value of portfolio "
            + "at date 2024-03-12 is:1977.8")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));
    String input2 = "1\nPortfolio 123\nIBM\n10\n2024-03-12\nq\n9\n1\n2024-03-09\n22\n";
    simulateInputAndRunController(input2);

    assertTrue(outContent.toString()
            .contains("Error: Data not found for specified date and stock:2024-03-09 IBM")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));
    String input3 = "1\nPortfolio 123\nIBM\n10\n2024-03-12\nq\n9\n1\n2024--09\n2024-03-12\n22\n";
    simulateInputAndRunController(input3);
    assertTrue(outContent.toString()
            .contains("Error")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));

  }

  @Test
  public void testInvalidPortfolioNumber() {
    String input = "1\nPortfolio 123\nIBM\n10\n2024-03-12\nq\n9\n-1\n1\n2024-03-12\n22\n";
    simulateInputAndRunController(input);
    assertTrue(outContent.toString().contains("Invalid portfolio number.")
            || outContent.toString().contains(
            "Thank you for using Alpha Vantage!"
                    + " Our standard API rate limit is 25 requests per day."));
  }

  @Test
  public void testEmptyPortfolio() {
    String input = "9\n22\n";
    simulateInputAndRunController(input);
    assertTrue(outContent.toString().contains("There are no portfolios."));
  }

  @Test
  public void testAddStockToSuperPortfolioAndWeightInvestmentInvalidMoneyAmount() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n100\n20\n1\nAAPL\nGOOG\nIBM\nq\n0.1\n0.1\n"
            + "0\n100000\n2024-04-01\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "Error: Invalid money amount entered.";

    Assert.assertTrue(outContent.toString().contains(expectedLogEntry));
  }

  @Test
  public void testAddStockToSuperPortfolioAndWeightInvestmentInvalidPercentage() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n100\n20\n1\nAAPL\nGOOG\nIBM\nq\n0.1\n1.5\n0.1\n"
            + "100000\n2024-04-01\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "Error: Cannot use this percentage because out of bound:1.5";

    Assert.assertTrue(outContent.toString().contains(expectedLogEntry));
  }

  @Test
  public void testAddStockToSuperPortfolioAndWeightInvestmentOverPeriodOfTimeWithInvalidPeriod() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n100\n21\n1\nAAPL\nGOOG\nIBM\nIBM\nq\n0.1\n0.1\n"
            + "100000\n2024-03-12\n2024-04-01\n0\n5\n22\n";
    simulateInputAndRunController(input);
    //System.out.println(outContent.toString());
    String expectedLogEntry = "Error: Cannot use period less than 1 day.";
    Assert.assertTrue(outContent.toString().contains(expectedLogEntry));
  }

  @Test
  public void testAddStockToSuperPortfolioAndWeightInvestmentOverPeriodOfTimeWithInvalidMoney() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n100\n21\n1\nAAPL\nGOOG\nIBM\nq\n0.1\n0.1\n"
            + "0\n100000\n2024-03-12\n2024-04-01\n5\n22\n";
    simulateInputAndRunController(input);
    //System.out.println(outContent.toString());
    String expectedLogEntry = "Error: Invalid money amount entered.";
    Assert.assertTrue(outContent.toString().contains(expectedLogEntry));
  }

  @Test
  public void testWeightInvestmentOverPeriodOfTimeWithInvalidPercentage() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n100\n21\n1\nAAPL\nGOOG\nIBM\nq\n0.1\n0.99\n"
            + "0.1\n100000\n2024-03-12\n2024-04-01\n5\n22\n";
    simulateInputAndRunController(input);
    //System.out.println(outContent.toString());
    String expectedLogEntry = "Error: Cannot use this percentage because out of bound";
    Assert.assertTrue(outContent.toString().contains(expectedLogEntry));
  }

}
