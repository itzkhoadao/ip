package koara.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import koara.client.Client;
import koara.client.ClientList;
import koara.exception.KoaraException;

/**
 * Tests loading and saving client records.
 */
public class ClientStorageTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void saveAndLoad_clients_preservesAllDetails() throws KoaraException {
        Path filePath = tempDirectory.resolve("data").resolve("clients.txt");
        ClientStorage storage = new ClientStorage(filePath);
        ClientList clients = new ClientList();
        clients.add(new Client("Alex Tan", "91234567", "Run 5 km", "Knee injury"));
        clients.add(new Client("Beth Lee", "92345678", "Gain strength", "Vegetarian"));

        storage.save(clients);
        ClientList loadedClients = storage.load();

        assertTrue(Files.exists(filePath));
        assertEquals(List.of(
                "Alex Tan\t91234567\tRun 5 km\tKnee injury",
                "Beth Lee\t92345678\tGain strength\tVegetarian"
        ), loadedClients.toDataLines());
    }

    @Test
    public void load_invalidClientData_throwsKoaraException() throws IOException {
        Path filePath = tempDirectory.resolve("clients.txt");
        Files.writeString(filePath, "Alex Tan\t91234567\t\tKnee injury");
        ClientStorage storage = new ClientStorage(filePath);

        assertThrows(KoaraException.class, storage::load);
    }
}
