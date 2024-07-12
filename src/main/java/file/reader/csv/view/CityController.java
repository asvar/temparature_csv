package file.reader.csv.view;

import file.reader.csv.storage.TemperatureDataManager;
import file.reader.csv.storage.Year;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class CityController {

    private final TemperatureDataManager dataManager;

    @GetMapping("/{city}")
    public ResponseEntity<List<Year>> getAverageTemps(@PathVariable("city") String city) {
        Optional<List<Year>> years = dataManager.getByCity(city);

        if (years.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(years.get(), HttpStatus.OK);
    }

}
