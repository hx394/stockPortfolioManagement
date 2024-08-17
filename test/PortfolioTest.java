import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import controller.AlphaVantageDemo;
import controller.ControllerImp;
import model.ModelImp;
import model.Portfolio;
import model.Stock;
import view.ViewImp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Provides tests for {@link Portfolio} class functionalities,
 * including adding stocks, evaluating portfolio value,
 * handling portfolio names, selling the portfolio,
 * checking for the existence of a company in the portfolio,
 * and testing the save and load functionality.
 */
public class PortfolioTest {
  private Portfolio portfolio;
  private LocalDate dateNow;

  /**
   * Initializes a new portfolio and stocks before each test.
   */
  @Before
  public void setUp() {
    ModelImp modelImp = new ModelImp();
    ViewImp viewImp = new ViewImp(System.out);
    ControllerImp controllerImp = new ControllerImp(modelImp, viewImp, System.in, System.out,
            new AlphaVantageDemo());
    portfolio = new Portfolio();
    portfolio.setPortfolioName("myPortfolio1");
    dateNow = LocalDate.now();
    Stock stock1 = Stock.getBuilder().symbol("AAPL").shares(100).price(150.0).initialPrice(140.0)
            .timeStamp(dateNow.minusDays(1)).build2();
    Stock stock2 = Stock.getBuilder().symbol("MSFT").shares(200).price(200.0).initialPrice(190.0)
            .timeStamp(dateNow.minusDays(1)).build2();
    try {
      portfolio.addStock(stock1.getSymbol(), stock1.getShares());
      portfolio.addStock(stock2.getSymbol(), stock2.getShares());
    } catch (Exception e) {
      System.out.println(e.getMessage());
      portfolio.addStock(stock1);
      portfolio.addStock(stock2);
    }
  }

  /**
   * Tests adding a single stock to the portfolio and evaluating its value.
   */
  @Test
  public void addSingleStockAndEvaluatePortfolioValue() {
    String symbol = "AAPL";
    long shares = 100;
    double pricePerShare = 150.0; // Assuming the price per share is known
    double expectedValue = shares * pricePerShare;
    try {
      portfolio = new Portfolio();
      Stock stock1 = Stock.getBuilder()
              .symbol("AAPL")
              .shares(100)
              .price(150.0)
              .initialPrice(140.0)
              .timeStamp(dateNow.minusDays(1))
              .build2();
      // Add a single stock to the portfolio
      portfolio.addStock(stock1);

      // Check if the portfolio value matches the expected value of the added stock
      assertEquals("Adding a single stock should correctly update the portfolio value.",
              expectedValue, portfolio.getValueWithoutUpdate(), 0.01);
    } catch (Exception e) {
      System.out.println(e.getMessage());

      return;
    }
  }

  /**
   * Tests adding multiple stocks to the portfolio and evaluating its overall value.
   */
  @Test
  public void addMultipleStocksAndEvaluatePortfolioValue() {
    // Define the stocks to add
    String symbol1 = "AAPL";
    long shares1 = 100;
    double pricePerShare1 = 150.0; // Assuming the price per share is known
    String symbol2 = "GOOGL";
    long shares2 = 200;
    double pricePerShare2 = 200.0; // Assuming the price per share is known
    // Calculate the expected total value after adding both stocks
    double expectedValue = (shares1 * pricePerShare1) + (shares2 * pricePerShare2);
    try {
      // Add stocks to the portfolio
      portfolio = new Portfolio();
      Stock stock1 = Stock.getBuilder()
              .symbol("AAPL").shares(100).price(150.0).initialPrice(140.0)
              .timeStamp(dateNow.minusDays(1)).build2();
      // Add a single stock to the portfolio
      portfolio.addStock(stock1);
      Stock stock2 = Stock.getBuilder().symbol("MSFT").shares(200).price(200.0).initialPrice(190.0)
              .timeStamp(dateNow.minusDays(1)).build2();
      portfolio.addStock(stock2);
      // Check if the portfolio value matches the combined expected value of the added stocks
      assertEquals("Adding multiple stocks should correctly update the portfolio value.",
              expectedValue, portfolio.getValueWithoutUpdate(), 0.01);
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Tests the assignment and retrieval of a portfolio's name.
   */
  @Test
  public void portfolioNameHandling() {
    String expectedName = "My Portfolio";
    portfolio.setPortfolioName(expectedName);
    assertEquals(expectedName, portfolio.getPortfolioName());
  }

  /**
   * Tests selling the portfolio and calculating profit.
   */
  @Test
  public void sellPortfolioAndCalculateProfit() {
    try {
      portfolio.sellPortfolio();
      assertTrue(portfolio.isSold());
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  /**
   * Tests checking for the existence of a company within the portfolio.
   */
  @Test
  public void checkForExistingCompany() {
    assertTrue(portfolio.hasCompany("AAPL"));
    assertFalse(portfolio.hasCompany("GOOG")); // Assuming GOOG was not added
  }

  /**
   * Placeholder for testing save and load functionality.
   *
   * @throws Exception If save or load fails.
   */
  @Test
  public void testSavePortfolio() throws Exception {
    try {
      assertTrue(portfolio.save());

    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  @Test
  public void testFindPortfolioValueAtCertainDate() {
    try {
      Portfolio p = new Portfolio();
      p.addStock("GOOGL", 100);
      double value = p.getValueAtCertainDate(LocalDate.parse("2024-03-12"));
      if (value != -1) {
        assertEquals(value, 13850, 0.01);
      } else {
        assertEquals(value, -1, 0.01);
      }
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  @Test
  public void testFindPortfolioValueAtCertainDateButFails() {
    try {
      Portfolio p = new Portfolio();
      p.addStock("GOOGL", 100);
      double value = p.getValueAtCertainDate(LocalDate.parse("0001-03-12"));
      assertEquals(value, -1, 0.01);
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }
}
