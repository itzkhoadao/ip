# Koara UI Test Plan

## Test environment

- Entry point: `src/main/java/Koara.java`
- Runtime and compiler: Java 25
- Isolation: each test case starts a new Koara process
- Comparison: exact standard output after normalizing line endings; spaces, blank lines, punctuation, letter case, and the final newline remain significant
- Failure policy: stop immediately after the first failed case
- State safety: positive and negative cases are interleaved, and invalid commands are followed by valid commands or `list` in the same session

## TC-01: Add a todo

### Aim

Verify the exact confirmation, task icon, indentation, task count, and farewell when adding a valid todo.

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

## TC-02: Reject empty and unknown commands without changing the list

### Aim

Verify the exact sample error messages and confirm that empty or unknown commands do not add tasks or stop later valid commands.

### Inputs

```text
todo

todo keep state
blah
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
     OOPS!!! The description of a todo cannot be empty.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] keep state
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! I'm sorry, but I don't know what that means :-(
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] keep state
    ____________________________________________________________
    ____________________________________________________________
     Bye. Koara hopes to see you again soon!
    ____________________________________________________________
```

## TC-03: Mark and unmark a typed task

### Aim

Verify that deadline text remains unchanged and the type icon is retained when a valid task number is marked and unmarked.

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

## TC-04: Reject malformed typed tasks without changing the list

### Aim

Verify specific deadline and event errors while interleaving valid additions, then confirm only valid tasks appear in the correct order.

### Inputs

```text
todo first valid
deadline /by Sunday
deadline missing timing
deadline empty time /by
deadline second valid /by Friday
event /from 2pm /to 3pm
event missing end /from 2pm
event empty start /from  /to 3pm
event empty end /from 2pm /to
event third valid /from Sat /to Sun
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
       [T][ ] first valid
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! The description of a deadline cannot be empty.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! A deadline needs a /by date or time.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! A deadline needs a /by date or time.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [D][ ] second valid (by: Friday)
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! The description of an event cannot be empty.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! An event needs /from and /to dates or times.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! An event needs /from and /to dates or times.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! An event needs /from and /to dates or times.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [E][ ] third valid (from: Sat to: Sun)
     Now you have 3 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first valid
     2.[D][ ] second valid (by: Friday)
     3.[E][ ] third valid (from: Sat to: Sun)
    ____________________________________________________________
    ____________________________________________________________
     Bye. Koara hopes to see you again soon!
    ____________________________________________________________
```

## TC-05: Add and list all task types

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

## TC-06: Reject invalid task numbers without changing statuses

### Aim

Verify missing, non-numeric, zero, negative, and out-of-range task numbers, then confirm only the valid mark command changes task status.

### Inputs

```text
todo first
todo second
mark
mark abc
mark 0
mark 3
mark 2
unmark
unmark 1.5
unmark -1
unmark 3
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
       [T][ ] first
     Now you have 1 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     Got it. I've added this task:
       [T][ ] second
     Now you have 2 tasks in the list.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! Please specify a task number to mark.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! The task number must be a whole number.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! That task number does not exist.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! That task number does not exist.
    ____________________________________________________________
    ____________________________________________________________
     Nice! I've marked this task as done:
       [T][X] second
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! Please specify a task number to unmark.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! The task number must be a whole number.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! That task number does not exist.
    ____________________________________________________________
    ____________________________________________________________
     OOPS!!! That task number does not exist.
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[T][ ] first
     2.[T][X] second
    ____________________________________________________________
    ____________________________________________________________
     Bye. Koara hopes to see you again soon!
    ____________________________________________________________
```
