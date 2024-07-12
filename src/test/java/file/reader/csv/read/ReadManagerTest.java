package file.reader.csv.read;

import file.reader.csv.storage.Year;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class ReadManagerTest {
    @Test
    void shouldReadDifferentCities(){
        Path fileToRead = new File(this.getClass().getClassLoader().getResource("different_city.csv").getFile()).toPath();
        ReadManager readManager = new ReadManager(fileToRead, StandardCharsets.UTF_8);

        ReadResult result = readManager.readFile();

        assertThat(result.successful()).isTrue();
        assertThat(result.data()).hasSize(2)
                .containsEntry("Warszawa", List.of(new Year(2023, new BigDecimal("13.54"))))
                .containsEntry("Kraków", List.of(new Year(2018, new BigDecimal("18.90"))));
    }

    @Test
    void shouldReadDifferentYears(){
        Path fileToRead = new File(this.getClass().getClassLoader().getResource("different_years.csv").getFile()).toPath();
        ReadManager readManager = new ReadManager(fileToRead, StandardCharsets.UTF_8);

        ReadResult result = readManager.readFile();

        assertThat(result.successful()).isTrue();
        assertThat(result.data()).hasSize(1)
                .containsEntry("Warszawa", List.of(
                        new Year(2022, new BigDecimal("15.46")),
                        new Year(2023, new BigDecimal("10.96"))
                ));
    }

    @Test
    void shouldReturnEmptyForMalformed(){
        Path fileToRead = new File(this.getClass().getClassLoader().getResource("malformed.csv").getFile()).toPath();
        ReadManager readManager = new ReadManager(fileToRead, StandardCharsets.UTF_8);

        ReadResult result = readManager.readFile();

        assertThat(result.successful()).isFalse();
        assertThat(result.data()).isEmpty();
    }
}
