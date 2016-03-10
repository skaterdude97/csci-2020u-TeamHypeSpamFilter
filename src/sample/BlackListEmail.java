package sample;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

/**
 * Created by brad on 10/03/16.
 */
public class BlackListEmail {
    private String email;
    private List<String> blacklist = new ArrayList<>();
    private List<File> fileBlackList = new ArrayList<>();

    public BlackListEmail(String email) {
        email = this.email;

    }

    public void addEmail(String email){
        blacklist.add(email);
    }
//    public void processFile(File file, String[] blacklist) throws IOException {
//        if (file.isDirectory()) {
//            File[] filesInDir = file.listFiles();
//            for (int i = 0; i < filesInDir.length; i++) {
//                processFile(filesInDir[i], blacklist);
//            }
//        } else if (file.exists()) {
//            Scanner scan = new Scanner(file);
//            for (int i = 0; i < blacklist.length; i++) {
//                while (scan.hasNextLine()) {
//                    if (scan.nextLine().contains(blacklist[i])){
//                        fileBlackList.add(file);
//                    }
//                }
//            }
//            }
//        }

}
