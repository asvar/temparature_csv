package file.reader.csv.read.partition.aggregate;

import file.reader.csv.read.YearAggregate;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class CityAggregate {
    private final String name;
    private final Map<Integer, YearAggregate> years = new HashMap<>();

    public CityAggregate(String name, YearTemperature yearTemperature) {
        this.name = name;
        years.put(yearTemperature.year(), new YearAggregate(yearTemperature));
    }

    public void collect(YearTemperature yearTemperature) {
        if (years.containsKey(yearTemperature.year())) {
            years.get(yearTemperature.year()).add(yearTemperature.temp());
        } else {
            years.put(yearTemperature.year(), new YearAggregate(yearTemperature));
        }
    }

    public void collect(CityAggregate yearAggregate) {
        yearAggregate.getYears().forEach((key, value) -> {
            if (years.containsKey(key)) {
                years.get(key).add(value.getTotalTemp(), value.getNumberOfRecords());
            } else {
                years.put(key, new YearAggregate(key, value.getTotalTemp(), value.getNumberOfRecords()));
            }
        });
    }
}
