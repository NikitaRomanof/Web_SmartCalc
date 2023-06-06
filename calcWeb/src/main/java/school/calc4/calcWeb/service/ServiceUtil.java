package school.calc4.calcWeb.service;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;


@Service
public class ServiceUtil {
    private static final String PATH_HISTORY = System.getProperty("user.dir") + "/history.txt";

    public void sateFileHistory() {
        File history = new File(PATH_HISTORY);
        if (!history.isFile()) {
            try {
                if (!history.isFile()) history.createNewFile();
            } catch (IOException ignored) {
            }
        }
    }

    public void saveFileHistory(String input) {
        sateFileHistory();
        try(FileWriter writer = new FileWriter(PATH_HISTORY, true);
            BufferedWriter wr = new BufferedWriter(writer)) {
            wr.write("\n");
            wr.write(input);
            wr.flush();
        } catch (Exception ignored) {
        }

    }

    public List<String> getAllHistory() {
        List<String> allHistory = new ArrayList<>();
        try(FileReader reader = new FileReader(PATH_HISTORY);
            BufferedReader bufferReader = new BufferedReader(reader)) {
            String line;
            while ((line = bufferReader.readLine()) != null) {
                allHistory.add(line);
            }
        } catch (Exception ignored) {
        }
        return allHistory;
    }

    public void cleanHistory() {
        try(FileWriter writer = new FileWriter(PATH_HISTORY, false);
            BufferedWriter wr = new BufferedWriter(writer)) {
            wr.write("\n");
            wr.flush();
        } catch (Exception ignored) {
        }
    }
}
