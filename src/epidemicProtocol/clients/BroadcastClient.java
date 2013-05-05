package epidemicProtocol.clients;

public class BroadcastClient extends Thread {

    private int targetPort;

    public BroadcastClient(int port) {
        this.targetPort = port;
    }

    @Override
    public void run() {
    }
}
