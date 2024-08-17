import org.junit.Test;

import controller.AlphaVantageDemo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the AlphaVantageDemo class.
 */
public class AlphaVantageDemoTest {

  private static final String stock = "GOOG";

  /**
   * Tests storing data for a valid stock symbol and checks if the operation was fail.
   * Positive test case.
   */
  @Test
  public void storeDataValidSymbolTest() throws Exception {
    try {
      String result = new AlphaVantageDemo().storeData(stock);
      assertEquals("Expecting success message for a valid symbol.",
              "success", result);
      assertNotEquals("Expecting success message for a valid symbol.",
              "fail", result);
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  /**
   * Tests storing data for an invalid stock symbol and expects a failure.
   * Negative test case.
   */
  @Test
  public void storeDataInvalidSymbolTest() throws Exception {
    try {
      // Assuming "INVALID" is an invalid stock symbol
      String result = new AlphaVantageDemo().storeData("INVALID");
      assertEquals("Expecting fail message for an invalid symbol.",
              "fail", result);
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  /**
   * Assumes "IBM" is a valid stock symbol and previously stored data exists.
   * This is a positive test case where we expect to retrieve data successfully.
   */
  @Test
  public void getDataForStoredSymbolTest() throws Exception {
    try {
      // Use a valid stock symbol for testing
      String symbol = "IBM";
      String storeResult = new AlphaVantageDemo().storeData(symbol);
      // Check if data was stored successfully before attempting to retrieve it
      assertEquals(
              "Data storage should be successful for a valid symbol.",
              "success", storeResult);

      String data = new AlphaVantageDemo().getData(symbol);
      assertNotNull(
              "Data should be retrieved successfully for a stored symbol.", data);
      assertFalse(
              "Retrieved data should not contain error messages.",
              data.contains("Error Message"));
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  /**
   * Tests retrieving data for a stock symbol for which no data is available.
   * Negative test case.
   */
  @Test
  public void getDataForNonexistentSymbolTest() throws Exception {
    // Assuming no data is stored for "NONEXISTENT"
    String data = new AlphaVantageDemo().getData("NONEXISTENT");
    assertNull(
            "Data retrieval for a nonexistent symbol should return null.", data);
  }


  /**
   * Tests the getData method for null response when no data is available.
   */
  @Test
  public void testGetDataWithNoData() throws Exception {
    assertNull(
            "Data retrieval should return null for a symbol with no data.",
            new AlphaVantageDemo().getData("NODATA"));
  }

  /**
   * Tests if the {@link AlphaVantageDemo#getData(String)} method returns non-null data
   * when provided with a valid stock symbol.
   * This test assumes the stock symbol "GOOG" is valid and that corresponding data is available.
   */
  @Test
  public void testGetDataWithData() throws Exception {
    try {
      assertNotNull(
              "Not Null", new AlphaVantageDemo().getData(stock));
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }


  /**
   * Tests the response time of the storeData method to ensure it's within acceptable limits.
   * Performance test case.
   */
  @Test
  public void storeDataResponseTimeTest() throws Exception {
    try {
      long startTime = System.currentTimeMillis();
      new AlphaVantageDemo().storeData("GOOG");
      long endTime = System.currentTimeMillis();
      // Assuming the response time should be less than 5000 milliseconds
      assertTrue("Store data response time should be under 5000ms.",
              (endTime - startTime) < 5000);
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }

  /**
   * Tests storing and retrieving data for a new stock symbol to verify the end-to-end process.
   * Integration test case.
   */
  @Test
  public void endToEndDataStorageAndRetrievalTest() throws Exception {
    try {
      String symbol = "MSFT";
      String storeResult = new AlphaVantageDemo().storeData(symbol);
      assertEquals("Data storage should be successful for MSFT symbol.",
              "success", storeResult);

      String data = new AlphaVantageDemo().getData(symbol);
      assertNotNull("Data retrieval should be successful for recently stored symbol.",
              data);
      assertFalse("Retrieved data should not be empty for MSFT symbol.",
              data.isEmpty());
    } catch (Exception e) {
      System.out.println(e.getMessage());

    }
  }
}
