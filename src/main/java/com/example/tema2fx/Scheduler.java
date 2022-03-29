package com.example.tema2fx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum SelectionPolicy {
    SHORTEST_QUEUE, SHTOREST_TIME
}

public class Scheduler {
    private int maxNrServers;
    private int maxTasksPerServer;
    private List<Server> servers;
    private List<Thread> threads;
    private Strategy strategy;

    public Scheduler(int maxNrServers, int maxTasksPerServer) {
        servers = new ArrayList<>();
        threads = new ArrayList<>();
        for (int i = 0; i < maxNrServers; i++) {
            Server server = new Server();
            servers.add(server);
            Thread thread = new Thread(server, "server" + (i + 1));
            threads.add(thread);
            thread.start();
        }
    }

    public void changeStrategy(SelectionPolicy selectionPolicy) {
        if (selectionPolicy == SelectionPolicy.SHORTEST_QUEUE)
            strategy = new ConcreteStrategyQueue();
        if (selectionPolicy == SelectionPolicy.SHTOREST_TIME)
            strategy = new ConcreteStrategyTime();
    }

    public void dispatchTask(Task t) {
        try {
            strategy.addTask(servers, t);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int nrOfClients() {
        int num = 0;
        for (Server s : servers) {
            num += s.getTasks().size();
        }
        return num;
    }

    public List<Server> getServers() {
        return servers;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    @Override
    public String toString() {
        String s = "";
        int i = 0;
        for (Server srv : servers) {
            s += "Queue " + i++ + ": " + srv + "\n";
        }
        return s;
    }
}
