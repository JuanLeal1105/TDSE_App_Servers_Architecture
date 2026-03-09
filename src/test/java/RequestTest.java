import org.example.demo.api.Request;
import org.junit.Test;
import static org.junit.Assert.*;

public class RequestTest {
    @Test
    public void shouldParseMultipleQueryParams() {
        Request req = new Request("/greeting", "name=Juan&role=Admin");
        assertEquals("Juan", req.getQueryParam("name"));
        assertEquals("Admin", req.getQueryParam("role"));
    }

    @Test
    public void shouldHandleNullOrEmptyQueries() {
        Request reqNull = new Request("/hello", null);
        assertNull("Should handle null query gracefully without crashing", reqNull.getQueryParam("name"));

        Request reqEmpty = new Request("/hello", "");
        assertNull("Should handle empty query gracefully", reqEmpty.getQueryParam("name"));
    }

    @Test
    public void shouldHandleMalformedQueries() {
        Request req = new Request("/api", "badQueryWithoutEquals");
        assertNull("Should ignore malformed query strings", req.getQueryParam("badQueryWithoutEquals"));
    }
}
