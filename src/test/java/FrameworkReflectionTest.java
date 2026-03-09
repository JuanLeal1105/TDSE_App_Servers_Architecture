import org.example.demo.GetMapping;
import org.example.demo.GreetingController;
import org.example.demo.RestController;
import org.example.demo.api.RequestParam;
import org.junit.Test;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import static org.junit.Assert.*;

public class FrameworkReflectionTest {

    @Test
    public void testControllerIsAnnotatedForDiscovery() {
        assertTrue("GreetingController must be annotated with @RestController to be discovered", GreetingController.class.isAnnotationPresent(RestController.class));
    }

    @Test
    public void testGetMappingRegistersCorrectUri() throws NoSuchMethodException {
        Method method = GreetingController.class.getDeclaredMethod("greeting", String.class);

        assertTrue("Method must have @GetMapping annotation", method.isAnnotationPresent(GetMapping.class));
        GetMapping mapping = method.getAnnotation(GetMapping.class);
        assertEquals("The framework should map this method to /greeting", "/greeting", mapping.value());
    }

    @Test
    public void testRequestParamExtractsMetadata() throws NoSuchMethodException {
        Method method = GreetingController.class.getDeclaredMethod("greeting", String.class);
        Parameter parameter = method.getParameters()[0];

        assertTrue("Parameter must have @RequestParam annotation", parameter.isAnnotationPresent(RequestParam.class));
        RequestParam reqParam = parameter.getAnnotation(RequestParam.class);
        assertEquals("The parameter key should be 'name'", "name", reqParam.value());
        assertEquals("The framework should fall back to 'World'", "World", reqParam.defaultValue());
    }
}
