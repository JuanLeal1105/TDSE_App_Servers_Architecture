import org.example.demo.GreetingController;
import org.junit.Test;
import static org.junit.Assert.*;

public class GreetingControllerTest {

    @Test
    public void shouldReturnGreetingAndIncrementCounter() {
        GreetingController controller = new GreetingController();

        String response1 = controller.greeting("Profesor");
        assertTrue(response1.contains("Profesor"));
        assertTrue(response1.contains("Request #1"));

        String response2 = controller.greeting("Juan");
        assertTrue(response2.contains("Juan"));
        assertTrue(response2.contains("Request #2"));
    }
}