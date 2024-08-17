import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;


import model.Stock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * this is a stock test class for assignment 5 features.
 */
public class Stock2Test {
  private Stock s;

  @Before
  public void setUp() {
    s = Stock.getBuilder().symbol("AAPL").shares(100)
            .timeStamp(LocalDate.parse("2024-03-22")).build2();
  }

  @Test
  public void testGainOrLoseOnGivenDay() {
    String result = s.gainOrLoseOnGivenDay(LocalDate.parse("2024-03-22"));
    String result2 = s.gainOrLoseOnGivenDay(LocalDate.parse("2024-03-21"));
    assertEquals("Gain:0.52", result);
    assertEquals("Lose:5.68", result2);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLoseOnGivenDayWithNonBusinessDate() {
    String result = s.gainOrLoseOnGivenDay(LocalDate.parse("2024-03-23"));
  }

  @Test
  public void testGainOrLoseOverPeriodOfTime() {
    String result = s.gainOrLoseOverPeriodOfTime(LocalDate.parse("2024-03-06"),
            LocalDate.parse("2024-03-22"));
    String result2 = s.gainOrLoseOverPeriodOfTime(LocalDate.parse("2024-02-18"),
            LocalDate.parse("2024-03-02"));
    String result3 = s.gainOrLoseOverPeriodOfTime(LocalDate.parse("2023-02-18"),
            LocalDate.parse("2023-02-18"));
    assertEquals("Gain: 3.16,start price:169.12,end price:172.28", result);
    assertEquals("Lose: -2.65,start price:182.31,end price:179.66", result2);
    assertEquals("Equal 0 profit,start price:152.55,end price:152.55", result3);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLoseOverPeriodOfTimeWithStartBeyondEnd() {
    String result = s.gainOrLoseOverPeriodOfTime(LocalDate.parse("2024-03-22"),
            LocalDate.parse("2024-03-21"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLoseOverPeriodOfTimeWithStartNoData() {
    String result = s.gainOrLoseOverPeriodOfTime(LocalDate.parse("1998-03-22"),
            LocalDate.parse("2024-03-21"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testGainOrLoseOverPeriodOfTimeWithEndNoData() {
    String result = s.gainOrLoseOverPeriodOfTime(LocalDate.parse("2023-03-22"),
            LocalDate.parse("3000-03-21"));
  }


  @Test
  public void testXDayMovingAverage() {
    double result = s.xDayMovingAverage(LocalDate.parse("2024-01-06"), 7);
    assertEquals(187.46, result, 0.01);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testXDayMovingAverageWithBeyondStartDate() {
    double result = s.xDayMovingAverage(LocalDate.parse("3000-01-06"), 7);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testXDayMovingAverageWithBeyondXValue() {
    double result = s.xDayMovingAverage(LocalDate.parse("1999-11-01"), 2);
  }

  @Test
  public void testCrossovers() {
    String result = s.crossovers(LocalDate.parse("2023-02-18"),
            LocalDate.parse("2024-03-18"));
    assertEquals("negative crossover for:2024-02-12" + System.lineSeparator()
            + "positive crossover for:2024-02-06" + System.lineSeparator()
            + "negative crossover for:2024-01-30" + System.lineSeparator()
            + "positive crossover for:2024-01-19" + System.lineSeparator()
            + "negative crossover for:2023-12-29" + System.lineSeparator()
            + "positive crossover for:2023-11-02" + System.lineSeparator()
            + "negative crossover for:2023-10-18" + System.lineSeparator()
            + "positive crossover for:2023-10-09" + System.lineSeparator()
            + "negative crossover for:2023-09-06" + System.lineSeparator()
            + "positive crossover for:2023-08-30" + System.lineSeparator()
            + "negative crossover for:2023-08-03" + System.lineSeparator()
            + "positive crossover for:2023-03-13" + System.lineSeparator()
            + "negative crossover for:2023-03-10" + System.lineSeparator()
            + "positive crossover for:2023-03-03" + System.lineSeparator()
            + "negative crossover for:2023-03-01" + System.lineSeparator(), result);
    String result2 = s.crossovers(LocalDate.parse("2024-02-18"),
            LocalDate.parse("2024-03-18"));
    assertEquals("No crossovers for given period.", result2);
    String result3 = s.crossovers(LocalDate.parse("2024-02-12"),
            LocalDate.parse("3000-03-18"));

    assertTrue(result3.contains("negative crossover for:2024-02-12"));
    String result4 = s.crossovers(LocalDate.parse("1980-02-12"),
            LocalDate.parse("1999-12-31"));
    assertEquals("positive crossover for:1999-12-31" + System.lineSeparator()
            + "negative crossover for:1999-12-30" + System.lineSeparator()
            + "positive crossover for:1999-12-29" + System.lineSeparator()
            + "negative crossover for:1999-12-27" + System.lineSeparator()
            + "positive crossover for:1999-12-21" + System.lineSeparator()
            + "negative crossover for:1999-12-20" + System.lineSeparator()
            + "positive crossover for:1999-12-15" + System.lineSeparator()
            + "negative crossover for:1999-12-14" + System.lineSeparator(), result4);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossoversWithStartBeyondEnd() {
    String result = s.crossovers(LocalDate.parse("2024-02-18"),
            LocalDate.parse("2024-02-17"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossoversWithNoValidData() {
    String result = s.crossovers(LocalDate.parse("3000-02-18"),
            LocalDate.parse("3000-03-17"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCrossoversWithNoValidData2() {
    String result = s.crossovers(LocalDate.parse("1000-02-18"),
            LocalDate.parse("1000-03-17"));
  }

  @Test
  public void testMovingCrossovers() {
    String result = s.movingCrossovers(LocalDate.parse("2023-12-18"),
            LocalDate.parse("2024-03-18"), 7, 30);
    assertEquals("negative moving crossover for:2024-02-14" + System.lineSeparator()
            + "positive moving crossover for:2024-02-12" + System.lineSeparator()
            + "negative moving crossover for:2024-02-05" + System.lineSeparator()
            + "positive moving crossover for:2024-01-25" + System.lineSeparator()
            + "negative moving crossover for:2024-01-02" + System.lineSeparator(), result);

    String result2 = s.movingCrossovers(LocalDate.parse("2023-12-18"),
            LocalDate.parse("2024-01-01"), 7, 30);
    assertEquals("No moving crossovers for given period.", result2);
    String result3 = s.movingCrossovers(LocalDate.parse("2024-02-14"),
            LocalDate.parse("3000-03-18"), 7, 30);

    assertTrue(result3.contains("negative moving crossover for:2024-02-14"));
    String result4 = s.movingCrossovers(LocalDate.parse("1980-02-12"),
            LocalDate.parse("2000-09-14"), 30, 60);
    assertEquals("positive moving crossover for:2000-09-14" + System.lineSeparator()
            + "negative moving crossover for:2000-05-09" + System.lineSeparator(), result4);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovingCrossoversWithXBiggerThanY() {
    String result = s.movingCrossovers(LocalDate.parse("2023-12-18"),
            LocalDate.parse("2024-03-18"), 30, 30);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovingCrossoversWithStartBeyondEnd() {
    String result = s.movingCrossovers(LocalDate.parse("2023-12-18"),
            LocalDate.parse("2023-03-18"), 7, 30);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovingCrossoversWithNoValidData() {
    String result = s.movingCrossovers(LocalDate.parse("3000-12-18"),
            LocalDate.parse("3001-03-18"), 7, 30);

  }

  @Test(expected = IllegalArgumentException.class)
  public void testMovingCrossoversWithNoValidData2() {
    String result = s.movingCrossovers(LocalDate.parse("1000-12-18"),
            LocalDate.parse("1001-03-18"), 7, 30);

  }
}
