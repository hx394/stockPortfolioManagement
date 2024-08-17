import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;

import model.Stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

/**
 * This class tests the Stock class by using each of its features.
 * calls functions of the stock class to check the validity.
 */
public class StockTest {
  private final LocalDate timestamp = LocalDate.now();
  // Used for floating point comparisons
  private final double delta = 0.01;
  private Stock stock;

  /**
   * Initializes a Stock object with predefined values before each test.
   */
  @Before
  public void setUp() {
    // Use StockBuilder to initialize the stock object before each test
    stock = Stock.getBuilder()
            .symbol("AAPL")
            .shares(100)
            .price(150.0)
            .initialPrice(140.0)
            .timeStamp(LocalDate.now())
            .build2();
  }

  /**
   * Tests if the getPrice method returns the correct price of the stock.
   */
  @Test
  public void getPriceTest() {
    assertEquals(150.0, stock.getPrice(), delta);
  }

  /**
   * Tests if the getShares method returns the correct number of shares.
   */
  @Test
  public void getSharesTest() {
    assertEquals(100, stock.getShares());
    assertNotEquals(101.000000001, stock.getShares());
  }

  /**
   * Tests if the evaluate method correctly calculates the total market value of the stock.
   */
  @Test
  public void evaluateTest() {
    // Assuming evaluate calculates the total value of the stock holdings
    assertEquals(15000.0, stock.evaluate(), delta);
    assertNotEquals(5000.0, stock.evaluate(), delta);
  }

  /**
   * Tests if the getSymbol method returns the correct stock symbol.
   */
  @Test
  public void getSymbolTest() {
    assertEquals("AAPL", stock.getSymbol());
    assertNotEquals("IBM", stock.getSymbol());
  }

  /**
   * Tests if the getInitialPrice method returns the correct initial price of the stock.
   */
  @Test
  public void getInitialPriceTest() {
    assertEquals(140.0, stock.getInitialPrice(),
            delta);
  }

  /**
   * Tests if the getTimestamp method returns the correct timestamp associated with the stock.
   */
  @Test
  public void getTimestampTest() {
    assertEquals(timestamp, stock.getTimestamp());
  }


  /**
   * Tests to get the historical price at certain date.
   * uses apple as example.
   */
  @Test
  public void getHistoricalPriceTest() {
    try {
      double price = stock.getHistoricalPrice(LocalDate.parse("2024-03-12"));
      assertEquals(
              price, 173.23, 0.01);
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  /**
   * Tests to get the volume at previous trading day.
   * uses apple as example.
   */
  @Test
  public void testGetVolume() {
    try {
      long volume = Stock.getVolume(stock.getSymbol(), stock.getTimestamp());
      assertTrue(
              volume >= 0);
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  /**
   * Tests the calculation of the stock's market value
   * based on the current price and the number of shares.
   */
  @Test
  public void testEvaluateMarketValue() {
    double expectedMarketValue = stock.getPrice() * stock.getShares();
    assertEquals("The stock's market value should be correctly calculated.",
            expectedMarketValue, stock.evaluate(), 0.001);
  }

  /**
   * Tests retrieving the initial price of the stock.
   */
  @Test
  public void testGetInitialPrice() {
    assertEquals(
            "The initial price of the stock should be correct.",
            140.0, stock.getInitialPrice(), 0.001);
  }

  /**
   * Tests the toString method of the Stock class to ensure
   * it returns the correct string representation.
   */
  @Test
  public void testToString() {
    String expected =
            "AAPL price:150.0 shares:100 initial price:140.0 trade time:"
                    + timestamp.toString();
    assertEquals(
            "The toString method should return the correct string representation.",
            expected, stock.toString());
  }
}
