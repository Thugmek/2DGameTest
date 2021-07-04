package jobs;

import java.util.List;

public abstract class JobTask {
    private List<JobTask> dependencies;
    private boolean done;
}
