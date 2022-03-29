package com.example.tema2fx;

import java.util.List;

public class ConcreteStrategyQueue implements Strategy {

    @Override
    public void addTask(List<Server> servers, Task t) {
        int min = Integer.MAX_VALUE;
        int index = 0;

        for (int i = 0; i < servers.size(); i++) {
            if (servers.get(i).getTasks().size() < min) {
                min = servers.get(i).getTasks().size();
                index = i;
            }
        }
        servers.get(index).addTask(t);
    }
}
