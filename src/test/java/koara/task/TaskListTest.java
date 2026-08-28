package koara.task;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TaskListTest {

    @Test
    public void taskOperations_multipleTasks_updatesListAndStatus() {
        TaskList tasks = new TaskList();
        Todo todo = new Todo("read book");
        Deadline deadline = new Deadline("return book", LocalDate.of(2019, 10, 15));

        tasks.add(todo);
        tasks.add(deadline);
        assertEquals(2, tasks.size());
        assertSame(deadline, tasks.get(1));

        tasks.mark(1);
        assertEquals("[D][X] return book (by: Oct 15 2019)", tasks.get(1).toString());
        tasks.unmark(1);
        assertEquals("[D][ ] return book (by: Oct 15 2019)", tasks.get(1).toString());

        assertSame(todo, tasks.delete(0));
        assertEquals(1, tasks.size());
        assertSame(deadline, tasks.get(0));
    }

    @Test
    public void constructor_sourceListChanged_doesNotChangeTaskList() {
        ArrayList<Task> source = new ArrayList<>();
        source.add(new Todo("read book"));

        TaskList tasks = new TaskList(source);
        source.clear();

        assertEquals(1, tasks.size());
        assertEquals("[T][ ] read book", tasks.get(0).toString());
    }

    @Test
    public void toDataLines_mixedTasks_returnsStorageFormat() {
        ArrayList<Task> source = new ArrayList<>();
        source.add(new Todo("read book"));
        source.add(new Deadline("return book", LocalDate.of(2019, 10, 15)));
        source.add(new Event("project meeting", LocalDate.of(2019, 12, 2), LocalDate.of(2019, 12, 3)));
        TaskList tasks = new TaskList(source);
        tasks.mark(1);

        assertEquals(List.of(
                "T | 0 | read book",
                "D | 1 | return book | 2019-10-15",
                "E | 0 | project meeting | 2019-12-02 | 2019-12-03"
        ), tasks.toDataLines());
    }

    @Test
    public void find_matchingDescriptions_returnsMatchingTasksInOrder() {
        Todo todo = new Todo("read book");
        Deadline deadline = new Deadline("return book", LocalDate.of(2019, 10, 15));
        Event event = new Event("project meeting", LocalDate.of(2019, 12, 2), LocalDate.of(2019, 12, 3));
        TaskList tasks = new TaskList(new ArrayList<>(List.of(todo, deadline, event)));

        TaskList matchingTasks = tasks.find("book");

        assertEquals(2, matchingTasks.size());
        assertSame(todo, matchingTasks.get(0));
        assertSame(deadline, matchingTasks.get(1));
    }

    @Test
    public void find_keywordOutsideDescription_returnsEmptyTaskList() {
        TaskList tasks = new TaskList(new ArrayList<>(List.of(
                new Deadline("return assignment", LocalDate.of(2019, 10, 15))
        )));

        TaskList matchingTasks = tasks.find("2019");

        assertEquals(0, matchingTasks.size());
    }
}
