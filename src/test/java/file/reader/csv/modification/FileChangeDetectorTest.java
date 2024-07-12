package file.reader.csv.modification;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class FileChangeDetectorTest {

    @TempDir
    private Path tempDirPath;

    @Test
    void shouldSignalFileChange() throws IOException {
        Path tempFilePath = Files.createTempFile(tempDirPath, "tempFile", "_temp");
        Executed executed = new Executed();

        assertThat(tempFilePath).exists();

        try (FileModificationDetector changeDetector = new FileModificationDetector(tempDirPath, tempFilePath, () -> executed.execute())) {
            Files.write(tempFilePath, "test".getBytes());

            changeDetector.checkEvents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(executed.wasExecuted()).isTrue();
    }

    @Test
    void shouldNotSignalFileChangeWhenDifferentFileChanges() throws IOException {
        Path tempFilePath = Files.createTempFile(tempDirPath, "tempFile", "_temp");
        Path differentFilePath = Files.createTempFile(tempDirPath, "notThisFile", "_temp");
        Executed executed = new Executed();

        assertThat(tempFilePath).exists();

        try (FileModificationDetector changeDetector = new FileModificationDetector(tempDirPath, tempFilePath, () -> executed.execute())) {
            Files.write(differentFilePath, "test".getBytes());

            changeDetector.checkEvents();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        assertThat(executed.wasExecuted()).isFalse();
    }

}

class Executed {
    private boolean executed = false;

    public void execute() {
        this.executed = true;
    }

    public boolean wasExecuted() {
        return executed;
    }
}