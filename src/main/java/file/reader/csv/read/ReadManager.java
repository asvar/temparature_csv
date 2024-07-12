package file.reader.csv.read;

import file.reader.csv.read.partition.RowSplitter;
import file.reader.csv.read.partition.reduce.CityReducer;
import file.reader.csv.storage.Year;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Stream;

@Slf4j
public class ReadManager {
    private final Path fileToRead;
    private final Charset charset;
    private ExecutorService readExecutor;
    private ExecutorService computeExecutor;

    public ReadManager(Path fileToRead, Charset charset) {
        this.fileToRead = fileToRead;
        this.charset = charset;
    }

    public void stopCurrent() {
        shutdownExecutors();
    }

    public ReadResult readFile() {
        log.info("Started processing data file");
        createExecutors();

        CityReducer reducer = new CityReducer();
        RowSplitter splitter = new RowSplitter(computeExecutor, reducer);

        var submit = startReadingFile(splitter);

        boolean successfulRead = waitForReadToComplete(submit);
        if (!successfulRead) {
            return ReadResult.failed();
        }

        boolean successfulCompute = waitForComputationToComplete(splitter);
        if (!successfulCompute) {
            return ReadResult.failed();
        }

        Map<String, List<Year>> result = reducer.reduce();
        log.info("Finished processing data file");

        shutdownExecutors();

        return ReadResult.successful(result);
    }

    private void createExecutors() {
        readExecutor = Executors.newSingleThreadExecutor();
        computeExecutor = Executors.newVirtualThreadPerTaskExecutor();
    }

    private Future<?> startReadingFile(RowSplitter splitter) {
        return readExecutor.submit(() -> {
            try (Stream<String> stream = Files.lines(fileToRead, charset)) {
                stream.forEach(splitter::consume);
            } catch (Exception e) {
                this.stopCurrent();
            }
        });
    }

    private boolean waitForReadToComplete(Future readTask) {
        try {
            readTask.get();
            return true;
        } catch (InterruptedException | ExecutionException e) {
            return false;
        }
    }

    private boolean waitForComputationToComplete(RowSplitter splitter) {
        splitter.flush();
        try {
            splitter.waitToComplete();
            return true;
        } catch (ExecutionException | InterruptedException e) {
            this.stopCurrent();
            return false;
        }
    }


    private void shutdownExecutors() {
        readExecutor.shutdownNow();
        computeExecutor.shutdownNow();
    }
}
