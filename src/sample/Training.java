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
    private Map<String, Integer> trainSpamFreq;
    private Map<String, Integer> trainHamFreq;
    private Map<String, Integer> probSpam;
    private Map<String, Integer> probHam;

    public Training() {
        trainSpamFreq = new TreeMap<>();
        trainHamFreq = new TreeMap<>();
        probHam = new TreeMap<>();
        probSpam = new TreeMap<>();
    }

    public void processFile(File file, Map<String, Integer> wordCounts) throws IOException {
        if (file.isDirectory()) {
            // process all of the files recursively
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processFile(filesInDir[i], wordCounts);
            }
        } else if (file.exists()) {
            // load all of the data, and process it into words
            Scanner scanner = new Scanner(file);
            while (scanner.hasNext()) {
                String word = scanner.next();
                if (isWord(word)) {
                    countWord(word, wordCounts);
                }
            }
        }
    }

    private void countWord(String word, Map<String, Integer> wordCounts) {
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

//    public void printWordCounts(int minCount, File outputFile) throws FileNotFoundException {
//        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
//        if (!outputFile.exists() || outputFile.canWrite()) {
//            PrintWriter fout = new PrintWriter(outputFile);
//
//            Set<String> keys = wordCounts.keySet();
//            Iterator<String> keyIterator = keys.iterator();
//
//            while(keyIterator.hasNext()) {
//                String key = keyIterator.next();
//                int count = wordCounts.get(key);
//
//                if (count >= minCount) {
//                    fout.println(key + ": " + count);
//                }
//            }
//            fout.close();
//        } else {
//            System.err.println("Cannot write to output file");
//        }
//    }




    public void train(File dir){
        File spam = new File(dir, "/spam");
        File ham = new File(dir, "/ham");
        File ham2 = new File(dir, "/ham2");
        try {
            processFile(spam, trainSpamFreq);
            processFile(ham, trainHamFreq);
            processFile(ham2, trainHamFreq);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
