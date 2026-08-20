# Koara

Koara is a personal assistant chatbot built incrementally as part of the CS2103T individual project. At Level 3, Koara can store tasks for the current session, display their completion status, and mark or unmark them. Given below are instructions on how to set it up.

## Setting up in Intellij

Prerequisites: JDK 25, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 25** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Koara.java` file, right-click it, and choose `Run Koara.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, Koara should start an interactive session like the one below.
   Enter any text to add it as a task, `list` to view all tasks, `mark NUMBER` to mark a task, `unmark NUMBER` to reverse its status, or `bye` to exit. Tasks are kept in memory only and are not saved after the program ends. Below is an example of how we can use Koara.
   ```
       ____________________________________________________________
         _  __  ___      _      ____       _
        | |/ / / _ \    / \    |  _ \     / \
        | ' / | | | |  / _ \   | |_) |   / _ \
        | . \ | |_| | / ___ \  |  _ <   / ___ \
        |_|\_\ \___/ /_/   \_\ |_| \_\ /_/   \_\
        Hello! I'm Koara.
        What can I do for you?
       ____________________________________________________________
   read book
       ____________________________________________________________
        added: read book
       ____________________________________________________________
   return book
       ____________________________________________________________
        added: return book
       ____________________________________________________________
   list
       ____________________________________________________________
        Here are the tasks in your list:
        1.[ ] read book
        2.[ ] return book
       ____________________________________________________________
   mark 2
       ____________________________________________________________
        Nice! I've marked this task as done:
          [X] return book
       ____________________________________________________________
   unmark 2
       ____________________________________________________________
        OK, I've marked this task as not done yet:
          [ ] return book
       ____________________________________________________________
   bye
       ____________________________________________________________
        Bye. Koara hopes to see you again soon!
       ____________________________________________________________
   ```

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.
