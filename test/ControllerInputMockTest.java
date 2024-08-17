import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import controller.AlphaVantageDemo;
import controller.ControllerImp;
import model.MockModel;
import view.TextView;
import view.ViewImp;

/**
 * this is a controller test class using mock model for all the input verification and method
 * calling of model verification.
 */
public class ControllerInputMockTest {
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  private MockModel mockModel;
  private TextView view;

  @Before
  public void setUp() {
    // Replace ModelImp with MockModel
    mockModel = new MockModel(new StringBuilder(), 12345);
    view = new ViewImp(new PrintStream(outContent));
  }

  private void simulateInputAndRunController(String inputData) {
    // Simulate user input
    ByteArrayInputStream inContent = new ByteArrayInputStream(inputData.getBytes());
    // Initialize controller with MockModel, no need for View or AlphaVantageDemo in this context
    ControllerImp controller = new ControllerImp(mockModel, view, inContent, System.out,
            new AlphaVantageDemo());
    controller.toGo();
  }

  @Test
  public void testAddingNewPortfolio() {
    String input = "1\nPortfolio1\nq\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "addPortfolio called";
    // Verify if MockModel logged the expected method call
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
    // Additional verification based on controller output can be added here
  }

  // Test for showing all portfolios
  @Test
  public void testShowAllPortfolios() {
    String input = "3\n22\n"; // Assuming option 3 is for showing all portfolios and 20 for exit
    simulateInputAndRunController(input);
    String expectedLogEntry = "showAllPortfolios called";
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
    Assert.assertTrue(outContent.toString().contains("Mock portfolios list 12345"));
  }

  // Test for adding stock to super portfolio
  @Test
  public void testAddStockToSuperPortfolio() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n100\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "superPortfolioAddStock called with number: 1, "
            + "symbol: AAPL, shares: 100, time: 2024-03-12";

    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }

  // Test for selling stock from super portfolio
  @Test
  public void testSellStockFromSuperPortfolio() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n1000\n6\n1\nAAPL\n2024-03-25\n50\n4\n1\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry =
            "superPortfolioSellStock called with number: 1, "
                    + "symbol: AAPL, shares: 50, time: 2024-03-25";

    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }

  // Test for updating portfolios
  @Test
  public void testUpdatePortfolios() {
    String input = "11\n22\n"; // Assuming option 11 is for updating portfolios and 20 for exit
    simulateInputAndRunController(input);
    String expectedLogEntry = "update called";
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }

  // Test for selling a portfolio
  @Test
  public void testSellPortfolio() {
    String input = "10\n1\n22\n"; // Assuming option 10 is for selling a portfolio and 20 for exit
    simulateInputAndRunController(input);
    String expectedLogEntry = "sellPortfolio called with number: 1";
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }


  // Test for saving a portfolio
  @Test
  public void testSavePortfolio() {
    // Simulate user selecting to save a portfolio (option "12") and then exiting (option "20")
    String input = "2\np1\n12\n1\n22\n";
    simulateInputAndRunController(input);

    // Verify if MockModel logged the save operation for the correct portfolio number
    String expectedLogEntry = "save called with portfolioNum: 1";


    Assert.assertTrue("The save operation was not called as expected.",
            mockModel.getLog().contains(expectedLogEntry));

    // Verify the output to ensure the user is notified about the successful save
    Assert.assertTrue("The save success message was not displayed.",
            outContent.toString().contains("saved successfully")
                    || outContent.toString().contains("save fails."));
  }


  @Test
  public void testCalculateCostBasis() {
    String input = "7\n1\n2020-01-01\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "superPortfolioCostBasis called with number: 1, time: 2020-01-01";
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }


  @Test
  public void testPortfolioValueAtDate() {
    String input = "8\n1\n2020-01-01\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "superPortfolioTimeBoundaryValue called with number: 1,"
            + " time: 2020-01-01";
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }


  @Test
  public void testGenerateChartForStock() {
    String input = "19\n1\nAAPL\n2020-01-01\n2020-01-31\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "generateChartForStock called with symbol: AAPL, "
            + "startDate: 2020-01-01, endDate: 2020-01-31";
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }


  @Test
  public void testGenerateChartForPortfolio() {
    String input = "19\n2\n1\n2020-01-01\n2020-01-31\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "generateChartForPortfolio called with portfolioNum: 1, "
            + "startDate: 2020-01-01, endDate: 2020-01-31";
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }

  @Test
  public void testAddStockToSuperPortfolioAndWeightInvestment() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n100\n20\n1\nAAPL\nGOOG\nIBM\nq\n0.1\n0.1\n"
            + "100000\n2024-04-01\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "1GOOG0.1AAPL0.1IBM0.82024-04-01100000.0";

    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }

  @Test
  public void testAddStockToSuperPortfolioAndWeightInvestmentOverPeriodOfTime() {
    String input = "2\np1\n5\n1\nAAPL\n2024-03-12\n100\n21\n1\nAAPL\nGOOG\nIBM\nq\n0.1\n0.1\n"
            + "100000\n2024-03-12\n2024-04-01\n5\n22\n";
    simulateInputAndRunController(input);
    String expectedLogEntry = "1GOOG0.1AAPL0.1IBM0.82024-03-122024-04-01100000.05";
    Assert.assertTrue(mockModel.getLog().contains(expectedLogEntry));
  }
}

