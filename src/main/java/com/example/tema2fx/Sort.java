package com.example.tema2fx;

import java.util.Comparator;

public class Sort implements Comparator<Task> {


    public int compare(Task a, Task b)
    {
        return a.getArrivalTime() - b.getArrivalTime();
    }

}
