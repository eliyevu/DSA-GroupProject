package com.ug.dsa.algorithms;

import com.ug.dsa.datastructures.DynamicArray;
import static com.ug.dsa.algorithms.GreedyScheduler.schedule;


public class GreedySchedulerTest {

    public static void main(String[] args) {
        DynamicArray<GreedyScheduler.Job> jobs = new DynamicArray<>();
        jobs.add(new GreedyScheduler.Job("J1", 2, 60));
        jobs.add(new GreedyScheduler.Job("J2", 1, 100));
        jobs.add(new GreedyScheduler.Job("J3", 3, 20));
        jobs.add(new GreedyScheduler.Job("J4", 2, 40));
        jobs.add(new GreedyScheduler.Job("J5", 1, 20));

        System.out.println("Input Jobs:");
        for (int i = 0; i < jobs.size(); i++) {
            System.out.println("  " + jobs.get(i));
        }

        GreedyScheduler.ScheduleResult result = schedule(jobs);

        System.out.println("\nScheduled Jobs:");
        for (int i = 0; i < result.scheduledJobs.size(); i++) {
            System.out.println("  " + result.scheduledJobs.get(i));
        }

        System.out.println("\nTotal Maximum Profit: " + result.totalProfit);
    }

}
