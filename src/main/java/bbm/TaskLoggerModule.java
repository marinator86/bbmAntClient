package bbm;

import com.google.inject.AbstractModule;
import org.apache.tools.ant.util.TaskLogger;

/**
 * Created by mario on 3/16/17.
 */
public class TaskLoggerModule extends AbstractModule{

    private final TaskLogger logger;

    public TaskLoggerModule(TaskLogger logger) {
        this.logger = logger;
    }

    @Override
    protected void configure() {
        binder().bind(TaskLogger.class).toInstance(logger);
    }
}
