package controller;

import java.awt.event.ActionListener;

import view.GUI;

/**
 * The {@code GUIController} interface extends the {@code ActionListener} interface
 * to define the responsibilities of a controller
 * in a graphical user interface (GUI) context.
 * It outlines the essential functionalities a GUI controller must provide,
 * particularly in managing the interactions between the user interface
 * and the underlying application logic.
 * This interface mandates the implementation of methods to set up and manipulate
 * the GUI components based on user actions.
 */
public interface GUIController extends ActionListener {
  /**
   * Sets the view component for the controller.
   * This method binds a specific GUI view to the controller,
   * enabling the controller to update the view in response to user actions
   * or changes in the application state.
   *
   * @param v the GUI view to be set for this controller.
   */
  void setView(GUI v);
}
