import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;

import model.SuperPortfolio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This is a super portfolio test class for assignment 5 features.
 */
public class SuperPortfolioTest {
  private SuperPortfolio sp;

  @Before
  public void setUp() {
    this.sp = new SuperPortfolio();
  }

  @Test
  public void testAddStock() {
    sp.addStock("AAPL", 100, LocalDate.parse("2024-03-14"));
    sp.addStock("IBM", 1000, LocalDate.parse("2024-03-08"));
    assertTrue(sp.showPortfolio().contains("IBM"));
    assertTrue(sp.showPortfolio().contains("AAPL"));
  }

  @Test
  public void testSellStock() {
    sp.addStock("AAPL", 100, LocalDate.parse("2024-03-14"));
    sp.addStock("IBM", 1000, LocalDate.parse("2024-03-08"));
    sp.sellStock("IBM", 900, LocalDate.parse("2024-03-11"));
    sp.sellStock("AAPL", 90, LocalDate.parse("2024-03-14"));

    assertTrue(sp.showPortfolio().contains("shares:100"));
    assertTrue(sp.showPortfolio().contains("shares:-900"));
    assertTrue(sp.showPortfolio().contains("shares:-90"));
    assertTrue(sp.showPortfolio().contains("shares:1000"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockAtIncorrectDate() {
    sp.addStock("AAPL", 100, LocalDate.parse("2024-03-14"));
    sp.addStock("IBM", 1000, LocalDate.parse("2024-03-08"));
    sp.sellStock("IBM", 900, LocalDate.parse("2024-03-07"));

  }

  @Test(expected = IllegalArgumentException.class)
  public void testSellStockAtMoreSharesCurrentlyHave() {
    sp.addStock("AAPL", 100, LocalDate.parse("2024-03-14"));
    sp.addStock("IBM", 1000, LocalDate.parse("2024-03-08"));
    sp.sellStock("IBM", 1000, LocalDate.parse("2024-03-09"));
    sp.sellStock("IBM", 1, LocalDate.parse("2024-03-08"));

  }

  @Test
  public void testCostBasis() {
    sp.addStock("AAPL", 100, LocalDate.parse("2024-03-14"));
    sp.addStock("IBM", 1000, LocalDate.parse("2024-03-08"));
    sp.sellStock("IBM", 900, LocalDate.parse("2024-03-11"));
    sp.sellStock("AAPL", 90, LocalDate.parse("2024-03-14"));
    sp.addStock("AAPL", 100, LocalDate.parse("2024-03-15"));
    sp.addStock("IBM", 1000, LocalDate.parse("2024-03-07"));
    assertEquals(409790, sp.costBasis(LocalDate.parse("2024-03-14")), 0.01);
  }

  @Test
  public void testGetValueByDate() {
    sp.addStock("AAPL", 100, LocalDate.parse("2024-03-14"));
    sp.addStock("IBM", 1000, LocalDate.parse("2024-03-08"));
    sp.sellStock("IBM", 900, LocalDate.parse("2024-03-11"));
    sp.sellStock("AAPL", 90, LocalDate.parse("2024-03-14"));
    sp.addStock("AAPL", 100, LocalDate.parse("2024-03-15"));
    sp.addStock("IBM", 1000, LocalDate.parse("2024-03-07"));
    double result = sp.getValueByDate(LocalDate.parse("2024-03-11"));
    double result2 = sp.getValueByDate(LocalDate.parse("2024-03-10"));
    double result3 = sp.getValueByDate(LocalDate.parse("2024-03-01"));
    assertEquals(210903, result, 0.01);
    assertEquals(391900, result2, 0.01);
    assertEquals(0, result3, 0.01);
  }

  @Test
  public void testWeightInvestmentOnSpecificDate() {
    HashMap<String, Double> hashmap = new HashMap<>();
    hashmap.put("AAPL", 0.1);
    hashmap.put("IBM", 0.25);
    hashmap.put("GOOG", 0.65);
    sp.weightInvestment(hashmap, LocalDate.parse("2024-04-01"), 100000);
    assertTrue(sp.showPortfolio()
            .contains("shares:415.34 initial price:156.5 trade time:2024-04-01"));
    assertTrue(sp.showPortfolio()
            .contains("shares:58.81 initial price:170.03 trade time:2024-04-01"));
    assertTrue(sp.showPortfolio()
            .contains("shares:131.7 initial price:189.83 trade time:2024-04-01"));
    assertTrue(sp.showPortfolio().contains("GOOG"));
    assertTrue(sp.showPortfolio().contains("IBM"));
    assertTrue(sp.showPortfolio().contains("AAPL"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWeightInvestmentIncorrectSymbol() {
    HashMap<String, Double> hashmap = new HashMap<>();
    hashmap.put("NotAStock", 1.0);
    sp.weightInvestment(hashmap, LocalDate.parse("2024-04-01"), 100000);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWeightInvestmentIncorrectDate() {
    HashMap<String, Double> hashmap = new HashMap<>();
    hashmap.put("AAPL", 0.1);
    hashmap.put("IBM", 0.25);
    hashmap.put("GOOG", 0.65);
    sp.weightInvestment(hashmap, LocalDate.parse("2024-03-31"), 100000);
  }

  @Test
  public void testWeightInvestmentOnSpecificPeriod() {
    HashMap<String, Double> hashmap = new HashMap<>();
    hashmap.put("AAPL", 0.1);
    hashmap.put("IBM", 0.25);
    hashmap.put("GOOG", 0.65);
    sp.weightInvestmentStartToFinish(hashmap, LocalDate.parse("2024-03-12"),
            LocalDate.parse("2024-04-01"), 100000, 5);
    assertTrue(sp.showPortfolio().contains("GOOG"));
    assertTrue(sp.showPortfolio().contains("IBM"));
    assertTrue(sp.showPortfolio().contains("AAPL"));
    //System.out.println(sp.showPortfolio());
    assertTrue(sp.showPortfolio()
            .contains("shares:465.55 initial price:139.62 trade time:2024-03-12"));
    assertTrue(sp.showPortfolio()
            .contains("shares:57.73 initial price:173.23 trade time:2024-03-12"));
    assertTrue(sp.showPortfolio()
            .contains("shares:126.4 initial price:197.78 trade time:2024-03-12"));
    assertTrue(sp.showPortfolio()
            .contains("shares:437.77 initial price:148.48 trade time:2024-03-18"));
    assertTrue(sp.showPortfolio()
            .contains("shares:57.56 initial price:173.72 trade time:2024-03-18"));
    assertTrue(sp.showPortfolio()
            .contains("shares:130.42 initial price:191.69 trade time:2024-03-18"));
    assertTrue(sp.showPortfolio()
            .contains("shares:430.04 initial price:151.15 trade time:2024-03-25"));
    assertTrue(sp.showPortfolio()
            .contains("shares:58.53 initial price:170.85 trade time:2024-03-25"));
    assertTrue(sp.showPortfolio()
            .contains("shares:132.42 initial price:188.79 trade time:2024-03-25"));
    assertTrue(sp.showPortfolio()
            .contains("shares:415.34 initial price:156.5 trade time:2024-04-01"));
    assertTrue(sp.showPortfolio()
            .contains("shares:58.81 initial price:170.03 trade time:2024-04-01"));
    assertTrue(sp.showPortfolio()
            .contains("shares:131.7 initial price:189.83 trade time:2024-04-01"));
  }

}
