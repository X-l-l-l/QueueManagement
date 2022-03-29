package com.example.tema2fx;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.concurrent.Task;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Controller {
    @FXML
    public ProgressBar progress;
    @FXML
    public Button start;
    @FXML
    public TextArea rez;
    @FXML
    public TextField QText;
    @FXML
    public TextField CText;
    @FXML
    public TextField MaxArr;
    @FXML
    public TextField MinArr;
    @FXML
    public TextField MaxProc;
    @FXML
    public TextField MinProc;
    @FXML
    public TextField MaxTime;
    @FXML
    public MenuButton strat;
    @FXML
    public TextField timeBox;


    @FXML
    public int getQueues() {
        return Integer.parseInt(QText.getText());
    }

    @FXML
    public int getClients() {
        return Integer.parseInt(CText.getText());
    }

    @FXML
    public int getMinArr() {
        return Integer.parseInt(MinArr.getText());
    }

    @FXML
    public int getMaxArr() {
        return Integer.parseInt(MaxArr.getText());
    }

    @FXML
    public int getMinProc() {
        return Integer.parseInt(MinProc.getText());
    }

    @FXML
    public int getMaxProc() {
        return Integer.parseInt(MaxProc.getText());
    }

    @FXML
    public int getTime() {
        return Integer.parseInt(MaxTime.getText());
    }

    int str;

    @FXML
    protected void strat1() {
        str = -1;
        strat.setText("Shortest Time");
    }

    @FXML
    protected void strat2() {
        str = 1;
        strat.setText("Shortest Queue");
    }

    static int bar;

    @FXML
    public void start(ActionEvent actionEvent) {
        SelectionPolicy selectionPolicy = (str < 0) ? SelectionPolicy.SHTOREST_TIME : SelectionPolicy.SHORTEST_QUEUE;
        SimulationManager gen = new SimulationManager(getTime(), getMinProc(), getMaxProc(), getMinArr(), getMaxArr(), getQueues(), getClients(), selectionPolicy);
        bar = 0;
        new Thread(gen).start();
        Task task = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                return null;
            }

            @Override
            public void run() {
                final int max = getTime();
                rez.setText("");
                for (int i = 0; i < max; i++) {
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    rez.appendText(SimulationManager.text);
                    updateProgress(bar, getClients());
                    timeBox.setText(String.valueOf(SimulationManager.currentTime));
                    try {
                        FileWriter fileWriter = new FileWriter("C:\\Users\\rares\\Documents\\TP\\Tema2FX\\src\\main\\resources\\rez.txt", true);
                        PrintWriter printWriter = new PrintWriter(fileWriter);
                        printWriter.print(SimulationManager.text);
                        printWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (SimulationManager.text.contains("Done !!")) {
                        rez.appendText(SimulationManager.text);
                        timeBox.setText(String.valueOf(SimulationManager.currentTime - 1));
                        updateProgress(1, 1);
                        try {
                            FileWriter fileWriter = new FileWriter("C:\\Users\\rares\\Documents\\TP\\Tema2FX\\src\\main\\resources\\rez.txt", true);
                            PrintWriter printWriter = new PrintWriter(fileWriter);
                            printWriter.print(SimulationManager.text);
                            printWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }

                }
            }
        };
        progress.progressProperty().bind(task.progressProperty());
        new Thread(task).start();
    }
}