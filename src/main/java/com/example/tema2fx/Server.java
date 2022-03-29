package com.example.tema2fx;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class Server implements Runnable {

    private BlockingQueue<Task> tasks;
    private AtomicInteger waitingPeriod;

    public Server() {
        tasks = new LinkedBlockingQueue<>();
        waitingPeriod = new AtomicInteger(0);
    }

    public void addTask(Task task) {
        try {
            tasks.put(task);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        waitingPeriod.getAndAdd(task.getServiceTime());

        SimulationManager.total += waitingPeriod.get();
    }

    @Override
    public void run() {
        Task t;
        while (true) {
            if (tasks.size() != 0) {

                t = tasks.peek();

                t.setServiceTime(t.getServiceTime() - 1);

                waitingPeriod.decrementAndGet();

                if (t.getServiceTime() == 0) {
                    tasks.remove(t);
                    Controller.bar++;
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public BlockingQueue<Task> getTasks() {
        return tasks;
    }

    public AtomicInteger getWaitingPeriod() {
        return waitingPeriod;
    }

    @Override
    public String toString() {
        String s = "";
        for (Task t : tasks) {
            s += t;
        }
        return s;
    }
}
