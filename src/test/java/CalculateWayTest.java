import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.pegas_agro.implement.CalculateWayImpl;
import ru.pegas_agro.service.CalculateWay;


public class CalculateWayTest {

    CalculateWay calculate = new CalculateWayImpl();

    @Test
    public void calculateWayTest() {
        String s = calculate.calculateWay();
        Assertions.assertNotNull(s, "Строка равна null");
        Assertions.assertNotEquals("", s, "Строка пустая");
    }
}
