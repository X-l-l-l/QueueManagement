package com.example.tema2fx;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SimulationManager implements Runnable {

    public int timeLimit = 10;
    public int maxProcessingTime = 5;
    public int minProcessingTime = 2;

    public int minArrivingTime = 1;
    public int maxArrivingTime = 4;
    public int numberOfServers = 3;
    public int numberOfClients = 5;
    public SelectionPolicy selectionPolicy = SelectionPolicy.SHORTEST_QUEUE;

    public static int total = 0;

    public SimulationManager(int timeLimit, int minProcessingTime, int maxProcessingTime, int minArrivingTime, int maxArrivingTime, int numberOfServers, int numberOfClients, SelectionPolicy selectionPolicy) {
        this.timeLimit = timeLimit;
        this.maxProcessingTime = maxProcessingTime;
        this.minProcessingTime = minProcessingTime;
        this.minArrivingTime = minArrivingTime;
        this.maxArrivingTime = maxArrivingTime;
        this.numberOfServers = numberOfServers;
        this.numberOfClients = numberOfClients;
        this.selectionPolicy = selectionPolicy;
        scheduler = new Scheduler(numberOfServers, numberOfClients);
        scheduler.changeStrategy(selectionPolicy);
        generateNRandomTasks();
    }

    public static String text = "";

    private Scheduler scheduler;
    //GUI
    private List<Task> generatedTasks = new ArrayList<>();

    private void generateNRandomTasks() {

        for (int i = 0; i < numberOfClients; i++) {
            Task task = new Task(minProcessingTime, maxProcessingTime, minArrivingTime, maxArrivingTime);
            task.setID(i + 1);
            generatedTasks.add(task);
        }
        generatedTasks.sort(new Sort());
    }

    private int avgProc() {
        int avg = 0;
        for (int i = 0; i < generatedTasks.size(); i++) {
            avg += generatedTasks.get(i).getServiceTime();
        }
        return avg;
    }
    static int currentTime = 0;

    @Override
    public void run() {
        currentTime = 0;
        int peekStart = 0, peekEnd = 0;
        int newNrClients = 0, oldNrClients = 0;
        int avg = avgProc();
        while (currentTime < timeLimit) {

            System.out.println("\nTime " + currentTime);
            text = "\nTime " + currentTime + "\n";

            for (int i = 0; i < generatedTasks.size(); i++) {
                avg += generatedTasks.get(i).getServiceTime();
                if (generatedTasks.get(i).getArrivalTime() == currentTime) {
                    scheduler.dispatchTask(generatedTasks.get(i));
                    generatedTasks.remove(generatedTasks.get(i));
                    i--;
                }
            }
            System.out.println("Waiting: " + generatedTasks);
            text += "Waiting: " + generatedTasks + "\n";
            newNrClients = scheduler.nrOfClients();

            if (newNrClients > oldNrClients) {
                peekStart = currentTime;
                peekEnd = peekStart;
                oldNrClients = newNrClients;
            } else if (newNrClients == oldNrClients && peekEnd == currentTime - 1) {
                peekEnd++;
            }

            text += scheduler;
            System.out.println(scheduler);
            currentTime++;

            if (generatedTasks.size() == 0) {
                int ok = 0;
                for (Server s : scheduler.getServers())
                    if (s.getTasks().size() != 0)
                        ok = 1;

                if (ok == 0) {
                    text += "\nDone !!";
                    System.out.println("Done !!");
                    System.out.println("Peek start: " + peekStart + " Peek end: " + peekEnd);
                    System.out.println((1.0 * total) / numberOfClients);
                    text += "\nPeek start: " + peekStart + " Peek end: " + peekEnd + "\n" + "Avg service: " + (1.0 * total) / numberOfClients + "  Avg waiting: " + (1.0 * avg) / numberOfClients;
                    for (Thread t : scheduler.getThreads())
                        t.stop();
                    return;
                }

            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        text += "\nDone !!";
        System.out.println("Done !!");
        System.out.println("Peek start: " + peekStart + " Peek end: " + peekEnd);
        System.out.println((1.0 * total) / numberOfClients);
        text += "\nPeek start: " + peekStart + " Peek end: " + peekEnd + "\n" + "Avg service: " + (1.0 * total) / numberOfClients + "  Avg waiting: " + (1.0 * avg) / numberOfClients;
        for (Thread t : scheduler.getThreads())
            t.stop();
    }

    public static void main(String[] args) {
//        SimulationManager gen = new SimulationManager();
//        Thread t = new Thread(gen, "SimulationManager");
//        t.start();
    }

}
