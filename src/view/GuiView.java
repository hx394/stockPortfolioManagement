package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JPanel;

import controller.Features;

/**
 * GuiView class extends JFrame and implements GUI interface to provide a graphical
 * user interface for the Investment Portfolio Manager application.
 * It initializes and displays UI components such as buttons, text areas,
 * and panels for user interaction.
 */
public class GuiView extends JFrame implements GUI {
  private JPanel newButtonPanel;
  private JTextArea jta;
  private JTextField textInput;
  private JButton buyStockButton;
  private JButton sellStockButton;
  private JButton costBasisButton;
  private JButton valueAtDateButton;
  private JButton viewAllPortfoliosButton;
  private JButton addSuperPortfolioButton;
  private JButton viewPortfolioDetailsButton;
  private JButton updatePortfoliosButton;
  private JButton savePortfolioButton;
  private JButton loadPortfolioButton;
  private JButton weightInvestmentButton;
  private JButton dollarCostAveragingButton;
  private JButton queryStatisticsButton;
  private JButton confirmInputButton;
  private JPanel buttonPanel;
  private JPanel textPlace;
  private String textType;

  /**
   * Constructs a GuiView object initializing the main window of
   * the application with all its UI components.
   */
  public GuiView() {
    super("Investment Portfolio Manager");

    setSize(800, 600);

    setLocation(200, 200);

    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    Dimension dimension = new Dimension(800, 600);
    setMaximumSize(dimension);
    initializeUI();
  }

  /**
   * Initializes and arranges all UI components including buttons, text area, and panels.
   */
  private void initializeUI() {
    System.out.println("initialize UI");
    this.setLayout(new FlowLayout());
    jta = new JTextArea(30, 40);
    jta.setText("to be displayed");
    jta.setEditable(false);

    JScrollPane scrollPane = new JScrollPane(jta);

    scrollPane.setHorizontalScrollBarPolicy(
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setVerticalScrollBarPolicy(
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

    this.add(scrollPane);


    buttonPanel = new JPanel();
    buttonPanel.setSize(new Dimension(200, 300));
    buttonPanel.setLayout(new GridLayout(0, 1));


    confirmInputButton = new JButton("Submit");
    confirmInputButton.setActionCommand("Submit");

    initializeButtons1();
    initializeButtons2();
    this.add(buttonPanel);
    pack();
    if (this.getHeight() > 600) {
      this.setSize(this.getWidth(), 600);
    }
    setVisible(true);
  }

  /**
   * initialize buttons.
   */
  private void initializeButtons1() {
    addSuperPortfolioButton = new JButton(
            "Add Flexible Portfolio");
    addSuperPortfolioButton.setActionCommand(
            "Add Flexible Portfolio");
    buttonPanel.add(addSuperPortfolioButton);

    buyStockButton = new JButton(
            "Buy stock at specified date");
    buyStockButton.setActionCommand(
            "Buy stock at specified date");
    buttonPanel.add(buyStockButton);

    sellStockButton = new JButton("Sell stock at specified date");
    sellStockButton.setActionCommand("Sell stock at specified date");
    buttonPanel.add(sellStockButton);

    costBasisButton = new JButton("Cost basis of portfolio");
    costBasisButton.setActionCommand("Cost basis of portfolio");
    buttonPanel.add(costBasisButton);

    valueAtDateButton = new JButton("Value at specified date");
    valueAtDateButton.setActionCommand("Value at specified date");
    buttonPanel.add(valueAtDateButton);

    savePortfolioButton = new JButton("Save Portfolio");
    savePortfolioButton.setActionCommand("Save Portfolio");
    buttonPanel.add(savePortfolioButton);

    loadPortfolioButton = new JButton("Load Portfolio");
    loadPortfolioButton.setActionCommand("Load Portfolio");
    buttonPanel.add(loadPortfolioButton);
  }

  /**
   * initialize buttons.
   */
  private void initializeButtons2() {
    queryStatisticsButton = new JButton(
            "query statistics of a stock");
    queryStatisticsButton.setActionCommand(
            "query statistics of a stock");
    buttonPanel.add(queryStatisticsButton);

    weightInvestmentButton = new JButton(
            "weight investment on existing portfolio");
    weightInvestmentButton.setActionCommand(
            "weight investment on existing portfolio");
    buttonPanel.add(weightInvestmentButton);

    dollarCostAveragingButton = new JButton(
            "dollar cost averaging on existing portfolio");
    dollarCostAveragingButton.setActionCommand(
            "dollar cost averaging on existing portfolio");
    buttonPanel.add(dollarCostAveragingButton);


    viewAllPortfoliosButton = new JButton("View All Portfolios");
    viewAllPortfoliosButton.setActionCommand("View All Portfolios");
    buttonPanel.add(viewAllPortfoliosButton);


    viewPortfolioDetailsButton = new JButton("View Portfolio Details");
    viewPortfolioDetailsButton.setActionCommand("View Portfolio Details");
    buttonPanel.add(viewPortfolioDetailsButton);
  }

  @Override
  public void createTextField(String order, String type) {
    this.textType = type;
    this.remove(buttonPanel);
    textPlace = new JPanel();
    textInput = new JTextField(10);
    textPlace.add(textInput);
    confirmInputButton.setActionCommand(order);
    textPlace.add(confirmInputButton);
    this.add(textPlace);


    this.pack();
    if (this.getHeight() > 600) {
      this.setSize(this.getWidth(), 600);
    }
    this.revalidate();
    this.repaint();
  }

  @Override
  public void clearDisplay() {
    jta.setText("");
    jta.setEditable(false);
  }

  @Override
  public void createMultipleTextField(String order,
                                      ArrayList<String> list, String type, Features features) {
    displayMessage("Please input percentage for each stock symbol:");
    this.textType = type;
    this.remove(buttonPanel);
    textPlace = new JPanel();
    for (int i = 0; i < list.size(); i++) {
      JLabel name = new JLabel(list.get(i));
      textPlace.add(name);
      textInput = new JTextField(10);
      textPlace.add(textInput);
    }
    addSubmitButton(features, list, order);
    this.pack();
    if (this.getHeight() > 600) {
      this.setSize(this.getWidth(), 600);
    }
    this.revalidate();
    this.repaint();
  }

  /**
   * helper add submit button.
   *
   * @param features Features
   * @param list     array list
   * @param order    command
   */
  private void addSubmitButton(Features features, ArrayList<String> list, String order) {
    JButton submit = new JButton("submit");
    submit.setActionCommand(order);
    submit.addActionListener(evt -> {
      double total = 1;
      HashMap<String, Double> symbolPercentList = new HashMap<>();
      ArrayList<Double> percentList = new ArrayList<>();
      for (Component cp : textPlace.getComponents()) {
        if (cp instanceof JTextField) {
          String text = ((JTextField) cp).getText();
          try {
            double current = Double.parseDouble(text);
            if (current < 0) {
              setDisplayMessage("Cannot use smaller than 0 percent.");
              return;
            }
            if (current > Math.round(total * 100) / 100.0) {
              setDisplayMessage("cannot use percent sum bigger than 1");
              return;
            }
            percentList.add(current);
            total = total - current;
          } catch (NumberFormatException e) {
            setDisplayMessage(text + " is not a double");
            return;
          }
        }
      }
      if (Math.round(total * 100) != 0) {
        setDisplayMessage("The sum is not 1.");
        return;
      }
      for (int i = 0; i < list.size(); i++) {
        symbolPercentList.put(list.get(i), percentList.get(i));
      }
      features.setSymbolPercentList(symbolPercentList);
      this.remove(textPlace);
      features.inputMoney();
    });
    textPlace.add(submit);
    this.add(textPlace);
  }

  @Override
  public void createButtonPanel(String order, String type,
                                ArrayList<String> arraylist,
                                Features features, int countOfSimple) {
    //System.out.println("createBUttonPanel called");
    this.remove(buttonPanel);
    newButtonPanel = new JPanel();
    for (int i = 0; i < arraylist.size(); i++) {
      JButton bt = new JButton(arraylist.get(i));
      group1(type, bt, features, order, i, countOfSimple);
      group2(type, order, features, arraylist, bt, i);
      if ((order.equals("weightInvestment")
              || order.equals("weightInvestmentOverPeriod"))
              && type.equals("superPortfolioNumber")) {
        int portfolioNum = i + 1 + countOfSimple;
        bt.setActionCommand(order);
        bt.addActionListener(evt -> {
          features.savePortfolioNum(portfolioNum);
          this.remove(newButtonPanel);
          jta.setText("");
          jta.setEditable(false);
          features.inputSymbolList();
        });
      }
      if (order.equals("query")) {
        newButtonPanel.setLayout(new GridLayout(0, 1));
        bt.setActionCommand(arraylist.get(i));
        int num = i;
        bt.addActionListener(evt -> {
          this.remove(newButtonPanel);
          features.query(num);
        });
      }
      newButtonPanel.add(bt);
    }
    this.add(newButtonPanel);
    this.pack();
    if (this.getHeight() > 600) {
      this.setSize(this.getWidth(), 600);
    }
    this.revalidate();
    this.repaint();
  }

  /**
   * helper function for group 1.
   *
   * @param type          type
   * @param bt            button
   * @param features      features
   * @param order         order
   * @param i             current iteration index
   * @param countOfSimple count of simple portfolio
   */
  private void group1(String type, JButton bt, Features features, String order, int i,
                      int countOfSimple) {
    if (type.equals("YesNo")) {

      int num = i;
      bt.setActionCommand(order);
      bt.addActionListener(evt -> {
        this.remove(newButtonPanel);
        features.chooseYesNo(num);

      });
    }
    if (order.equals("viewPortfolioDetail") && type.equals("portfolioNumber")) {
      int portfolioNum = i + 1;
      bt.addActionListener(evt -> features.viewPortfolioDetails(portfolioNum));
    }
    if ((order.equals("buyStock")
            || order.equals("sellStock"))
            && type.equals("superPortfolioNumber")) {
      int portfolioNum = i + 1 + countOfSimple;
      bt.setActionCommand(order);
      bt.addActionListener(evt -> {
        features.savePortfolioNum(portfolioNum);
        this.remove(newButtonPanel);
        features.inputSymbol(evt.getActionCommand());
      });
    }
    if ((order.equals("costBasis")
            || order.equals("valueAtSpecificDate"))
            && type.equals("superPortfolioNumber")) {
      int portfolioNum = i + 1 + countOfSimple;
      bt.setActionCommand(order);
      bt.addActionListener(evt -> {
        features.savePortfolioNum(portfolioNum);
        this.remove(newButtonPanel);
        jta.setText("");
        jta.setEditable(false);
        features.inputTime();
      });
    }
  }

  /**
   * helper function for group 2.
   *
   * @param type      type
   * @param order     order
   * @param features  features
   * @param arraylist arraylist
   * @param bt        button
   * @param i         current iteration index
   */
  private void group2(String type, String order, Features features,
                      ArrayList<String> arraylist, JButton bt, int i) {
    if (type.equals("year")) {

      newButtonPanel.setLayout(new GridLayout(0, 6));
      int year = Integer.parseInt(arraylist.get(i));
      bt.setActionCommand(order);
      bt.addActionListener(evt -> {
        features.setYear(year);
        this.remove(newButtonPanel);
        features.inputTime();
      });
    }
    if (type.equals("month")) {
      newButtonPanel.setLayout(new GridLayout(0, 4));
      int month = Integer.parseInt(arraylist.get(i));
      bt.setActionCommand(order);
      bt.addActionListener(evt -> {
        features.setMonth(month);
        this.remove(newButtonPanel);
        features.inputTime();
      });
    }
    if (type.equals("day")) {
      newButtonPanel.setLayout(new GridLayout(0, 7));
      int day = Integer.parseInt(arraylist.get(i));
      bt.setActionCommand(order);
      bt.addActionListener(evt -> {
        features.setDay(day);
        this.remove(newButtonPanel);
        features.inputTime();
      });
    }
    if (order.equals("savePortfolio")) {
      int portfolioNum = i + 1;
      bt.addActionListener(evt -> features.savePortfolioDetails(portfolioNum));
    }
  }

  @Override
  public void displayPortfolios(String portfoliosInfo) {
    setDisplayMessage(portfoliosInfo);
  }

  @Override
  public void displayPortfolioDetails(String portfolioDetails) {
    setDisplayMessage(portfolioDetails);
  }

  @Override
  public void displayMessage(String message) {
    setDisplayMessage(message);

  }

  @Override
  public void displayErrorMessage(String errorMessage) {
    setDisplayMessage(errorMessage);
  }

  @Override
  public void readUserInput(String prompt) {
    setDisplayMessage(getInputString());
  }

  @Override
  public void setListener(ActionListener listener) {
    confirmInputButton.addActionListener(listener);
  }

  @Override
  public void addFeatures(Features features) {
    System.out.println("add features");

    buyStockButton.addActionListener(evt -> features.buyStock());

    sellStockButton.addActionListener(evt -> features.sellStock());

    costBasisButton.addActionListener(evt -> features.costBasis());

    valueAtDateButton.addActionListener(evt -> features.valueAtDate());

    viewAllPortfoliosButton.addActionListener(evt -> features.viewAllPortfolios());

    addSuperPortfolioButton.addActionListener(evt -> features.inputNameForSuperPortfolio());

    viewPortfolioDetailsButton.addActionListener(evt -> features.inputPortfolioNumber(
            "viewPortfolioDetail"));

    savePortfolioButton.addActionListener(evt -> features.savePortfolio());

    loadPortfolioButton.addActionListener(evt -> features.loadPortfolio());

    weightInvestmentButton.addActionListener(evt -> features.weightInvestment());

    dollarCostAveragingButton.addActionListener(evt -> features.dollarCostAveraging());

    queryStatisticsButton.addActionListener(evt -> features.queryStatistics());

  }

  @Override
  public String getDisplayMessage() {
    return jta.getText();
  }

  @Override
  public void setDisplayMessage(String s) {
    jta.setText(s);
    jta.setEditable(false);


    this.pack();
    if (this.getHeight() > 600) {
      this.setSize(this.getWidth(), 600);
    }

    this.revalidate();
    this.repaint();
  }

  @Override
  public String getInputString() {
    return textInput.getText();
  }

  @Override
  public void clearInputString() {
    textInput.setText("");
  }

  @Override
  public void shutTextField() {
    this.remove(textPlace);
    this.revalidate();
    this.repaint();
  }

  @Override
  public void showMenu() {
    this.add(buttonPanel);

    this.pack();
    if (this.getHeight() > 600) {
      this.setSize(this.getWidth(), 600);
    }
    this.revalidate();
    this.repaint();
  }

  @Override
  public String getTextType() {
    return textType;
  }

  @Override
  public void removeNewButtonPanel() {
    this.remove(newButtonPanel);
  }
}
