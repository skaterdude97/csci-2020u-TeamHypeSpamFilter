package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Scanner;
import javafx.collections.*;


/**
 * Created by andrei on 10/03/16.
 */
public class Testing {
    private ObservableList<TestFile> files;
    private File spam, ham;
    private int correctSpam=0, spamCount=0;
    private double accuracy, precision;
    private BlackListEmail blackList;

    public Testing(File dir) {
        files = FXCollections.observableArrayList();
        spam = new File(dir, "/spam");
        ham = new File(dir, "/ham");
        blackList = new BlackListEmail();
    }

    public double getAccuracy () {return accuracy;}

    public double getPrecision () {return precision;}

    public void addToBlacklist (String email) throws IOException {
        if (!blackList.addEmail(ham, email))
            blackList.addEmail(spam,email);
    }

    private boolean isWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }

    public void test (Map<String, Double> probOfWord) {
        if (!files.isEmpty()) files.removeAll();
        try {

            for (int i = 0; i<spam.listFiles().length; i++) {
                if (!blackList.getBlacklist().contains(spam.listFiles()[i])) {
                    Scanner testInput = new Scanner(spam.listFiles()[i]);
                    double sp;
                    double n = 0;
                    while (testInput.hasNext()) {
                        String word = testInput.next();
                        if (isWord(word)) {
                            if (probOfWord.containsKey(word)) {
                                if (probOfWord.get(word) == 1) {
                                    n += (Math.log(1 - 0.99999999999) - Math.log(0.9999999999));
                                } else if (probOfWord.get(word) != 0) {
                                    n += (Math.log(1 - probOfWord.get(word)) - Math.log(probOfWord.get(word)));
                                } else {
                                    n += (Math.log(1 - 0.00000000001) - Math.log(0.00000000001));
                                }
                            }
                        }
                    }

                    sp = 1 / (1 + Math.pow(Math.E, n));

                    if (sp > 0.5) {
                        correctSpam++;
                        spamCount++;
                    }

                    files.add(new TestFile(spam.listFiles()[i].getName(), sp, "Spam"));
                } else {
                    correctSpam++;
                    spamCount++;
                    files.add(new TestFile(spam.listFiles()[i].getName(), 1.0, "Spam"));
                }
            }
            for (int i = 0; i<ham.listFiles().length; i++) {
                if (!blackList.getBlacklist().contains(ham.listFiles()[i])) {
                    Scanner testInput = new Scanner(ham.listFiles()[i]);
                    double sp;
                    double n = 0;
                    while (testInput.hasNext()) {
                        String word = testInput.next();
                        if (isWord(word)) {
                            if (probOfWord.containsKey(word)) {
                                if (probOfWord.get(word) == 1) {
                                    n += (Math.log(1 - 0.99999999999) - Math.log(0.9999999999));
                                } else if (probOfWord.get(word) != 0) {
                                    n += (Math.log(1 - probOfWord.get(word)) - Math.log(probOfWord.get(word)));
                                } else {
                                    n += (Math.log(1 - 0.00000000001) - Math.log(0.00000000001));
                                }
                            }
                        }
                    }
                    sp = 1 / (1 + Math.pow(Math.E, n));

                    if (sp > 0.5) {
                        spamCount++;
                    }

                    files.add(new TestFile(ham.listFiles()[i].getName(), sp, "Ham"));
                } else {
                    correctSpam++;
                    spamCount++;
                    files.add(new TestFile(ham.listFiles()[i].getName(), 1.0, "Ham"));
                }

            }

            accuracy = 1-((spamCount-correctSpam)/(double)files.size());
            precision = correctSpam/(double)spamCount;
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }
    }

    public ObservableList<TestFile> getFiles () {
        return files;
    }
}
