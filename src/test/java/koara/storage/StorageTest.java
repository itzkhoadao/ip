package koara.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import koara.exception.KoaraException;
import koara.task.Deadline;
import koara.task.Event;
import koara.task.TaskList;
import koara.task.Todo;

public class StorageTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void load_missingFile_returnsEmptyTaskList() throws KoaraException {
        Storage storage = new Storage(tempDirectory.resolve("missing.txt"));

        TaskList tasks = storage.load();

        assertEquals(0, tasks.size());
    }

    @Test
    public void saveAndLoad_mixedTasks_preservesData() throws KoaraException {
        Path filePath = tempDirectory.resolve("data").resolve("koara.txt");
        Storage storage = new Storage(filePath);
        TaskList tasks = new TaskList();
        tasks.add(new Todo("read book"));
        tasks.add(new Deadline("return book", LocalDate.of(2019, 10, 15)));
        tasks.add(new Event("project meeting", LocalDate.of(2019, 12, 2), LocalDate.of(2019, 12, 3)));
        tasks.mark(1);

        storage.save(tasks);
        TaskList loadedTasks = storage.load();

        assertTrue(Files.exists(filePath));
        assertEquals(List.of(
                "T | 0 | read book",
                "D | 1 | return book | 2019-10-15",
                "E | 0 | project meeting | 2019-12-02 | 2019-12-03"
        ), loadedTasks.toDataLines());
    }

    @Test
    public void load_invalidData_throwsKoaraException() throws IOException {
        Path filePath = tempDirectory.resolve("koara.txt");
        Files.writeString(filePath, "invalid task data");
        Storage storage = new Storage(filePath);

        assertThrows(KoaraException.class, storage::load);
    }
}
