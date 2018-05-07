package lib;
import java.util.ArrayList;
import java.util.Arrays;

public class StopWordsHandle {
    
    static String rootDir = System.getProperty("user.dir");
    static String stopFile = rootDir + "/src/data/stop-word.txt";
    static String[] stopWords = new ReadStopWord().readStopWord(stopFile);
    
    public static ArrayList<String> stopWordsList = new ArrayList<String>(Arrays.asList(stopWords));
    
    public static Boolean IsStotpWord(String word)
    {
        if (stopWordsList.contains(word))
            return true;
        else
            return false;
    }
}
