package koara.storage;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

import koara.client.Client;
import koara.client.ClientList;
import koara.exception.KoaraException;

/**
 * Loads clients from their data file and saves clients to it.
 */
public class ClientStorage {
    private static final String FIELD_SEPARATOR = "\t";
    private static final int CLIENT_FIELD_COUNT = 4;
    private static final String INVALID_DATA_ERROR = "Sorry, the saved client data is invalid.";

    private final Path dataFilePath;

    /**
     * Creates client storage that uses the specified data file.
     *
     * @param dataFilePath Path of the client data file.
     */
    public ClientStorage(Path dataFilePath) {
        assert dataFilePath != null : "Client data file path must not be null";
        this.dataFilePath = dataFilePath;
    }

    /**
     * Loads clients from the data file.
     *
     * @return Clients loaded from the data file.
     * @throws KoaraException If the file cannot be read or contains invalid data.
     */
    public ClientList load() throws KoaraException {
        ArrayList<Client> clients = new ArrayList<>();
        if (Files.notExists(dataFilePath)) {
            return new ClientList(clients);
        }

        try {
            for (String clientLine : Files.readAllLines(dataFilePath, StandardCharsets.UTF_8)) {
                clients.add(parseStoredClient(clientLine));
            }
        } catch (IOException exception) {
            throw new KoaraException("Sorry, I couldn't load your saved clients.");
        }
        return new ClientList(clients);
    }

    /**
     * Saves the complete client list to the data file.
     *
     * @param clients Clients to save.
     * @throws KoaraException If the client list cannot be written.
     */
    public void save(ClientList clients) throws KoaraException {
        assert clients != null : "Client list must not be null";
        try {
            Path dataDirectory = dataFilePath.getParent();
            if (dataDirectory != null) {
                Files.createDirectories(dataDirectory);
            }
            Files.write(dataFilePath, clients.toDataLines(), StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new KoaraException("Sorry, I couldn't save your clients.");
        }
    }

    private Client parseStoredClient(String clientLine) throws KoaraException {
        assert clientLine != null : "Stored client line must not be null";
        String[] fields = clientLine.split(FIELD_SEPARATOR, -1);
        if (fields.length != CLIENT_FIELD_COUNT) {
            throw new KoaraException(INVALID_DATA_ERROR);
        }
        for (String field : fields) {
            if (field.isBlank()) {
                throw new KoaraException(INVALID_DATA_ERROR);
            }
        }
        return new Client(fields[0], fields[1], fields[2], fields[3]);
    }
}
