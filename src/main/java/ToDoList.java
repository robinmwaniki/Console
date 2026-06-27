//to do list
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
public class ToDoList {

    // H2 file-based database -> data persists between runs in ./data/todolist.mv.db
    private static final String DB_URL = "jdbc:h2:./data/todolist";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "";

    private Connection connection;

    public ToDoList() {
        try {
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            createTables();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to connect to the database: " + e.getMessage(), e);
        }
    }

    private void createTables() throws SQLException {
        String createCategoryTable =
                "CREATE TABLE IF NOT EXISTS category (" +
                        "category_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "category_name VARCHAR(100) NOT NULL" +
                        ")";

        String createTaskTable =
                "CREATE TABLE IF NOT EXISTS task (" +
                        "task_id INT AUTO_INCREMENT PRIMARY KEY, " +
                        "description VARCHAR(255) NOT NULL, " +
                        "is_complete BOOLEAN DEFAULT FALSE, " +
                        "category_id INT, " +
                        "FOREIGN KEY (category_id) REFERENCES category(category_id)" +
                        ")";

        try (Statement stmt = connection.createStatement()) {
            stmt.execute(createCategoryTable);
            stmt.execute(createTaskTable);
        }
    }

    // ---------- Task operations ----------

    public Task addTask(String description, Integer categoryId) throws SQLException {
        String sql = "INSERT INTO task (description, is_complete, category_id) VALUES (?, FALSE, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, description);
            if (categoryId == null) {
                ps.setNull(2, Types.INTEGER);
            } else {
                ps.setInt(2, categoryId);
            }
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    return new Task(newId, description, false, categoryId);
                }
            }
        }
        throw new SQLException("Failed to retrieve generated task id.");
    }

    public Task viewTaskById(int taskId) throws SQLException {
        String sql = "SELECT * FROM task WHERE task_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapTask(rs);
                }
            }
        }
        return null; // Caller decides how to handle "not found"
    }

    public List<Task> viewAllTasks() throws SQLException {
        List<Task> tasks = new ArrayList<>();
        String sql = "SELECT * FROM task ORDER BY task_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                tasks.add(mapTask(rs));
            }
        }
        return tasks;
    }


    public boolean deleteTask(int taskId) throws SQLException {
        String sql = "DELETE FROM task WHERE task_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;
        }
    }

    private Task mapTask(ResultSet rs) throws SQLException {
        int id = rs.getInt("task_id");
        String description = rs.getString("description");
        boolean complete = rs.getBoolean("is_complete");
        int categoryIdValue = rs.getInt("category_id");
        Integer categoryId = rs.wasNull() ? null : categoryIdValue;
        return new Task(id, description, complete, categoryId);
    }


    public Category addCategory(String categoryName) throws SQLException {
        String sql = "INSERT INTO category (category_name) VALUES (?)";
        try (PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, categoryName);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) {
                    int newId = keys.getInt(1);
                    return new Category(newId, categoryName);
                }
            }
        }
        throw new SQLException("Failed to retrieve generated category id.");
    }

    public List<Category> viewAllCategories() throws SQLException {
        List<Category> categories = new ArrayList<>();
        String sql = "SELECT * FROM category ORDER BY category_id";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categories.add(new Category(rs.getInt("category_id"), rs.getString("category_name")));
            }
        }
        return categories;
    }


    public boolean categoryExists(int categoryId) throws SQLException {
        String sql = "SELECT 1 FROM category WHERE category_id = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, categoryId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}
