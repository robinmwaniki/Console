

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class ToDoListApp {

    private static final Scanner scanner = new Scanner(System.in);
    private static ToDoList toDoList;

    public static void main(String[] args) {
        try {
            toDoList = new ToDoList();
        } catch (RuntimeException e) {
            System.out.println("Could not start the application: " + e.getMessage());
            return;
        }

        boolean running = true;
        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    addTask();
                    break;
                case "2":
                    viewTaskById();
                    break;
                case "3":
                    viewAllTasks();
                    break;
                case "4":
                    deleteTask();
                    break;
                case "5":
                    addCategory();
                    break;
                case "6":
                    viewAllCategories();
                    break;
                case "7":
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 7.");
            }
        }

        toDoList.close();
        scanner.close();
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("Simple To Do List Application ");
        System.out.println("1. Add a Task");
        System.out.println("2. View Task by Id");
        System.out.println("3. View All Tasks");
        System.out.println("4. Delete a Task");
        System.out.println("5. Add a Category");
        System.out.println("6. View All Categories");
        System.out.println("7. Exit the app");
        System.out.print("Choose option: ");
    }

    private static void addTask() {
        System.out.print("Enter task description: ");
        String description = scanner.nextLine().trim();

        if (description.isEmpty()) {
            System.out.println("Description cannot be empty");
            return;
        }

        Integer categoryId = null;
        System.out.print("Enter category id: ");
        String categoryInput = scanner.nextLine().trim();

        if (!categoryInput.isEmpty()) {
            try {
                int candidateId = Integer.parseInt(categoryInput);
                if (toDoList.categoryExists(candidateId)) {
                    categoryId = candidateId;
                } else {
                    System.out.println("No category with id " + candidateId + " exists. Adding task without a category.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid category id. Adding task without a category.");
            } catch (SQLException e) {
                System.out.println("Database error while checking category: " + e.getMessage());
            }
        }

        try {
            Task created = toDoList.addTask(description, categoryId);
            System.out.println("Task added: " + created);
        } catch (SQLException e) {
            System.out.println("Failed to add task: " + e.getMessage());
        }
    }

    private static void viewTaskById() {
        System.out.print("Enter task id: ");
        String input = scanner.nextLine().trim();

        try {
            int taskId = Integer.parseInt(input);
            Task task = toDoList.viewTaskById(taskId);
            if (task == null) {
                System.out.println("No task found with id " + taskId + ".");
            } else {
                System.out.println(task);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid id. Please enter a whole number.");
        } catch (SQLException e) {
            System.out.println("Database error while fetching task: " + e.getMessage());
        }
    }

    private static void viewAllTasks() {
        try {
            List<Task> tasks = toDoList.viewAllTasks();
            if (tasks.isEmpty()) {
                System.out.println("No tasks yet. Add one from the menu!");
            } else {
                System.out.println("---- All Tasks ----");
                for (Task task : tasks) {
                    System.out.println(task);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while fetching tasks: " + e.getMessage());
        }
    }

    private static void deleteTask() {
        System.out.print("Enter id of task to delete: ");
        String input = scanner.nextLine().trim();

        try {
            int taskId = Integer.parseInt(input);
            boolean deleted = toDoList.deleteTask(taskId);
            if (deleted) {
                System.out.println("Task " + taskId + " deleted.");
            } else {
                System.out.println("No task found with id " + taskId + ". Nothing was deleted.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid id. Please enter a whole number.");
        } catch (SQLException e) {
            System.out.println("Database error while deleting task: " + e.getMessage());
        }
    }

    private static void addCategory() {
        System.out.print("Enter category name: ");
        String name = scanner.nextLine().trim();

        if (name.isEmpty()) {
            System.out.println("Category name cannot be empty. Category not added.");
            return;
        }

        try {
            Category created = toDoList.addCategory(name);
            System.out.println("Category added: " + created);
        } catch (SQLException e) {
            System.out.println("Failed to add category: " + e.getMessage());
        }
    }

    private static void viewAllCategories() {
        try {
            List<Category> categories = toDoList.viewAllCategories();
            if (categories.isEmpty()) {
                System.out.println("No categories yet. Add one from the menu!");
            } else {
                System.out.println("---- All Categories ----");
                for (Category category : categories) {
                    System.out.println(category);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error while fetching categories: " + e.getMessage());
        }
    }
}
