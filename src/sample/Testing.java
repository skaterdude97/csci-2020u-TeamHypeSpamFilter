package sample;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Scanner;


/**
 * Created by andrei on 10/03/16.
 */
public class Testing {
    private TestFile[] files;
    private File spam, ham;

    public Testing(File dir) {
        spam = new File(dir, "/spam");
        ham = new File(dir, "/ham");
        int numFiles = spam.listFiles().length + ham.listFiles().length;
        files = new TestFile[numFiles];
    }

    private boolean isWord(String str){
        String pattern = "^[a-zA-Z]*$";
        if (str.matches(pattern)){
            return true;
        }
        return false;
    }

    public void test (Map<String, Double> probOfWord) {
        try {
            double sp = 0;
            for (int i = 0; i<spam.listFiles().length; i++) {
                Scanner testInput = new Scanner(spam.listFiles()[i]);
                while (testInput.hasNext()) {
                    String word = testInput.next();
                    if (isWord(word)) {
                        if (probOfWord.containsKey(word)) {
                            sp += Math.log(1-probOfWord.get(word))- Math.log(probOfWord.get(word));
                        }
                    }
                }
                sp = 1/(1+Math.pow(Math.E,sp));


                files[i] = new TestFile(spam.listFiles()[i].getName(), sp, "spam" );
            }
            for (int i = 0; i<ham.listFiles().length; i++) {



                files[i] = new TestFile(ham.listFiles()[i].getName(), sp, "ham" );
            }
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
        }

    }

    public TestFile[] getFiles () {
        return files;
    }
}
