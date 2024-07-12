package file.reader.csv.read.partition;

import file.reader.csv.read.partition.aggregate.AggregationTask;
import file.reader.csv.read.partition.aggregate.PartitionAggregator;
import file.reader.csv.read.partition.map.TemperatureRowMapper;
import file.reader.csv.read.partition.reduce.CityReducer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class RowSplitter {
    private final ExecutorService computeExecutor;
    private final CityReducer reducer;
    private final List<Future<PartitionAggregator>> futures;
    private ArrayList<String> currentLines = new ArrayList<>(1000);

    public RowSplitter(ExecutorService computeExecutor, CityReducer reducer) {
        this.computeExecutor = computeExecutor;
        this.reducer = reducer;
        this.futures = new ArrayList<>();
    }

    public void consume(String line) {
        currentLines.add(line);

        if (currentLines.size() == 1000) {
            submitCurrentPage();

            currentLines = new ArrayList<>(1000);
        }
    }

    public void flush() {
        submitCurrentPage();
    }

    private void submitCurrentPage() {
        var feature = computeExecutor.submit(new AggregationTask(new TemperatureRowMapper(), currentLines));
        futures.add(feature);
    }

    public void waitToComplete() throws ExecutionException, InterruptedException {
        for (Future<PartitionAggregator> future : futures) {
            reducer.collect(future.get());
        }
    }
}
