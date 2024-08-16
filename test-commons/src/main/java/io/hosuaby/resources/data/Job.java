package io.hosuaby.resources.data;

import java.util.List;

public class Job {
    private int jobId;
    private List<Step> steps;

    public int getJobId() {
        return jobId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public List<Step> getSteps() {
        return steps;
    }

    public void setSteps(List<Step> steps) {
        this.steps = steps;
    }

    public static class Step {
        public int stepId;

        public int getStepId() {
            return stepId;
        }

        public void setStepId(int stepId) {
            this.stepId = stepId;
        }
    }
}
