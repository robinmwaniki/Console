


public class Task {
    private int taskId;
    private String description;
    private boolean isComplete;
    private Integer categoryId;

    public Task() {
    }

    public Task(int taskId, String description, boolean isComplete, Integer categoryId) {
        this.taskId = taskId;
        this.description = description;
        this.isComplete = isComplete;
        this.categoryId = categoryId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        this.isComplete = complete;
    }

    public Integer getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    @Override
    public String toString() {
        return "Task #" + taskId
                + " | " + description
                + " | " + (isComplete ? "Done" : "Pending")
                + " | Category: " + (categoryId == null ? "None" : categoryId);
    }
}
