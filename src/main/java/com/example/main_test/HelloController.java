package com.example.main_test;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.concurrent.*;

public class HelloController {

    @FXML
    private TextField directoryPath;

    @FXML
    private CheckBox includeSubdirectoriesCheckbox;

    @FXML
    private Button start1;

    @FXML
    private TableView<FileStats> statisticsTable;

    @FXML
    private TableColumn<FileStats, String> fileName_Text;

    @FXML
    private TableColumn<FileStats, Integer> NWords;

    @FXML
    private TableColumn<FileStats, Integer> ISN;

    @FXML
    private TableColumn<FileStats, Integer> AREN;

    @FXML
    private TableColumn<FileStats, Integer> YOUN;

    @FXML
    private TableColumn<FileStats, String> LONG;

    @FXML
    private TableColumn<FileStats, String> SHORT;

    private String longWord = "";  // تعيين قيمة فارغة مبدئية
    private String shortWord = ""; // تعيين قيمة فارغة مبدئية

    @FXML
    private TextArea logArea;

    @FXML
    private Label progressArea1;
    @FXML
    private Label progressArea2;

    private ObservableList<FileStats> fileList = FXCollections.observableArrayList();

    private FilesHandling filesHandling = new FilesHandling();

    private ExecutorService executorService;
    private Semaphore semaphore;

    @FXML
    public void initialize() {
        fileName_Text.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFileName()));
        NWords.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getWordCount()).asObject());
        ISN.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getIsCount()).asObject());
        AREN.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getAreCount()).asObject());
        YOUN.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getYouCount()).asObject());
        LONG.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getLongestWord()));
        SHORT.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getShortestWord()));

        statisticsTable.setItems(fileList);
    }

    @FXML
    public void handleBrowse() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Folder");

        File initialDirectory = new File(System.getProperty("user.home"));
        directoryChooser.setInitialDirectory(initialDirectory);

        File selectedDirectory = directoryChooser.showDialog(new Stage());

        if (selectedDirectory != null) {
            directoryPath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    public void handleStart() {
        String path = directoryPath.getText();

        if (path != null && !path.isEmpty()) {
            File directory = new File(path);

            if (!directory.exists() || !directory.isDirectory()) {
                logUpdate("Invalid directory path.");
                return;
            }
            progressArea1.setText("");
            progressArea2.setText("");

            fileList.clear();
            logArea.clear();
            executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            semaphore = new Semaphore(8);

            if (directory.isDirectory()) {
                if (includeSubdirectoriesCheckbox.isSelected()) {
                    processDirectoryWithSubdirs(directory);
                } else {
                    processDirectory(directory);
                }
            }
        } else {
            logUpdate("Please enter a valid directory path.");
        }
    }

    private void processDirectoryWithSubdirs(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        processDirectoryWithSubdirs(file);
                    } else if (file.getName().endsWith(".txt")) {

                        // تحديث القيم الطويلة والقصيرة داخل الدليل
                        String longestWordInFolder = filesHandling.findLongestWordInFolder(file.getParent());
                        synchronized (this) {
                            if (longWord.isEmpty() || longWord.length() < longestWordInFolder.length()) {
                                longWord = longestWordInFolder;
                            }

                            String shortestWordInFolder = filesHandling.findShortestWordInFolder(file.getParent());
                            if (shortWord.isEmpty() || shortWord.length() > shortestWordInFolder.length()) {
                                shortWord = shortestWordInFolder;
                            }
                        }

                        executorService.submit(() -> processFile(file));
                    }
                }
            }
        }
    }

    private void processDirectory(File directory) {
        if (directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {

                        // تحديث القيم الطويلة والقصيرة داخل الدليل
                        String longestWordInFolder = filesHandling.findLongestWordInFolder(file.getParent());
                        synchronized (this) {
                            if (longWord.isEmpty() || longWord.length() < longestWordInFolder.length()) {
                                longWord = longestWordInFolder;
                            }

                            String shortestWordInFolder = filesHandling.findShortestWordInFolder(file.getParent());
                            if (shortWord.isEmpty() || shortWord.length() > shortestWordInFolder.length()) {
                                shortWord = shortestWordInFolder;
                            }
                        }

                        executorService.submit(() -> processFile(file));
                    }
                }
            }
        }
    }

    private void processFile(File file) {
        try {
            logUpdate("Processing file: " + file.getName());
            semaphore.acquire();

            Thread.sleep(500); // Simulate processing delay
            logUpdate("Counting words in: " + file.getName());
            int wordCount = filesHandling.countWordsInFile(file.getAbsolutePath());

            Thread.sleep(600);
            logUpdate("Counting 'is' occurrences in: " + file.getName());
            int isCount = filesHandling.countIs(file.getAbsolutePath());

            Thread.sleep(700);
            logUpdate("Counting 'are' occurrences in: " + file.getName());
            int areCount = filesHandling.countAre(file.getAbsolutePath());

            Thread.sleep(800);
            logUpdate("Finding longest word in: " + file.getName());
            String longestWordInFile = filesHandling.findLongestWord(file.getAbsolutePath());

            Thread.sleep(900);
            logUpdate("Finding shortest word in: " + file.getName());
            String shortestWordInFile = filesHandling.findShortestWord(file.getAbsolutePath());


            Platform.runLater(() -> {

                progressArea1.setText(longWord);
                progressArea2.setText(shortWord);
                fileList.add(new FileStats(file.getName(), wordCount, isCount, areCount, 0, longestWordInFile, shortestWordInFile));
            });

            logUpdate("File processing completed: " + file.getName());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logUpdate("Error processing file: " + file.getName());
        } finally {
            semaphore.release();
        }
    }

    private void logUpdate(String message) {
        Platform.runLater(() -> logArea.appendText(message + "\n"));
    }

    public static class FileStats {
        private final String fileName;
        private final int wordCount;
        private final int isCount;
        private final int areCount;
        private final int youCount;
        private final String longestWord;
        private final String shortestWord;

        public FileStats(String fileName, int wordCount, int isCount, int areCount, int youCount, String longestWord, String shortestWord) {
            this.fileName = fileName;
            this.wordCount = wordCount;
            this.isCount = isCount;
            this.areCount = areCount;
            this.youCount = youCount;
            this.longestWord = longestWord;
            this.shortestWord = shortestWord;
        }

        public String getFileName() {
            return fileName;
        }

        public int getWordCount() {
            return wordCount;
        }

        public int getIsCount() {
            return isCount;
        }

        public int getAreCount() {
            return areCount;
        }

        public int getYouCount() {
            return youCount;
        }

        public String getLongestWord() {
            return longestWord;
        }

        public String getShortestWord() {
            return shortestWord;
        }
    }
}
