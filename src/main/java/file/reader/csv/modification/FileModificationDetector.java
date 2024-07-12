package file.reader.csv.modification;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;


@Slf4j
public class FileModificationDetector implements AutoCloseable {
    private final Path dataFile;
    private final FileChange fileChanegConsumer;
    private final WatchService watchService;
    private final ScheduledExecutorService executorService;

    public FileModificationDetector(Path dataFileDirectory, Path dataFile, FileChange fileChangeConsumer) throws IOException {
        this.dataFile = dataFile;
        this.fileChanegConsumer = fileChangeConsumer;
        this.executorService = Executors.newScheduledThreadPool(1);
        this.watchService = FileSystems.getDefault().newWatchService();
        dataFileDirectory.register(watchService, ENTRY_MODIFY, ENTRY_CREATE);

        scheduleToEvents();
    }

    protected void scheduleToEvents() {
        executorService.scheduleAtFixedRate(this::checkEvents, 5, 10, TimeUnit.SECONDS);
    }

    protected void checkEvents() {
        try {
            Optional.ofNullable(watchService.poll())
                    .map(this::findEventAndResetWatch)
                    .ifPresent(event -> this.fileChanegConsumer.detected());
        } catch (Exception e) {
            log.error("Error reading events from datafile directory", e);
        }

    }

    private Path findEventAndResetWatch(WatchKey key) {
        var event = findMatchingEvent(key);
        key.reset();
        return event;
    }

    private Path findMatchingEvent(WatchKey key) {
        return key.pollEvents()
                .stream()
                .map(watchEvent -> (Path) watchEvent.context())
                .filter(dataFile::endsWith)
                .findAny()
                .orElse(null);
    }

    @Override
    public void close() throws Exception {
        watchService.close();
        executorService.close();
    }
}
