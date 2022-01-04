import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PyProcess {
    Process process;
    BufferedReader input;
    public PyProcess(String path) {
        try {
            process = new ProcessBuilder("python", '"' + path + '"',
                    String.valueOf(GamePanel.camId), String.valueOf(GamePanel.camInverted)).start();
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
