package lib;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadStopWord {
    public String[] readStopWord(String file){
        List<String> list = new ArrayList<>();
        try {
            BufferedReader in = new BufferedReader(new FileReader(file));

            String str;



            while((str = in.readLine()) != null){
                list.add(str);
            }

        } catch (IOException e){

        }
        return  list.toArray(new String[0]);
    }
}
