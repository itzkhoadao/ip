package koara.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import koara.client.Client;
import koara.exception.KoaraException;
import koara.parser.Parser.ClientEdit;
import koara.task.Deadline;
import koara.task.Event;
import koara.task.Task;
import koara.task.Todo;

public class ParserTest {

    @Test
    public void parseTask_validCommands_returnsMatchingTaskTypes() throws KoaraException {
        Task todo = Parser.parseTask("todo read book");
        Task deadline = Parser.parseTask("deadline return book /by 2019-10-15");
        Task event = Parser.parseTask("event project meeting /from 2019-12-02 /to 2019-12-03");

        assertInstanceOf(Todo.class, todo);
        assertEquals("[T][ ] read book", todo.toString());
        assertInstanceOf(Deadline.class, deadline);
        assertEquals("[D][ ] return book (by: Oct 15 2019)", deadline.toString());
        assertInstanceOf(Event.class, event);
        assertEquals("[E][ ] project meeting (from: Dec 02 2019 to: Dec 03 2019)", event.toString());
    }

    @Test
    public void parseTask_invalidCommands_throwsKoaraException() {
        assertThrows(KoaraException.class, () -> Parser.parseTask("todo"));
        assertThrows(KoaraException.class, () -> Parser.parseTask("deadline return book /by tomorrow"));
        assertThrows(KoaraException.class, () -> Parser.parseTask("event meeting /from 2019-12-02"));
        assertThrows(KoaraException.class, () -> Parser.parseTask("unknown command"));
    }

    @Test
    public void parseTaskIndex_validNumbers_returnsZeroBasedIndex() throws KoaraException {
        assertEquals(0, Parser.parseTaskIndex("mark 1", "mark", 3));
        assertEquals(2, Parser.parseTaskIndex("delete 3", "delete", 3));
    }

    @Test
    public void parseTaskIndex_invalidNumbers_throwsKoaraException() {
        assertThrows(KoaraException.class, () -> Parser.parseTaskIndex("mark", "mark", 3));
        assertThrows(KoaraException.class, () -> Parser.parseTaskIndex("mark first", "mark", 3));
        assertThrows(KoaraException.class, () -> Parser.parseTaskIndex("mark 0", "mark", 3));
        assertThrows(KoaraException.class, () -> Parser.parseTaskIndex("mark 4", "mark", 3));
    }

    @Test
    public void parseFindKeyword_validCommand_returnsTrimmedKeyword() throws KoaraException {
        assertEquals("read book", Parser.parseFindKeyword("find   read book  "));
    }

    @Test
    public void parseFindKeyword_emptyKeyword_throwsKoaraException() {
        assertThrows(KoaraException.class, () -> Parser.parseFindKeyword("find"));
        assertThrows(KoaraException.class, () -> Parser.parseFindKeyword("find   "));
    }

    @Test
    public void parseClient_validCommands_returnsClientDetailsAndEditIndex() throws KoaraException {
        Client client = Parser.parseClient(
                "client add Alex Tan /phone 91234567 /goal Run 5 km /notes Knee injury");
        ClientEdit edit = Parser.parseClientEdit(
                "client edit 2 /name Alex Tan /phone 91230000 /goal Run 10 km /notes Recovered", 2);

        assertEquals("Alex Tan | Phone: 91234567 | Goal: Run 5 km | Notes: Knee injury", client.toString());
        assertEquals(1, edit.clientIndex());
        assertEquals("Alex Tan | Phone: 91230000 | Goal: Run 10 km | Notes: Recovered",
                edit.client().toString());
        assertEquals("knee", Parser.parseClientKeyword("client find knee"));
    }

    @Test
    public void parseClient_invalidCommands_throwsKoaraException() {
        assertThrows(KoaraException.class, () -> Parser.parseClient("client add Alex Tan"));
        assertThrows(KoaraException.class, () -> Parser.parseClient(
                "client add Alex /phone 91234567 /goal Run /notes   "));
        assertThrows(KoaraException.class, () -> Parser.parseClientEdit(
                "client edit 3 /name Alex /phone 91234567 /goal Run /notes Healthy", 2));
        assertThrows(KoaraException.class, () -> Parser.parseClientKeyword("client find"));
        assertThrows(KoaraException.class, () -> Parser.parseClientIndex("client delete first", "client delete", 1));
    }
}
