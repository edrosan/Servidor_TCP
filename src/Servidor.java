import java.net.*;
import java.io.IOException;

public class Servidor{

    private static ServerSocket creaListenSocket(int serverSockNum, int backlog, InetAddress bindAddr) {
        ServerSocket server = null;
        try {
            server = new ServerSocket(serverSockNum, backlog, bindAddr );
        } catch (IOException e) {
            System.err.println("Problems in port: " + serverSockNum);
            System.exit(-1);
        }
        return server;
    }

    private static Socket creaClientSocket(ServerSocket server) {
        Socket res = null;
        try {
            res = server.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        return res;
    }

    public static void main(String[] args) {
        InetAddress IP = null;
        try {
            IP = InetAddress.getLocalHost();
            //IP = InetAddress.getByName("192.168.100.56");
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        System.out.println(IP);
        int SERVER_PORT = 1050;

        ServerSocket serverSocket = creaListenSocket(SERVER_PORT, 5 , IP);

        while (true) {
            Socket clientSocket = creaClientSocket(serverSocket);
            new Thread(new Conexion(clientSocket)).start();
        }
    }
}
