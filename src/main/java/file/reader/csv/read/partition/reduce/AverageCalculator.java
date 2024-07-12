package file.reader.csv.read.partition.reduce;

import file.reader.csv.read.YearAggregate;
import file.reader.csv.read.partition.aggregate.CityAggregate;
import file.reader.csv.storage.Year;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class AverageCalculator {
    private final Map<String, CityAggregate> citiesData;

    public Map<String, List<Year>> calculateAverages() {
        return citiesData.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> calculateAverages(entry.getValue().getYears().values())));
    }

    private List<Year> calculateAverages(Collection<YearAggregate> aggregate) {
        return aggregate.stream()
                .map(year -> new Year(year.getName(), BigDecimal.valueOf((double) year.getTotalTemp() / (double) year.getNumberOfRecords() / 100.0).setScale(2, RoundingMode.HALF_UP)))
                .sorted(Comparator.comparingInt(Year::name))
                .collect(Collectors.toList());
    }
}
