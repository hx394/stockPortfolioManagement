package view;

import java.awt.event.ActionListener;
import java.util.ArrayList;

import controller.Features;

/**
 * Defines the graphical user interface (GUI) operations for the application.
 * This interface extends the View interface to include GUI-specific methods,
 * such as creating text fields, clearing displays, and setting listeners.
 */
public interface GUI extends View {

  /**
   * Creates a single text field for input.
   *
   * @param order Identifies the operation for which the text field is created.
   * @param type  The type of data expected from the user.
   */
  void createTextField(String order, String type);

  /**
   * Clears the current display in the GUI.
   */
  void clearDisplay();

  /**
   * Creates multiple text fields for input based on a list.
   *
   * @param order    Identifies the operation for which the text fields are created.
   * @param list     A list of labels for the text fields.
   * @param type     The type of data expected in each text field.
   * @param features The features controller to handle user input.
   */
  void createMultipleTextField(String order, ArrayList<String> list, String type,
                               Features features);

  /**
   * Creates a panel of buttons for the GUI.
   *
   * @param order         Identifies the operation for which the buttons are created.
   * @param type          The type of operation each button represents.
   * @param arrayList     A list of labels for the buttons.
   * @param features      The features controller to handle button actions.
   * @param countOfSimple The number of simple portfolios,
   *                      if relevant to button functionality.
   */
  void createButtonPanel(String order, String type, ArrayList<String> arrayList,
                         Features features, int countOfSimple);

  /**
   * Sets an ActionListener for the GUI.
   *
   * @param listener The listener to handle actions.
   */
  void setListener(ActionListener listener);

  /**
   * Adds feature controls to the GUI.
   *
   * @param features The features implementation to use for control logic.
   */
  void addFeatures(Features features);

  /**
   * Gets the current message displayed on the GUI.
   *
   * @return The displayed message.
   */
  String getDisplayMessage();

  /**
   * Sets the message to be displayed on the GUI.
   *
   * @param s The message to display.
   */
  void setDisplayMessage(String s);

  /**
   * Retrieves the current string input from the GUI.
   *
   * @return The input string.
   */
  String getInputString();

  /**
   * Clears the current input string in the GUI.
   */
  void clearInputString();

  /**
   * Disables the text field input in the GUI.
   */
  void shutTextField();

  /**
   * Displays the main menu on the GUI.
   */
  void showMenu();

  /**
   * Gets the type of text expected in the GUI input field.
   *
   * @return The text type.
   */
  String getTextType();

  /**
   * Removes the panel of new buttons from the GUI.
   */
  void removeNewButtonPanel();
}
