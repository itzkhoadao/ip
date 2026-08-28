package koara.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import koara.exception.KoaraException;
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
}
