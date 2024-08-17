import java.util.InputMismatchException;
import java.util.Scanner;

import controller.AlphaVantageDemo;
import controller.ControllerImp;
import controller.GUIController;
import controller.GuiControllerImpl;
import model.ModelImp;
import view.GUI;
import view.GuiView;
import view.ViewImp;

/**
 * Main class to run the MVC application.
 * Initializes the Model, View, and Controller components and starts the application flow.
 */
class ProgramRunner {
  /**
   * The main method that runs the program.
   *
   * @param args String arguments
   */
  public static void main(String[] args) {
    //set up before if needed
    ModelImp modelImp = new ModelImp();
    int choice;
    Scanner in = new Scanner(System.in);
    while (true) {

      try {
        System.out.println("Please select GUI or Text Interface, 1 for text,2 for GUI.");
        choice = in.nextInt();
      } catch (InputMismatchException e) {
        System.out.println("Invalid input string for choice.");
        in.nextLine();
        continue;
      }
      if (choice <= 0 || choice > 2) {
        System.out.println("Invalid choice.");
        in.nextLine();
        continue;
      }
      break;
    }
    if (choice == 1) {
      //setup details of view if needed
      ViewImp viewImp = new ViewImp(System.out);

      //create controller, give it the model and view
      ControllerImp controllerImp = new ControllerImp(modelImp, viewImp, System.in, System.out,
              new AlphaVantageDemo());
      controllerImp.toGo();
    } else {

      GUIController c = new GuiControllerImpl(modelImp, new AlphaVantageDemo());
      GUI view = new GuiView();
      c.setView(view);
    }
    //give control to the controller. Controller relinquishes only when program ends

  }
}