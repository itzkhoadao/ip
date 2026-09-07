package koara.client;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * Tests client display, storage, and searching behavior.
 */
public class ClientTest {

    @Test
    public void clientRepresentations_validDetails_returnExpectedFormats() {
        Client client = new Client("Alex Tan", "91234567", "Run 5 km", "Knee injury");

        assertTrue(client.containsKeyword("alex"));
        assertTrue(client.containsKeyword("KNEE"));
        assertFalse(client.containsKeyword("swimming"));
        assertTrue(client.toString().contains("Phone: 91234567"));
        assertTrue(client.toString().contains("Goal: Run 5 km"));
        assertTrue(client.toString().contains("Notes: Knee injury"));
        assertTrue(client.toDataString().equals("Alex Tan\t91234567\tRun 5 km\tKnee injury"));
    }
}
