# Koara UI Test Plan

## Test environment

- Entry point: `src/main/java/Koara.java`
- Runtime and compiler: Java 25
- Isolation: each test case starts a new Koara process
- Comparison: exact standard output after normalizing line endings; spaces, blank lines, punctuation, letter case, and the final newline remain significant
- Failure policy: stop immediately after the first failed case

## TC-01: Add one task

### Aim

Verify that Koara stores an entered task, confirms the addition, and exits with its farewell when `bye` is entered.

### Inputs

```text
read book
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
     added: read book
    ____________________________________________________________
    ____________________________________________________________
     Bye. Koara hopes to see you again soon!
    ____________________________________________________________
```

## TC-02: List multiple tasks

### Aim

Verify that Koara retains multiple tasks in entry order and displays them as a numbered list.

### Inputs

```text
read book
return book
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
     added: read book
    ____________________________________________________________
    ____________________________________________________________
     added: return book
    ____________________________________________________________
    ____________________________________________________________
     Here are the tasks in your list:
     1.[ ] read book
     2.[ ] return book
    ____________________________________________________________
    ____________________________________________________________
     Bye. Koara hopes to see you again soon!
    ____________________________________________________________
```
