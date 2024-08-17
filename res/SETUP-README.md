# SETUP-README

## Contributors: Hao Jin & Hongzhen Xu

### Running the Program:

1. Java Requirement: Make sure Java is installed on your system.
2. JAR File Placement: Place the application's JAR file in a desired directory.
3. Launching the Application:
    * Open a command line or terminal.
    * Navigate to the directory with the JAR file.
    * Run the application using: java -jar [JarFileName].jar.
    * Upon startup, select the GUI interface option to proceed with a more interactive and
      user-friendly experience.

### Creating Portfolios and Adding Stocks:

1. Create a new portfolio and assign it a name.
2. To add stocks to the portfolio:
    * Enter the stock symbol (e.g., "AAPL").
    * Specify the number of shares.
    * Provide the purchase date (YYYY-MM-DD).
3. Repeat for at least three different companies.
4. Exit stock addition mode with the 'q' command.

### Querying Portfolio Value on a Specific Date:

1. View details for a specific portfolio.
2. Query the portfolio's value or cost basis on specific dates.
3. Enter the date in YYYY-MM-DD format.

### Supported Stocks and Dates:

The program supports any stocks available through the Alpha Vantage API. However, for the sake of
demonstration, you can use these symbols:

* AAPL (Apple Inc.)
* MSFT (Microsoft Corporation)
* GOOG (Google Inc.)
* IBM (International Business Machines Corporation)

### Note:

Dates on which the value can be determined are limited to trading days. The Alpha Vantage API does
not provide data for weekends or public holidays.

Remember, the availability of data may vary based on API limitations and the API key's access level.

### Running Process:

1. choose 2: Create a flexible portfolio
2. input your username
3. choose 5: Add stock to a flexible portfolio
4. Enter portfolio number
5. Enter Stock Ticker: eg. AAPL
6. Input date as:YYYY-MM-DD
7. Enter shares number, must be integer
8. choose 5: Add another stock to a flexible portfolio
9. Enter same portfolio number
10. Enter Stock Ticker: eg. AAPL
11. Input another date as:YYYY-MM-DD
12. Enter another shares number, must be integer
13. choose 5: Add another stock to a flexible portfolio
14. Enter same portfolio number
15. Enter Stock Ticker: eg. AAPL
16. Input another date as:YYYY-MM-DD
17. Enter another shares number, must be integer
18. choose 8: View value of a flexible portfolio by date
19. Enter your portfolio number
20. input date as:YYYY-MM-DD
21. eg:2024-03-12 Date: 2024-03-12 Value at time 2024-03-12 for flexible portfolio 1 is:173230.0
22. Then choose 7 and input your portfolio number again
23. input the date of selection:
    eg:
24. Please input date as:YYYY-MM-DD 2024-03-25 Date: 2024-03-25 The cost basis for flexible
    portfolio 1 at time 2024-03-25 is:361831.21

### Using the GUI for Portfolio Management:

Creating and Managing Portfolios:

* __Creating a Portfolio:__ Use the GUI to easily create new simple or flexible portfolios with
  intuitive prompts and input fields.
* __Adding Stocks to Portfolio:__
    * Select a portfolio and use the graphical interface to add stocks.
    * Input the stock symbol, number of shares, and purchase date through dedicated fields.
* __Stock and Portfolio Visualization:__ View portfolio summaries, detailed stock information, and
  performance charts directly within the GUI, enhancing decision-making and portfolio management.

Querying and Visualizing Data:

* __Real-time and Historical Data:__ Access real-time updates and historical data visualization for
  stocks and portfolios, utilizing the integrated Alpha Vantage API.
* __Value and Cost Basis Queries:__
  Query the value or cost basis of portfolios on specific dates using an interactive calendar
  selection, simplifying the process of financial analysis.

### Running Process with GUI Highlights:

1. Upon launching the GUI, choose to create a new flexible portfolio. 
2. Enter a name for your portfolio using the GUI's text input. 
3. Add stocks to your portfolio by clicking on the "Add stock
   to a flexible portfolio" button and filling in the required information. 
4. To view the value of your portfolio on a specific date, select the "View value of a flexible portfolio by date" option
   and use the date picker for convenience. 
5. Similarly, to check the cost basis, select the relevant option from the GUI and specify the date.

### Additional Features and GUI Enhancements:

* __Portfolio Persistence:__ Save and load portfolio configurations effortlessly through the GUI. 
* __Advanced Visualization:__ Explore advanced features like statistical analyses for insightful investment strategies.