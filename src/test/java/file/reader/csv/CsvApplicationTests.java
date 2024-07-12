package file.reader.csv;

import file.reader.csv.storage.TemperatureDataManager;
import file.reader.csv.storage.Year;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CsvApplicationTests {

    @Autowired
    TemperatureDataManager dataManager;

    @Test
    void shouldGetCityFromFile() {
        var result = dataManager.getByCity("Warszawa");

        assertThat(result).isPresent();
        assertThat(result.get())
                .hasSize(6)
                .containsExactlyElementsOf(List.of(
                        new Year(2018, new BigDecimal("13.52")),
                        new Year(2019, new BigDecimal("13.81")),
                        new Year(2020, new BigDecimal("16.12")),
                        new Year(2021, new BigDecimal("15.61")),
                        new Year(2022, new BigDecimal("14.68")),
                        new Year(2023, new BigDecimal("15.46"))
                ));
    }

    @Test
    void shouldGetEmptyWhenCityNotInFile(){
        var result = dataManager.getByCity("Toruń");

        assertThat(result).isEmpty();
    }

}
