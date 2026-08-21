package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;
import com.ug.dsa.datastructures.Heap;

/**
 * Greedy job scheduler using the project's custom Min-Heap.
 * Highest-profit jobs are extracted first.
 */
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

    public static ScheduleResult schedule(DynamicArray<Job> jobs) {
        DynamicArray<ScheduledJob> scheduled = new DynamicArray<>();
        if (jobs == null || jobs.isEmpty()) return new ScheduleResult(scheduled, 0);

        int maxDeadline = 0;
        for (int i = 0; i < jobs.size(); i++) {
            if (jobs.get(i).deadline > maxDeadline) maxDeadline = jobs.get(i).deadline;
        }

        DynamicArray<Job> slots = new DynamicArray<>(maxDeadline + 1);
        for (int i = 0; i <= maxDeadline; i++) slots.add(null);

        // Min-Heap priority is negative profit, so the largest profit is extracted first.
        Heap<Job> profitHeap = new Heap<>();
        for (int i = 0; i < jobs.size(); i++) {
            Job job = jobs.get(i);
            profitHeap.insert(job, -job.profit);
        }

        int totalProfit = 0;
        while (!profitHeap.isEmpty()) {
            Job job = profitHeap.extractMin();
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
}
