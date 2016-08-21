package my.infinity.concurrent;

import org.springframework.core.task.TaskExecutor;

/**
 * Created by alexey on 15.06.16.
 */
public class TaskExecutorManager {
    private TaskExecutor taskExecutor;

    public TaskExecutorManager(TaskExecutor taskExecutor) {
        this.taskExecutor = taskExecutor;
    }

    public void execute(Runnable obj) {
        taskExecutor.execute(obj);
    }
}
