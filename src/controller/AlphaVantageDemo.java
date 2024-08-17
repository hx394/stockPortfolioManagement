package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;

/**
 * Demonstrates fetching and storing daily stock price data from Alpha Vantage API.
 */
public class AlphaVantageDemo implements DataSourceAPI {

  /**
   * default constructor.
   */
  public AlphaVantageDemo(){
    //default constructor doing nothing.
  }

  /**
   * Fetches daily stock price data for a given stock symbol and stores it in a local file.
   *
   * @param stock The stock symbol to fetch data for.
   * @return "success" if data was fetched and stored successfully,
   *         "fail" if an error message is received in response,
   *         "Failed to create directory" if unable to create the storage directory,
   *         "IO exception of writing file" if an error occurs writing the file,
   *          or "malformed URL Exception" if the URL is malformed.
   * @throws Exception if anything goes wrong
   */
  @Override
  public String storeData(String stock) throws Exception {
    //the API key needed to use this web service.

    String apiKey = "JLB7VCXXWFPOFL7F";
    URL url = urlCreate(apiKey, stock);

    StringBuilder output = new StringBuilder();

    inputOutput(url, output);

    String content = output.toString();
    LocalDate currentDate = LocalDate.now();
    createStockPricesDirectory();
    String filePath = "stock_prices/" + stock + "=" + currentDate + ".txt";

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
      writer.write(content);
    } catch (IOException e) {

      throw new IOException("IO exception of writing file");
    }
    if (content.contains("Error Message") || content.contains("Information")
            || content.contains("Note")) {
      throw new Exception(content);

    }
    return "success";
  }

  /**
   * Execute this query. This returns an InputStream object.
   * In the csv format, it returns several lines, each line being separated by commas.
   * Each line contains the date, price at opening time, the highest price for that date,
   * the lowest price for that date, price at closing time,
   * and the volume of trade (no. of shares bought/sold) on that date.
   *
   * @param url    the url to input
   * @param output the stringBuilder
   * @throws IOException if IOException happens
   */
  private static void inputOutput(URL url, StringBuilder output) throws IOException {
    try {

      InputStream in = url.openStream();
      int b;

      while ((b = in.read()) != -1) {
        output.append((char) b);
      }
    } catch (IOException e) {

      throw new IOException("IO exception");
    }

  }

  /**
   * create the URL.
   * This is the query to the web service.
   * The query string includes the type of query (DAILY stock prices),
   * stock symbol to be looked up, the API key and the format of
   * the returned data (comma-separated values:csv).
   * This service also supports JSON which you are welcome to use.
   *
   * @param apiKey the apiKey
   * @param stock  the stock symbol
   * @return URL the url
   * @throws Exception if anything goes wrong
   */
  private static URL urlCreate(String apiKey, String stock) throws Exception {
    try {

      return new URL("https://www.alphavantage"
              + ".co/query?function=TIME_SERIES_DAILY"
              + "&outputsize=full"
              + "&symbol"
              + "=" + stock + "&apikey=" + apiKey + "&datatype=csv");
    } catch (MalformedURLException e) {
      throw new Exception("malformed URL Exception");
    }
  }

  /**
   * create stock_prices directory.
   * if not exist. create it.
   * if not successful, throw exception.
   *
   * @throws Exception if anything goes wrong
   */
  private static void createStockPricesDirectory() throws Exception {
    String directoryPath = "stock_prices";
    // Create a File object for the directory
    File directory = new File(directoryPath);

    // Check if the directory does not exist and create it
    if (!directory.exists()) {
      boolean wasSuccessful = directory.mkdirs();
      if (!wasSuccessful) {
        throw new Exception("Failed to create directory");
      }
    }
  }

  /**
   * Reads stored daily stock price data for a given stock symbol from a local file.
   *
   * @param stock The stock symbol to retrieve data for.
   * @return The content of the file as a String if successful,
   *          null if an error occurs reading the file or if the "Error Message"
   *          is found in the content indicating a failed fetch operation.
   * @throws Exception if anything goes wrong
   */
  @Override
  public String getData(String stock) throws Exception {
    LocalDate currentDate = LocalDate.now();
    // Replace with your file path
    String filePath = "stock_prices/" + stock + "=" + currentDate + ".txt";
    StringBuilder stringBuilder = new StringBuilder();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = reader.readLine()) != null) {
        stringBuilder.append(line); // Process the line or do something with it
        stringBuilder.append("\n");
      }
    } catch (IOException e) {
      return null;
    }
    String result = stringBuilder.toString();
    if (result.contains("Error Message") || result.contains("Information")
            || result.contains("Note")) {
      throw new Exception(result);
    } else {

      return result;
    }
  }
}
