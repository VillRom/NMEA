import net.sf.marineapi.nmea.sentence.Sentence;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.pegas_agro.implement.ReadLogFileImpl;
import ru.pegas_agro.service.ReadLogFile;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ReadLogFileTest {

    private final ReadLogFile readLogFile = new ReadLogFileImpl();

    @Test
    public void splitByDaysTest() {
        HashMap<Date, List<Sentence>> sentenceForDays = readLogFile.splitByDays();
        Assertions.assertNotEquals(0, sentenceForDays.size(), "Мапа пустая");
        Assertions.assertEquals(2, sentenceForDays.size(), "Размер мапы не равен 2");
    }
}
