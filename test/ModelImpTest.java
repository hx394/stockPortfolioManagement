import org.junit.Before;
import org.junit.Test;



import model.Model;
import model.ModelImp;
import model.Portfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import static org.junit.Assert.fail;

/**
 * Tests for the ModelImp class.
 */
public class ModelImpTest {
  private Model model;

  /**
   * Initializes a new instance of {@link ModelImp} to be used in the tests.
   */
  @Before
  public void setUp() {
    model = new ModelImp();
  }

  /**
   * Tests adding a portfolio and verifying the model's size increases.
   * This serves as a positive test case.
   */
  @Test
  public void addPortfolioIncreasesModelSize() {
    Portfolio portfolio = model.addPortfolio();
    model.addPortfolio();
    assertNotNull(
            "Newly added portfolio should not be null",
            portfolio);
    assertEquals(
            "Model should have 2 portfolio after adding two new ones",
            2, model.getSize());
  }

  /**
   * Tests the behavior of showing all portfolios when the model is empty.
   */
  @Test
  public void showAllPortfoliosWhenEmptyTest() {
    assertEquals(
            "simple portfolios:\nflexible portfolios:\n", model.showAllPortfolios());
  }

  /**
   * Tests the response when attempting to sell a portfolio that does not exist.
   */
  @Test
  public void sellPortfolioNotExistsTest() {
    String result = model.sellPortfolio(1);
    assertEquals(
            "Selling a non-existing portfolio should return a specific message",
            "No such portfolio.", result);
  }

  /**
   * Tests retrieving information for a non-existent portfolio.
   */
  @Test
  public void getSpecificPortfolioInfoNotExistsTest() {
    String result = model.getSpecificPortfolioInfo(1);
    assertEquals(
            "Getting info for a non-existing portfolio should return a specific message",
            "No such portfolio.", result);
  }


  /**
   * Tests the model's size tracking by asserting its size before and after adding a portfolio.
   * Expected to start at 0 and increase to 1 upon portfolio addition,
   * confirming accurate size management.
   */
  @Test
  public void getSizeTest() {
    assertEquals("Initially, model size should be 0", 0, model.getSize());
    model.addPortfolio();
    assertEquals("After adding a portfolio, model size should be 1", 1,
            model.getSize());
  }

  /**
   * Tests updating the model's portfolios without expecting any exception.
   * Assumes that the update method's implementation might not have directly observable effects.
   */
  @Test
  public void testUpdatePortfoliosWithoutException() {
    try {
      model.addPortfolio();
      model.update();

    } catch (Exception e) {
      fail("Update method should not throw any exception.");
    }
  }


  /**
   * Tests the retrieval of the total number of portfolios when the model is both empty and
   * populated.
   * Includes both positive and negative test cases within the same test method.
   */
  @Test
  public void getSizeWithAndWithoutPortfolios() {
    assertEquals(
            "Initially, the model should report size 0.",
            0, model.getSize());
    model.addPortfolio();
    assertNotEquals(
            "After adding a portfolio, the model size should not be 0.",
            0, model.getSize());
  }


}
