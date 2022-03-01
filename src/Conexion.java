import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Conexion implements Runnable{
    Socket clientSocket;

    public Conexion(Socket clientSocket){
        this.clientSocket = clientSocket;
    }

    private static int numeroDeVocales(String frase) {
        int res = 0;
        String fraseMin = frase.toLowerCase();

        for (int i = 0; i < fraseMin.length(); ++i) {
            switch (fraseMin.charAt(i)) {
                case 'a', 'á', 'e', 'é', 'i', 'í', 'o', 'ó', 'u', 'ú' -> res++;
                default -> {
                }
                // se ignoran las demás letras
            }
        }
        return res;
    }

    @Override
    public void run() {
        PrintWriter salHaciaCliente = null;
        BufferedReader entDesdeCliente = null;

        try {
            salHaciaCliente = new PrintWriter(clientSocket.getOutputStream(), true);
            entDesdeCliente = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        } catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }

        String inputLine;
        try {
            inputLine = entDesdeCliente.readLine();
            while ((inputLine != null) && (!inputLine.equals("END OF SERVICE"))) {
                String respuesta = "'" + inputLine + "' has " + numeroDeVocales(inputLine) + " vowels";
                salHaciaCliente.println(respuesta);
                inputLine = entDesdeCliente.readLine();
            }
            salHaciaCliente.close();
        } catch (IOException e) {
            System.err.println(e);
            System.exit(-1);
        }
        System.out.println("Bye, bye.");
    }
}

