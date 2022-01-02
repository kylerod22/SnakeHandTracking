import java.io.IOException;
import java.util.List;

public class PyProcess {
    Process process;
    public PyProcess(String path) {
        try {
            process = Runtime.getRuntime().exec("python " + '"' + path + '"');
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void exit() {
        process.destroy();
    }

}
