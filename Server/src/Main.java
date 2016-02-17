/**
 * Created by Will on 11/25/2015.
 */
import java.awt.*;
import java.io.Console;
import java.io.IOException;
import java.io.InterruptedIOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedIOException{
        Console console = System.console();

        if(console == null && !GraphicsEnvironment.isHeadless()) {
            String fileName = Main.class.getProtectionDomain().getCodeSource().getLocation().toString().substring(6);
            Runtime.getRuntime().exec(new String[]{"cmd","/c","start","cmd","/k","java -jar \"" + fileName + "\""});
        } else {
            GameServer.main(new String[0]);
            System.out.println("Program has ended, please type 'exit' to close console");
        }
    }
}
