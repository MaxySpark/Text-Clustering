package run;

import lib.*;
public class Main {
    public static void main(String[] args) {
        String rootDir = System.getProperty("user.dir");
        String stopFile = rootDir + "/src/data/stop-word.txt";
        String[] stopWords = new ReadStopWord().readStopWord(stopFile);
        for(int i=0;i<stopWords.length;i++) {
            System.out.println(stopWords[i]);

        }
    }
}

