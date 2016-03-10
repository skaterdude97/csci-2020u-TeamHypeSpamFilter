package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

/**
 * Created by brad on 08/03/16.
 */
public class Training {
    private Map<String, Integer> wordCounts;

    public Training() {
        wordCounts = new TreeMap<>();
    }

    public void processFile(File file) throws IOException {
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processFile(filesInDir[i]);
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isWord(word)) {
                    countWord(word);
                }
            }
        }
    }

    private void countWord(String word) {
        if (wordCounts.containsKey(word)) {
            int oldCount = wordCounts.get(word);
            wordCounts.put(word, oldCount + 1);
        } else {
            wordCounts.put(word, 1);
        }
    }

    private boolean isWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }

    public void printWordCounts(int minCount, File outputFile) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);

            Set<String> keys = wordCounts.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                int count = wordCounts.get(key);

                if (count >= minCount) {
                    fout.println(key + ": " + count);
                }
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }




    public void train(){

    }
}
