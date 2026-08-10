package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;

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
            return "Job{id='" + id + "', deadline=" + deadline + ", profit=" + profit + "}";
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
            return "Slot " + slot + " -> " + job.id + " (profit=" + job.profit + ")";
        }
    }

    public static class ScheduleResult {
        public final DynamicArray<ScheduledJob> scheduledJobs;
        public final int totalProfit;

        public ScheduleResult(DynamicArray<ScheduledJob> scheduledJobs, int totalProfit) {
            this.scheduledJobs = scheduledJobs;
            this.totalProfit = totalProfit;
        }
    }

    private static DynamicArray<Job> sortByProfitDescending(DynamicArray<Job> jobs) {
        DynamicArray<Job> sorted = new DynamicArray<>();
        for (int i = 0; i < jobs.size(); i++) {
            sorted.add(jobs.get(i));
        }

        for (int i = 1; i < sorted.size(); i++) {
            Job current = sorted.get(i);
            int j = i - 1;
            while (j >= 0 && sorted.get(j).profit < current.profit) {
                sorted.set(j + 1, sorted.get(j));
                j--;
            }
            sorted.set(j + 1, current);
        }

        return sorted;
    }

    public static ScheduleResult schedule(DynamicArray<Job> jobs) {
        DynamicArray<ScheduledJob> scheduled = new DynamicArray<>();

        if (jobs == null || jobs.isEmpty()) {
            return new ScheduleResult(scheduled, 0);
        }

        // Step 1: sort a copy of the jobs by profit, highest first
        DynamicArray<Job> sortedJobs = sortByProfitDescending(jobs);

        // Step 2: find the maximum deadline to know how many slots exist
        int maxDeadline = 0;
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).deadline > maxDeadline) {
                maxDeadline = jobs.get(i).deadline;
            }
        }

        // Step 3: slots[1..maxDeadline] track which time units are taken
        DynamicArray<Job> slots = new DynamicArray<>(maxDeadline + 1);
        for (int i = 0; i <= maxDeadline; i++) {
            slots.add(null);
        }

        int totalProfit = 0;

        // Step 4: place each job as late as possible before its deadline
        for (int i = 0; i < sortedJobs.size(); i++) {
            Job job = sortedJobs.get(i);
            int latestSlot = Math.min(job.deadline, maxDeadline);

            for (int slot = latestSlot; slot >= 1; slot--) {
                if (slots.get(slot) == null) {
                    slots.set(slot, job);
                    scheduled.add(new ScheduledJob(job, slot));
                    totalProfit += job.profit;
                    break;
                }
            }
        }

        return new ScheduleResult(scheduled, totalProfit);
    }

    public static void main(String[] args) {
        DynamicArray<Job> jobs = new DynamicArray<>();
        jobs.add(new Job("J1", 2, 60));
        jobs.add(new Job("J2", 1, 100));
        jobs.add(new Job("J3", 3, 20));
        jobs.add(new Job("J4", 2, 40));
        jobs.add(new Job("J5", 1, 20));

        System.out.println("Input Jobs:");
        for (int i = 0; i < jobs.size(); i++) {
            System.out.println("  " + jobs.get(i));
        }

        ScheduleResult result = schedule(jobs);

        System.out.println("\nScheduled Jobs:");
        for (int i = 0; i < result.scheduledJobs.size(); i++) {
            System.out.println("  " + result.scheduledJobs.get(i));
        }

        System.out.println("\nTotal Maximum Profit: " + result.totalProfit);
    }
}