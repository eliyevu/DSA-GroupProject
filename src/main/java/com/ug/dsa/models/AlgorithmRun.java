package com.ug.dsa.models;

public class AlgorithmRun {

    private int runId;
    private String algorithmName;
    private int inputSize;
    private long timeNs;
    private double memoryKb;
    private String dateRun;

    public AlgorithmRun() {
    }

    public AlgorithmRun(int runId,
                        String algorithmName,
                        int inputSize,
                        long timeNs,
                        double memoryKb,
                        String dateRun) {
        this.runId = runId;
        this.algorithmName = algorithmName;
        this.inputSize = inputSize;
        this.timeNs = timeNs;
        this.memoryKb = memoryKb;
        this.dateRun = dateRun;
    }

    public int getRunId() {
        return runId;
    }

    public void setRunId(int runId) {
        this.runId = runId;
    }

    public String getAlgorithmName() {
        return algorithmName;
    }

    public void setAlgorithmName(String algorithmName) {
        this.algorithmName = algorithmName;
    }

    public int getInputSize() {
        return inputSize;
    }

    public void setInputSize(int inputSize) {
        this.inputSize = inputSize;
    }

    public long getTimeNs() {
        return timeNs;
    }

    public void setTimeNs(long timeNs) {
        this.timeNs = timeNs;
    }

    public double getMemoryKb() {
        return memoryKb;
    }

    public void setMemoryKb(double memoryKb) {
        this.memoryKb = memoryKb;
    }

    public String getDateRun() {
        return dateRun;
    }

    public void setDateRun(String dateRun) {
        this.dateRun = dateRun;
    }

    @Override
    public String toString() {
        return "AlgorithmRun{" +
                "runId=" + runId +
                ", algorithmName='" + algorithmName + '\'' +
                ", inputSize=" + inputSize +
                ", timeNs=" + timeNs +
                ", memoryKb=" + memoryKb +
                ", dateRun='" + dateRun + '\'' +
                '}';
    }
}