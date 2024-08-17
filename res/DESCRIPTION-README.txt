# CS5010_Spring_2024_Group-Work-Assignment-4 Description-Readme

## Contributors: Hao Jin & Hongzhen Xu

### Project Overview:

This Java-based investment portfolio manager application stands out for its flexibility and
user-friendliness, enabling users not just to create and manage diverse investment portfolios, but
also to integrate seamlessly with real-time and historical stock data through the Alpha Vantage API.
With its dual-interface approach, users can manage stocks within portfolios, access historical
performance data, dynamically track portfolio valuation, and much more, through both a command-line
and a graphical user interface (GUI).

### Features and Completeness:

* __Intuitive GUI for Portfolio Management:__ Beyond traditional command-line operations, our GUI
  provides a more accessible and engaging platform for managing portfolios. Users can effortlessly
  create new portfolios, add or remove stocks, and overview their investments with a few clicks.

* __Real-Time Stock Data Integration:__ Leveraging the Alpha Vantage API, the application fetches
  real-time stock data, allowing users to make informed decisions based on the latest market trends,
  all through an intuitive graphical interface.

* __Historical Data Analysis and Visualization:__ The GUI facilitates querying historical values of
  portfolios or stocks on specific dates, presenting this data in easy-to-understand charts and
  graphs for better analysis and decision-making.

* __Dynamic Portfolio Valuation and Visualization:__ Users can visualize the real-time valuation of
  their portfolios through the GUI, with updates reflecting current market conditions for a
  comprehensive financial overview.

* __Persistent Portfolio Configuration:__ Through both interfaces, users can save their portfolio
  configurations and data for future access, with the GUI simplifying the process of saving and
  loading these configurations.

* __Advanced Features:__ The GUI also introduces advanced functionalities like statistical displays,
  detailed chart visualizations, and flexible portfolio stock management (addition and selling),
  enhancing the user's interaction with their investment data.

### Menu and Choices

With a comprehensive menu, the application offers a wide range of functionalities, now more
accessible through the GUI:

"Please select an option:\n"
"1. Create a new simple portfolio\n"
"2. Create a flexible portfolio\n"
"3. View all portfolios\n"
"4. View specific portfolio details\n"
"5. Add stock to a flexible portfolio\n"
"6. Sell stock of a flexible portfolio\n"
"7. View cost basis of a flexible portfolio\n"
"8. View value of a flexible portfolio by date\n"
"9. Get value of simple portfolio at certain date\n"
"10. Sell a simple portfolio or Delete a flexible portfolio\n"
"11. Update portfolio values\n"
"12. save a portfolio\n"
"13. load a portfolio\n"
"14. Stock gain or lose on a certain day\n"
"15. Stock gain or lose over period of time\n"
"16. Stock x day moving average\n"
"17. Stock crossovers for price and 30 day moving average\n"
"18. Stock moving crossovers for x day and y day moving average\n"
"19. Generate value chart either for a stock or a portfolio\n"
"20. Weight Investment on specific day"
"21. Weight investment on given period with frequency"
"22. Exit"

### Known Limitations and Future Directions:

* The application requires an active internet connection for real-time data fetching.
* It relies on the Alpha Vantage API for stock data, which may limit data availability for certain symbols and is
  subject to daily API call limits.
* Future updates aim to enhance the GUI for broader accessibility,
  introduce more advanced analysis tools, and expand data sources beyond Alpha Vantage for a more
  comprehensive investment management experience.