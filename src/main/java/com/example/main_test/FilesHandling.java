
package com.example.main_test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class FilesHandling {






    public void CreateFile(String path) throws IOException{
        File f1 = new File(path);
        if(!f1.exists()){
            f1.createNewFile();
            System.out.println("The file has been created.");
        }
        else
            System.out.println("The file already exists.");

    }

    public void CreateDirectorie(String path){
        File f1 = new File(path);

        if(!f1.exists()){
            f1.mkdirs();
            System.out.println("The Folder has been created.");
        }
        else
            System.out.println("The Folder already exists.");
    }

    public void PrintDF(String path){

        File f1  = new File(path);
        String [] arr = f1.list();
        for(String s : arr){
            System.out.println(s);
        }
    }

    public void DeleteDirectorie(String path){
        File f1 = new File(path);
        if(f1.isDirectory()){
            f1.delete();
            System.out.println("The Folder has been deleted.");
        }
        else
            System.out.println("Wrong path !");
    }




    public int NumberOfFiles(String path){
        File f1  = new File(path);
        int count = 0;
        String [] arr = f1.list();
        for(String s : arr){
            File f2 = new File(f1,s);
            if(f2.isFile()){
                count++;
            }

        }
        return count;

    }


    public  int countWordsInFile(String filePath) {
        int wordCount = 0;

        File file = new File(filePath);
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;


            while ((line = reader.readLine()) != null) {

                String[] words = line.trim().split("\\s+");
                wordCount += words.length;
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return wordCount;
    }





    public String findLongestWord(String filePath) {
        String longestWord = "";
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");

                for (String word : words) {
                    word = word.replaceAll("[^a-zA-Z0-9]", "");
                    if (word.length() > longestWord.length() && !word.equals("is") && !word.equals("are") && !word.equals("you")) {
                        longestWord = word;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return longestWord;
    }


    public String findShortestWord(String filePath) {
        String shortestWord = null;
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");

                for (String word : words) {
                    word = word.replaceAll("[^a-zA-Z0-9]", "");
                    if (shortestWord == null || word.length() < shortestWord.length() && !word.equals("is") && !word.equals("are") && !word.equals("you")) {
                        shortestWord = word;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return shortestWord;
    }


    public String findLongestWordInFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return "The specified path is not a valid directory.";
        }

        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            String longestWord = "";
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    String word = findLongestWord(file.getAbsolutePath());
                    if (word.length() > longestWord.length()) {
                        longestWord = word;
                    }
                }
            }
            return longestWord.isEmpty() ? "No words found in folder." : longestWord;
        }
        return "No files found in folder.";
    }

    public String findShortestWordInFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists() || !folder.isDirectory()) {
            return "The specified path is not a valid directory.";
        }

        File[] files = folder.listFiles();
        if (files != null && files.length > 0) {
            String shortestWord = null;
            for (File file : files) {
                if (file.isFile() && file.getName().endsWith(".txt")) {
                    String word = findShortestWord(file.getAbsolutePath());
                    if (shortestWord == null || (word != null && word.length() < shortestWord.length())) {
                        shortestWord = word;
                    }
                }
            }
            return shortestWord == null ? "No words found in folder." : shortestWord;
        }
        return "No files found in folder.";
    }

    public int countIs(String filePath) {
        int count = 0;
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");
                for (String word : words) {
                    if (word.equalsIgnoreCase("is")) {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return count;
    }

    public int countAre(String filePath) {
        int count = 0;
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");
                for (String word : words) {
                    if (word.equalsIgnoreCase("are")) {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return count;
    }

    public int countYou(String filePath) {
        int count = 0;
        File file = new File(filePath);

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] words = line.trim().split("\\s+");
                for (String word : words) {
                    if (word.equalsIgnoreCase("you")) {
                        count++;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading the file: " + e.getMessage());
        }

        return count;
    }












}







