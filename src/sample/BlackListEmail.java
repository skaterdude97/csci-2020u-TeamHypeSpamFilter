package sample;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Created by brad on 10/03/16.
 */
public class BlackListEmail {
    private List<File> blackList;

    public BlackListEmail() {
        blackList = new ArrayList<>();

    }

    public boolean addEmail(File dir, String email) throws IOException{
            return processFile(dir,email);
    }

    public List<File> getBlacklist () {
        return blackList;
    }

   private boolean processFile(File file, String email) throws IOException {
        boolean added = false;
        if (file.isDirectory()) {
            File[] filesInDir = file.listFiles();
            for (int i = 0; i < filesInDir.length; i++) {
                processFile(filesInDir[i], email);
            }
        } else if (file.exists()) {
            Scanner scan = new Scanner(file);
            while (scan.hasNextLine()) {
                if (scan.nextLine().contains(email)){
                    blackList.add(file);
                    added = true;
                }
            }
        }
        return added;
        }

}
