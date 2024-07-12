package file.reader.csv.read.partition.reduce;

import file.reader.csv.read.partition.aggregate.CityAggregate;
import file.reader.csv.read.partition.aggregate.PartitionAggregator;
import file.reader.csv.storage.Year;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CityReducer {
    private final List<PartitionAggregator> aggregators = new ArrayList<>();

    public void collect(PartitionAggregator aggregator) {
        aggregators.add(aggregator);
    }

    public Map<String, List<Year>> reduce() {
        Map<String, CityAggregate> citiesData = new HashMap<>();
        aggregators
                .stream()
                .map(PartitionAggregator::getCities)
                .flatMap(results -> results.entrySet().stream())
                .forEach(entry -> {
                    if (citiesData.containsKey(entry.getKey())) {
                        citiesData.get(entry.getKey()).collect(entry.getValue());
                    } else {
                        citiesData.put(entry.getKey(), entry.getValue());
                    }
                });

        AverageCalculator averageCalculator = new AverageCalculator(citiesData);

        return averageCalculator.calculateAverages();
    }

}
