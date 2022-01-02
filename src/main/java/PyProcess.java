import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class PyProcess {
    Process process;
    BufferedReader input;
    public PyProcess(String path) {
        try {
            process = Runtime.getRuntime().exec("python " + '"' + path + '"');
            input = new BufferedReader((new InputStreamReader(process.getInputStream())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        process.destroy();
    }

    public String tryRead() {
        String line;
        try {
            if ((line = input.readLine()) != null) {
                return line;
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

}
