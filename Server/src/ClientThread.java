import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * Created by Will on 12/1/2015.
 * Uses java threads to multithread the server to allow for recieving two simultaneous responses
 * Used to implement the waitForDrawings() method in GamerServer.java
 */
public class ClientThread extends Thread {
    private ClientConnection client;
    private volatile BufferedImage drawing;
    private CyclicBarrier gate;

    public ClientThread(ClientConnection client, CyclicBarrier gate) {
        this.client = client;
        this.gate = gate;
    }

    @Override
    public void run() {
        BufferedImage drawing = null;

        while(true) {
            try {
                gate.await();
                drawing = ImageIO.read(client.getSocket().getInputStream());
                System.out.println("Go!");

                if(drawing == null) {
                    break;
                }

            } catch(IOException e) {
                System.out.println(e.getMessage());
            } catch(InterruptedException e) {
                System.out.println(e.getMessage());
            } catch(BrokenBarrierException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public BufferedImage getDrawing() {
        return drawing;
    }
}
