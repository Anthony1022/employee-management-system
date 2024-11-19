package dev.niss.models;

public enum Job {
    PREIDENT,
    MANAGER,
    SALESMAN,
    ANALYIST,
    CLERK;

    public static Job fromString(String jobString) {
        for (Job job : values()) {
            if (jobString.equals(job.toString()))
                return job;
        }
        throw new IllegalArgumentException("Invalid Job.");
    }
}
