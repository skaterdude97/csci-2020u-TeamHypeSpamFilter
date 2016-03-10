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
    private Map<String, Double> probOfWord;

    public Training() {
        trainSpamFreq = new TreeMap<>();
        trainHamFreq = new TreeMap<>();
        probOfWord = new TreeMap<>();
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

    public void printWordCounts(int minCount, File outputFile) throws FileNotFoundException {
        System.out.println("Saving word counts to " + outputFile.getAbsolutePath());
        if (!outputFile.exists() || outputFile.canWrite()) {
            PrintWriter fout = new PrintWriter(outputFile);

            Set<String> keys = probOfWord.keySet();
            Iterator<String> keyIterator = keys.iterator();

            while(keyIterator.hasNext()) {
                String key = keyIterator.next();
                double count = probOfWord.get(key);

                if (count >= minCount) {
                    fout.println(key + ": " + count);
                }
            }
            fout.close();
        } else {
            System.err.println("Cannot write to output file");
        }
    }


    public void setProbOfWord(Map<String, Integer> spam, Map<String, Integer> ham, int numHam, int numSpam){
        Iterator entries = spam.entrySet().iterator();

        while(entries.hasNext()){
            Map.Entry entry = (Map.Entry) entries.next();
            String key = (String) entry.getKey();
            Integer value = (Integer) entry.getValue();
            if(ham.get(key) == null){
                double probSpam = value/numSpam;
                double prob = probSpam/(probSpam);
                probOfWord.put(key, prob);
                System.out.println(key + " " + prob);
            }
            else {
                double probHam = ham.get(key) / numHam;
                System.out.println(probHam);

                double probSpam = value/numSpam;
                double prob = probSpam/(probSpam + probHam);
                probOfWord.put(key, prob);
                System.out.println(key + " " + prob);

            }
        }
    }

    public Map<String, Double> getProbOfWord(){
        return probOfWord;
    }

    public void train(File dir){
        File spam = new File(dir, "/spam");
        File ham = new File(dir, "/ham");
        File ham2 = new File(dir, "/ham2");
        int numSpam = spam.listFiles().length;
        int numHam = ham.listFiles().length + ham2.listFiles().length;
        System.out.println(numSpam + " " + numHam
        );
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

        setProbOfWord(trainSpamFreq, trainHamFreq, numHam, numSpam);
        File prob = new File("probability.txt");
        try {
            printWordCounts(2, prob);
        }
        catch(FileNotFoundException e){
            e.printStackTrace();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
