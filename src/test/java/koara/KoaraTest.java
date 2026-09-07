package koara;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * Tests command execution across Koara's parser, task list, and storage.
 */
public class KoaraTest {

    @TempDir
    Path tempDirectory;

    @Test
    public void getResponse_taskWorkflow_returnsExpectedResponses() {
        Koara koara = new Koara(tempDirectory.resolve("koara.txt"));

        assertEquals("Got it. I've added this task:\n  [T][ ] read book"
                + "\nNow you have 1 tasks in the list.", koara.getResponse("todo read book"));
        assertEquals("Nice! I've marked this task as done:\n  [T][X] read book",
                koara.getResponse("mark 1"));
        assertEquals("OK, I've marked this task as not done yet:\n  [T][ ] read book",
                koara.getResponse("unmark 1"));
        assertEquals("Here are the matching tasks in your list:\n1.[T][ ] read book",
                koara.getResponse("find book"));
        assertEquals("Here are the tasks in your list:\n1.[T][ ] read book",
                koara.getResponse("list"));
        assertEquals("Noted. I've removed this task:\n  [T][ ] read book"
                + "\nNow you have 0 tasks in the list.", koara.getResponse("delete 1"));
        assertEquals("Bye. Koara hopes to see you again soon!", koara.getResponse("bye"));
    }

    @Test
    public void getResponse_clientWorkflow_managesAndPersistsClients() {
        Path dataFile = tempDirectory.resolve("koara.txt");
        Koara koara = new Koara(dataFile);

        assertEquals("Added this client:\n  Alex Tan | Phone: 91234567"
                + " | Goal: Run 5 km | Notes: Knee injury\nNow you have 1 client.",
                koara.getResponse("client add Alex Tan /phone 91234567 /goal Run 5 km /notes Knee injury"));
        assertEquals("Here are the matching clients:\n1. Alex Tan | Phone: 91234567"
                + " | Goal: Run 5 km | Notes: Knee injury", koara.getResponse("client find KNEE"));
        assertEquals("Updated this client:\n  Alex Tan | Phone: 91230000"
                + " | Goal: Run 10 km | Notes: Recovered", koara.getResponse(
                        "client edit 1 /name Alex Tan /phone 91230000 /goal Run 10 km /notes Recovered"));

        Koara reloadedKoara = new Koara(dataFile);
        assertEquals("Here are your clients:\n1. Alex Tan | Phone: 91230000"
                + " | Goal: Run 10 km | Notes: Recovered", reloadedKoara.getResponse("client list"));
        assertEquals("Removed this client:\n  Alex Tan | Phone: 91230000"
                + " | Goal: Run 10 km | Notes: Recovered\nNow you have 0 clients.",
                reloadedKoara.getResponse("client delete 1"));
    }
}
