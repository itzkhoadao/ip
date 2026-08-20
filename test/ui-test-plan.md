# Koara UI Test Plan

## Test environment

- Entry point: `src/main/java/Koara.java`
- Runtime and compiler: Java 25
- Isolation: each test case starts a new Koara process
- Comparison: exact standard output after normalizing line endings; spaces, blank lines, punctuation, letter case, and the final newline remain significant
- Failure policy: stop immediately after the first failed case

## TC-01: Add a todo

### Aim

Verify the exact confirmation, task icon, indentation, task count, and farewell when adding a todo.

### Inputs

```text
todo borrow book
bye
```

### Expected output

```text
    ____________________________________________________________
      _  __  ___      _      ____       _
     | |/ / / _ \    / \    |  _ \     / \
     | ' / | | | |  / _ \   | |_) |   / _ \
     | . \ | |_| | / ___ \  |  _ <   / ___ \
     |_|\_\ \___/ /_/   \_\ |_| \_\ /_/   \_\
     Hello! I'm Koara.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] borrow book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Bye. Koara hopes to see you again soon!
    ____________________________________________________________
```

## TC-02: Mark and unmark a typed task

### Aim

Verify that deadline text remains unchanged and the type icon is retained when a task is marked and unmarked.

### Inputs

```text
deadline do homework /by no idea :-p
mark 1
unmark 1
bye
```

### Expected output

```text
    ____________________________________________________________
      _  __  ___      _      ____       _
     | |/ / / _ \    / \    |  _ \     / \
     | ' / | | | |  / _ \   | |_) |   / _ \
     | . \ | |_| | / ___ \  |  _ <   / ___ \
     |_|\_\ \___/ /_/   \_\ |_| \_\ /_/   \_\
     Hello! I'm Koara.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] do homework (by: no idea :-p)
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [D][X] do homework (by: no idea :-p)
    ____________________________________________________________
    ____________________________________________________________
     OK, I've marked this task as not done yet:
       [D][ ] do homework (by: no idea :-p)
    ____________________________________________________________
    ____________________________________________________________
     Bye. Koara hopes to see you again soon!
    ____________________________________________________________
```

## TC-03: Add and list all task types

### Aim

Verify todo, deadline, and event parsing; string-based date and time display; task counts; and numbered list formatting.

### Inputs

```text
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

### Expected output

```text
    ____________________________________________________________
      _  __  ___      _      ____       _
     | |/ / / _ \    / \    |  _ \     / \
     | ' / | | | |  / _ \   | |_) |   / _ \
     | . \ | |_| | / ___ \  |  _ <   / ___ \
     |_|\_\ \___/ /_/   \_\ |_| \_\ /_/   \_\
     Hello! I'm Koara.
     What can I do for you?
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] read book
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] return book (by: Sunday)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] project meeting (from: Mon 2pm to: 4pm)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] read book
     2.[D][ ] return book (by: Sunday)
     3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
    ____________________________________________________________
    ____________________________________________________________
     Bye. Koara hopes to see you again soon!
    ____________________________________________________________
```
