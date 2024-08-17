package model;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

/**
 * Represents a portfolio containing a collection of stocks.
 * Provides functionality to manage stocks within the portfolio, including adding stocks,
 * calculating the portfolio's value, and saving or loading portfolio data.
 */
public class Portfolio {

  /**
   * companies set.
   */
  protected final Set<String> companies = new HashSet<>();
  /**
   * portfolio arraylist.
   */
  protected ArrayList<Stock> portfolio;
  /**
   * portfolio name.
   */
  protected String portfolioName;

  /**
   * value of portfolio.
   */
  protected double value;

  /**
   * is sold or not.
   */
  protected boolean sold;

  /**
   * initial value of portfolio.
   */
  protected double initialValue;

  /**
   * Constructs an empty Portfolio instance.
   * Initializes the portfolio with no stocks,
   * an initial value of 0, and sets its status as not sold.
   */
  public Portfolio() {

    sold = false;

    this.portfolio = new ArrayList<Stock>();

    value = 0;

  }

  /**
   * Constructs a new Portfolio instance by parsing portfolio data from an XML Document.
   *
   * @param d The XML Document containing the portfolio data.
   * @throws Exception if there is an error during parsing.
   */
  public Portfolio(Document d) throws Exception {

    this.portfolioName = d
            .getElementsByTagName("portfolio_name").item(0).getTextContent();
    this.sold = Boolean.parseBoolean(
            d.getElementsByTagName("sold").item(0).getTextContent());
    this.value = Double.parseDouble(
            d.getElementsByTagName("value").item(0).getTextContent());
    this.initialValue = Double.parseDouble(
            d.getElementsByTagName("initial_value").item(0).getTextContent());
    this.portfolio = new ArrayList<Stock>();

    NodeList stocks = d.getElementsByTagName("stocks").item(0).getChildNodes();


    for (int i = 0; i < stocks.getLength(); i++) {
      Node n = stocks.item(i);
      if (n.getNodeType() == Node.ELEMENT_NODE) {
        Element element = (Element) n;
        portfolio.add(getStockFromElement(element));
      }
    }

  }

  /**
   * get stock from document element.
   *
   * @param element the document element
   * @return Stock the new created stock
   */
  protected static Stock getStockFromElement(Element element) {
    return Stock.getBuilder()
            .symbol(element.getElementsByTagName("symbol").item(0).getTextContent())
            .price(
                    Double.parseDouble(
                            element.getElementsByTagName("price")
                                    .item(0).getTextContent()))
            .shares(
                    Double.parseDouble(
                            element.getElementsByTagName("shares")
                                    .item(0).getTextContent()))
            .initialPrice(
                    Double.parseDouble(
                            element.getElementsByTagName("initial_price")
                                    .item(0).getTextContent()))
            .timeStamp(
                    LocalDate.parse(
                            element.getElementsByTagName("trade_time")
                                    .item(0).getTextContent()))
            .build2();
  }

  /**
   * Loads a portfolio from an XML document.
   *
   * @param d The XML document containing the portfolio data.
   * @return A new Portfolio instance populated with the data from the document.
   * @throws Exception if there is an error during the loading process.
   */
  public static Portfolio load(Document d) throws Exception {

    return new Portfolio(d);
  }

  /**
   * Generates a string representation of the portfolio,
   * including details of all stocks contained within.
   *
   * @return A formatted string containing portfolio and stock details.
   */
  public String showPortfolio() {
    StringBuilder information = new StringBuilder();
    information.append("Portfolio Name:").append(this.getPortfolioName()).append("\n");
    information.append("Value:").append(this.getValue()).append("\n");
    information.append("Initial Value:").append(this.initialValue).append("\n");
    information.append("Sold:").append(this.sold).append("\n");
    information.append("Components:\n");
    for (int i = 0; i < portfolio.size(); i++) {
      information.append("stock No.").append(i + 1).append("\n");
      information.append(portfolio.get(i).toString()).append("\n");
    }
    return information.toString();
  }

  /**
   * Marks the portfolio as sold and calculates the profit based on the initial and current values.
   *
   * @return A string message indicating the portfolio has been sold and displaying the profit.
   */
  public String sellPortfolio() {
    sold = true;
    StringBuilder information = new StringBuilder();
    information.append("Portfolio sold, Profits:");
    double profits = Math.round((getValue() - initialValue) * 100) / 100.0;
    information.append(profits);
    return information.toString();
  }

  /**
   * Returns the name of the portfolio.
   *
   * @return The name of the portfolio.
   */
  public String getPortfolioName() {
    return this.portfolioName;
  }

  /**
   * Sets the name of the portfolio to the specified string.
   *
   * @param name The new name for the portfolio.
   */
  public void setPortfolioName(String name) {
    this.portfolioName = name;
  }

  /**
   * Calculates and returns the current total value of the portfolio.
   * This method triggers an update to recalculate
   * the value of the portfolio based on the current stock prices.
   *
   * @return The updated total value of the portfolio.
   * @throws IllegalArgumentException from base level
   */
  public double getValue() throws IllegalArgumentException {
    this.update();
    return this.value;
  }

  /**
   * get value without update.
   *
   * @return The value without update
   */
  public double getValueWithoutUpdate() {
    return this.value;
  }

  /**
   * Updates the value of the portfolio based on the current price of the stocks it contains.
   */
  public void update() {
    double total = 0;
    for (Stock stock : portfolio) {
      try {
        stock.update();
      } catch (IllegalArgumentException e) {
        continue;
      }
      total += stock.evaluate();
    }
    this.value = Math.round(total * 100) / 100.0;
  }

  /**
   * Calculates the value of the portfolio at a specific date.
   *
   * @param date The date for which the portfolio value is calculated.
   * @return The value of the portfolio at the specified date,
   * @throws IllegalArgumentException from base level
   */
  public double getValueAtCertainDate(LocalDate date) throws IllegalArgumentException {
    double value = 0;
    for (Stock stock : portfolio) {
      //find its historical value
      try {
        value += stock.hasDataOnGivenDay(date) * stock.getShares();
      } catch (IllegalArgumentException e) {
        throw new IllegalArgumentException(e.getMessage());
      }
    }
    return Math.round(value * 100) / 100.0;
  }

  /**
   * Saves the portfolio data to an XML file.
   *
   * @return true if the save operation was successful, false otherwise.
   */
  public boolean save() {
    DocumentBuilderFactory factory;
    DocumentBuilder builder;
    try {
      factory = DocumentBuilderFactory.newInstance();
      builder = factory.newDocumentBuilder();
    } catch (Exception e) {

      return false;
    }
    Document document = builder.newDocument();
    // Create root element and append to the document
    documentWrite(document);
    String directoryPath = "savedPortfolios";
    // Create a File object for the directory
    File directory = new File(directoryPath);
    // Check if the directory does not exist and create it
    if (!directory.exists()) {
      boolean wasSuccessful = directory.mkdirs();
      if (!wasSuccessful) {
        return false;
      }
    }
    return transformToXML(document);
  }

  /**
   * transform the document into xml file.
   *
   * @param document the document object
   * @return boolean true if success, false otherwise
   */
  private boolean transformToXML(Document document) {
    // Write the document to an XML file
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    Transformer transformer;
    try {
      transformer = transformerFactory.newTransformer();

      transformer.setOutputProperty(OutputKeys.INDENT, "yes");
      DOMSource source = new DOMSource(document);
      StreamResult result = new StreamResult(
              "savedPortfolios/" + this.portfolioName + LocalDate.now() + ".xml");

      transformer.transform(source, result);
      return true;

    } catch (Exception e) {
      return false;
    }
  }

  /**
   * write a document by automatically creating elements.
   *
   * @param document the needed document to write
   */
  private void documentWrite(Document document) {
    Element rootElement =
            document.createElement(this.portfolioName
                    .replace(" ", "") + LocalDate.now());
    document.appendChild(rootElement);

    // Create a child element and append to the root element
    rootElement.appendChild(
                    document.createElement("portfolio_name"))
            .setTextContent(this.portfolioName);
    rootElement.appendChild(
                    document.createElement("sold"))
            .setTextContent(this.sold + "");
    rootElement.appendChild(
                    document.createElement("value"))
            .setTextContent(this.value + "");
    rootElement.appendChild(
                    document.createElement("initial_value"))
            .setTextContent(this.initialValue + "");
    addStocksElements(document, rootElement);
  }

  /**
   * add stocks elements to a document portfolio.
   *
   * @param document    the document
   * @param rootElement the root element of document
   */
  private void addStocksElements(Document document, Element rootElement) {
    Element stocksElements = document.createElement("stocks");
    for (Stock current : portfolio) {
      Element stock = document.createElement(current.getSymbol());
      stock.appendChild(
                      document.createElement("symbol"))
              .setTextContent(current.getSymbol());
      stock.appendChild(
                      document.createElement("price"))
              .setTextContent(current.getPrice() + "");
      stock.appendChild(
                      document.createElement("shares"))
              .setTextContent(current.getShares() + "");
      stock.appendChild(
                      document.createElement("initial_price"))
              .setTextContent(current.getInitialPrice() + "");
      stock.appendChild(
                      document.createElement("trade_time"))
              .setTextContent(current.getTimestamp().toString());

      stocksElements.appendChild(stock);
    }
    rootElement.appendChild(stocksElements);
  }

  /**
   * Adds a stock to the portfolio.
   *
   * @param symbol The stock's ticker symbol.
   * @param shares The number of shares to add.
   * @return Stock the object stock
   */
  public Stock addStock(String symbol, double shares) {
    Stock st = Stock.getBuilder().symbol(symbol).shares(shares).build();
    portfolio.add(st);
    value = value + portfolio.get(portfolio.size() - 1).evaluate();
    value = Math.round(value * 100) / 100.0;
    initialValue = value;
    companies.add(symbol);
    return st;
  }

  /**
   * Adds a stock to the portfolio.
   *
   * @param alpha a stock
   */
  public void addStock(Stock alpha) {
    portfolio.add(alpha);
    value = value + portfolio.get(portfolio.size() - 1).evaluate();
    value = Math.round(value * 100) / 100.0;
    initialValue = value;
    companies.add(alpha.getSymbol());

  }

  /**
   * Checks if the portfolio already contains a stock with the specified symbol.
   *
   * @param symbol The stock's ticker symbol to check.
   * @return true if the stock is already in the portfolio, false otherwise.
   */
  public boolean hasCompany(String symbol) {
    return companies.contains(symbol);
  }

  /**
   * Checks if the portfolio has been sold.
   *
   * @return true if the portfolio is marked as sold, false otherwise.
   */
  public boolean isSold() {
    return this.sold;
  }

}
