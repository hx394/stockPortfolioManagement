import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import controller.AlphaVantageDemo;
import controller.Controller;
import controller.ControllerImp;
import model.Model;
import model.ModelImp;
import view.TextView;
import view.ViewImp;

import static org.junit.Assert.assertTrue;

/**
 * this is a test class for chart generating tests.
 */
public class GeneralTest {
  private final PrintStream originalOut = System.out;
  private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private Model model;
  private TextView view;

  /**
   * Set up common test fixtures.
   * Initializes model and view, and redirects system output to capture results.
   */
  @Before
  public void setUp() {
    model = new ModelImp();

    view = new ViewImp(new PrintStream(outContent));
  }


  /**
   * Simulates user input and runs the controller to process the input.
   *
   * @param inputData A string representing user input.
   */
  private void simulateInputAndRunController(String inputData) {
    ByteArrayInputStream inContent = new ByteArrayInputStream(inputData.getBytes());
    Controller controller = new ControllerImp(model, view, inContent, new PrintStream(outContent),
            new AlphaVantageDemo());
    controller.toGo();
  }


  @Test
  public void testGenerateChartForStock() {
    String input = "19\n1\nAAPL\n2024-03-12\n2024-03-24\n20\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();

    assertTrue(output.contains("Performance of Portfolio from 2024-03-12 to 2024-03-24\n"
            + "2024-03-12: ************************************************\n"
            + "2024-03-13: ***********************************************\n"
            + "2024-03-14: ************************************************\n"
            + "2024-03-15: ************************************************\n"
            + "2024-03-16: ************************************************\n"
            + "2024-03-17: ************************************************\n"
            + "2024-03-18: ************************************************\n"
            + "2024-03-19: *************************************************\n"
            + "2024-03-20: **************************************************\n"
            + "2024-03-21: ***********************************************\n"
            + "2024-03-22: ************************************************\n"
            + "2024-03-23: ************************************************\n"
            + "2024-03-24: ************************************************\n"
            + "Scale: * = 3.5734 units"));
  }

  @Test
  public void testGenerateChartForNonExistStock() {
    String input = "19\n1\n12345321232131ABC\nq\n20\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    assertTrue(output.contains("Error: Ticker symbol not found."));
  }

  @Test
  public void testGenerateChartForStockForInvalidDate1() {
    String input = "19\n1\nAAPL\n2024-03-12\n2024-03-11\n20\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    assertTrue(output.contains("Error: Start date must be before end date."));
  }

  @Test
  public void testGenerateChartForStockForInvalidDate2() {
    String input = "19\n1\nAAPL\n3024-03-10\n3024-03-11\n20\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();

    assertTrue(output.contains("Error: Invalid date range."));
  }

  @Test
  public void testGenerateChartForStockForInvalidDate3() {
    String input = "19\n1\nAAPL\n1024-03-02\n1024-03-11\n20\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    assertTrue(output.contains("Error: Invalid date range."));
  }

  @Test
  public void testGenerateChartForStockForSingleNonbusinessDate() {
    String input = "19\n1\nAAPL\n2024-03-10\n2024-03-10\n20\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();

    assertTrue(output.contains("Performance of Portfolio from 2024-03-10 to 2024-03-10\n"
            + "2024-03-10: **************************************************\n"
            + "Scale: * = 3.4145999999999996 units"));
  }

  @Test
  public void testGenerateChartForStockForSingleDate() {
    String input = "19\n1\nAAPL\n2024-03-12\n2024-03-12\n20\n";
    simulateInputAndRunController(input);
    String output = outContent.toString().trim();
    System.out.println(output);
    assertTrue(output.contains("Performance of Portfolio from 2024-03-12 to 2024-03-12\n"
            + "2024-03-12: **************************************************\n"
            + "Scale: * = 3.4646 units"));
  }
}
