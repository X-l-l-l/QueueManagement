package com.example.tema2fx;

import java.util.List;

public class ConcreteStrategyTime implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = Integer.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).getWaitingPeriod().get() < min) {
                min = servers.get(i).getWaitingPeriod().get();
                index = i;
            }
        }
        servers.get(index).addTask(t);
    }
}
