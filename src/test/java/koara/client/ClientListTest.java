package koara.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests operations on collections of clients.
 */
public class ClientListTest {

    @Test
    public void clientOperations_addFindUpdateDelete_updatesList() {
        Client alex = new Client("Alex Tan", "91234567", "Run 5 km", "Knee injury");
        Client beth = new Client("Beth Lee", "92345678", "Gain strength", "Vegetarian");
        Client updatedBeth = new Client("Beth Lee", "92345678", "Deadlift 80 kg", "Vegetarian");
        ClientList clients = new ClientList();

        clients.add(alex);
        clients.add(beth);
        assertEquals(2, clients.getSize());
        assertSame(alex, clients.find("knee").get(0));
        assertSame(beth, clients.find("STRENGTH").get(0));

        clients.update(1, updatedBeth);
        assertSame(updatedBeth, clients.get(1));
        assertSame(alex, clients.delete(0));
        assertEquals(List.of("Beth Lee\t92345678\tDeadlift 80 kg\tVegetarian"), clients.toDataLines());
    }

    @Test
    public void constructor_sourceListChanged_doesNotChangeClientList() {
        ArrayList<Client> sourceClients = new ArrayList<>();
        sourceClients.add(new Client("Alex Tan", "91234567", "Run 5 km", "Knee injury"));

        ClientList clients = new ClientList(sourceClients);
        sourceClients.clear();

        assertEquals(1, clients.getSize());
    }
}
