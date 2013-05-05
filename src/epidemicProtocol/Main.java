package epidemicProtocol;

import epidemicProtocol.clients.BroadcastClient;
import epidemicProtocol.clients.MessagesSender;
import epidemicProtocol.control.TargetsList;
import epidemicProtocol.servers.SpreadServer;
import epidemicProtocol.servers.TargetsListServer;

public class Main {

    private static final int TARGETS_LIST_PORT = 60000;
    private static final int SPREAD_SERVER_PORT = 60001;

    public static void main(String[] args) {
        TargetsList targetsList = new TargetsList();
        System.out.println("Starting Targets List Sever...");
        new TargetsListServer(targetsList, TARGETS_LIST_PORT).start();
        System.out.println("Starting Broadcast Client...");
        new BroadcastClient(TARGETS_LIST_PORT).start();
        System.out.println("Starting Spread Server...");
        new SpreadServer(targetsList, SPREAD_SERVER_PORT).start();
        System.out.println("Starting Messages Sender...");
        new MessagesSender(SPREAD_SERVER_PORT).start();
        System.out.println("Starting Epidemic Protocol...");
    }
}
