package com.ug.dsa.algorithms;

public class GreedyScheduler {
   
    public static class Job {
        public final String id;
        public final int deadline;

        public final int profit;

        public Job(String id, int deadline, int profit) {
            this.id = id;
            this.deadline = deadline;
            this.profit = profit;
        }

        @Override
        public String toString() {
            return "Job{id='" + id + "'. deadline=" + deadlline + ", profit=" + profit + "}";
        }
    }

    public static class ScheduledJob {
        public final Job job;
        public final int slot;

        public ScheduledJob(Job job, int slot) {
            this.job = job;
            this.slot = slot;
        }

        @Override
        public String toString() {
            return "Slot " + slot + " -> " + job.id + " (profit=" + job.profit +")";
        }
    }

    public static class ScheduleResult {
        public final ScheduleJob[] scheduledJobs;
        public final int scheduledCount;
        public final int totalProfit;

        public ScheduleResult(ScheduledJob[] scheduledJobs, int scheduledCount, int totalProfit) {
            this.scheduledJobs = scheduledjobs;
            this.scheduledCount = scheduledCount;
            this.totalprofit = totalProfit;
        }
    }

    private static void sortByProfitDescending(Job[] jobs) {
        for (int i = 1; i < jobs.length; i++) {
            Job current = jobs[i];
            int j = i - 1;
            while (j >= 0 && jobs[j].profit < current.profit) {
                jobs[j + 1] = jobs[j];
                j--;
            }
            jobs[j + 1] = current;
        }
    }

    public static ScheduleResult schedule(Job[] jobs) {
        if (jobs == null || jobs.length == 0) {
            return new ScheduleResult(new ScheduledJob[0], 0, 0);
        }

        // Step 1: sort a copy of the jobs by profit, highest first
        Job[] sortedJobs = new Job[jobs.length];
        for (int i = 0; i < jobs.length; i++) {
            sortedJobs[i] = jobs[i];
        }
        sortByProfitDescending(sortedJobs);

        // Step 2: find the maximum deadline to know how many slots exist
        int maxDeadline = 0;
        for (Job job : jobs) {
            if (job.deadline > maxDeadline) {
                maxDeadline = job.deadline;
            }
        }

        // Step 3: slots[1..maxDeadline] track which time units are taken
        Job[] slots = new Job[maxDeadline + 1];

        ScheduledJob[] scheduled = new ScheduledJob[jobs.length];
        int scheduledCount = 0;
        int totalProfit = 0;

        // Step 4: place each job as late as possible before its deadline
        for (Job job : sortedJobs) {
            int latestSlot = Math.min(job.deadline, maxDeadline);
            for (int slot = latestSlot; slot >= 1; slot--) {
                if (slots[slot] == null) {
                    slots[slot] = job;
                    scheduled[scheduledCount] = new ScheduledJob(job, slot);
                    scheduledCount++;
                    totalProfit += job.profit;
                    break;
                }
            }
        }

        return new ScheduleResult(scheduled, scheduledCount, totalProfit);
    }

    public static void main(String[] args) {
        Job[] jobs = new Job[] {
            new Job("J1", 2, 60),
            new Job("J2", 1, 100),
            new Job("J3", 3, 20),
            new Job("J4", 2, 40),
            new Job("J5", 1, 20)
        };

        System.out.println("Input Jobs:");
        for (Job job : jobs) {
            System.out.println("  " + job);
        }

        ScheduleResult result = schedule(jobs);

        System.out.println("\nScheduled Jobs:");
        for (int i = 0; i < result.scheduledCount; i++) {
            System.out.println("  " + result.scheduledJobs[i]);
        }

        System.out.println("\nTotal Maximum Profit: " + result.totalProfit);
    }
}