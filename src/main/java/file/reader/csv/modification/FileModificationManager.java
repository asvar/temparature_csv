package file.reader.csv.modification;

import file.reader.csv.read.ReadManager;
import file.reader.csv.storage.TemperatureDataManager;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;

@Slf4j
public class FileModificationManager {
    private final FileModificationDetector modificationDetector;

    public FileModificationManager(File dataFile, final ReadManager readManager, final TemperatureDataManager dataManager) throws IOException {
        File dataFileDirectory = dataFile.getParentFile();
        modificationDetector = new FileModificationDetector(
                dataFileDirectory.toPath(),
                dataFile.toPath(),
                () -> {
                    log.info("Data File was changed");
                    readManager.stopCurrent();
                    dataManager.newReadData(readManager.readFile());
                }
        );

    }

    @PreDestroy
    public void terminateDetector() throws Exception {
        modificationDetector.close();
    }
}
