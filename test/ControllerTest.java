import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import controller.AlphaVantageDemo;
import controller.Controller;
import controller.ControllerImp;
import model.Model;
import model.ModelImp;
import view.TextView;
import view.ViewImp;

import static org.junit.Assert.assertEquals;

/**
 * Tests the {@link ControllerImp} class for proper handling of user interactions.
 * Simulates user input via {@link ByteArrayInputStream}
 * and captures output with {@link ByteArrayOutputStream}.
 */
public class ControllerTest {

  /**
   * Simulates a user choosing to exit the application and verifies the output.
   * This test ensures the application displays the correct menu options
   * and handles the exit command as expected.
   */
  @Test
  public void testController() {
    String input = "22\n";
    InputStream in;
    OutputStream out;
    in = new ByteArrayInputStream(input.getBytes());
    out = new ByteArrayOutputStream();

    Model m = new ModelImp();
    TextView v = new ViewImp(new PrintStream(out));
    Controller c = new ControllerImp(m, v, in, new PrintStream(out), new AlphaVantageDemo());
    c.toGo();
    String output = out.toString().trim().replace("\r\n", "\n");
    assertEquals("Please select an option:\n"
            + "1. Create a new simple portfolio\n"
            + "2. Create a flexible portfolio\n"
            + "3. View all portfolios\n"
            + "4. View specific portfolio details\n"
            + "5. Add stock to a flexible portfolio\n"
            + "6. Sell stock of a flexible portfolio\n"
            + "7. View cost basis of a flexible portfolio\n"
            + "8. View value of a flexible portfolio by date\n"
            + "9. Get value of simple portfolio at certain date\n"
            + "10. Sell a simple portfolio or Delete a flexible portfolio\n"
            + "11. Update portfolio values\n"
            + "12. save a portfolio\n"
            + "13. load a portfolio\n"
            + "14. Stock gain or lose on a certain day\n"
            + "15. Stock gain or lose over period of time\n"
            + "16. Stock x day moving average\n"
            + "17. Stock crossovers for price and 30 day moving average\n"
            + "18. Stock moving crossovers for x day and y day moving average\n"
            + "19. Generate value chart either for a stock or a portfolio\n"
            + "20. Exit\n"
            + "Enter your choice: Input number is:20\n"
            + "Exiting...", output);
  }
}
