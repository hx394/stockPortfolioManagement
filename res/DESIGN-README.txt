# Design-Readme

## Contributors: Hao Jin & Hongzhen Xu

### Architecture Overview:

The application is designed following the Model-View-Controller (MVC) architecture pattern, ensuring
a clean separation of concerns. This approach simplifies management and enhances the scalability of
the application.

### Major Components:

* __Model:__ Manages business logic and data. It includes classes for portfolios (Portfolio,
  SuperPortfolio) and stocks (Stock).
* __View:__ Engages the user through two distinct interfaces:
    * __Graphical User Interface (GUI):__ Powered by the GuiView class, it offers an intuitive and
      interactive experience, allowing users to visually manage their portfolios, view statistics,
      and analyze stock data.
    * __Text Interface:__ Managed by the ViewImp class, it provides a command-line alternative for
      users preferring or requiring a non-graphical interface.
* __Controller:__ Bridges the Model and View by handling user inputs and updating the view based on
  model changes. It includes:
    * __ControllerImp__ for coordinating the application logic in the text interface.
    * __GuiControllerImpl__ specifically tailored for handling actions within the GUI, ensuring
      responsive and user-friendly navigation.

### Enhanced Focus on GUI:

The GUI aspect of the application, managed through the GuiView and GuiControllerImpl classes, is a
significant enhancement aimed at providing a more accessible and engaging user experience. Key
features include:

* __Intuitive Navigation:__ The GUI offers an organized layout with buttons and menus for
  straightforward navigation, making it easy for users to explore different functionalities.

* __Dynamic Data Visualization:__ Users can generate and view charts within the GUI, offering visual
  insights into stock performance and portfolio valuation over time.

* __Real-time Interaction:__ The GUI responds to user actions in real time, allowing for immediate
  feedback and updates based on the latest financial data.

* __Accessibility:__ Designed to be user-friendly, the GUI caters to both novice and experienced
  investors, making complex investment management tasks more approachable.

### Key Design Choices:

* __API Integration:__ The Alpha Vantage API is integrated for fetching real-time and historical
  stock data, abstracted through the AlphaVantageDemo class.
* __Flexible Portfolio Management:__ Through dynamic portfolio creation and customization options,
  users can tailor their investment strategies to meet personal financial goals.
* __Scalability and Extensibility:__ The application's architecture supports easy addition of new
  features and integration with other financial tools, laying the groundwork for future
  enhancements.

### Design Changes from Original Design Choices:

* __Enhanced GUI Features:__ Continued development will introduce more interactive elements,
  improving data presentation and user engagement.


This updated design readme emphasizes the significant role of the GUI in enriching the user
experience within the investment portfolio application. By combining advanced data management
capabilities with user-friendly navigation and visualization, the application aims to democratize
investment analysis and portfolio management.


Because of the assignment requirement, and the original controller can not deal with button callbacks,
we have to make these changes. This is the justification.