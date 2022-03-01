import java.net.Socket;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Scanner;

public class Cliente3 {
    static private final String SERVER_ADDRESS = "192.168.100.56";
    static private Socket socketAlServidor = null;

    static private boolean conectarServidor(int maxIntentos, String SERVER_ADDRESS, int SERVER_PORT){
        boolean exito = false;
        int van = 0;
        while((van<maxIntentos) && !exito){
            try {
                socketAlServidor = new Socket(SERVER_ADDRESS, SERVER_PORT);
                exito = true;
            } catch (Exception e) {
                van++;
                System.err.println("Failures:" + van);
                try {    //esperar 1 seg
                    Thread.sleep(1000);
                } catch (InterruptedException e2) {
                    e2.printStackTrace();
                }
            }
        }
        return exito;
    }

    public static void main(String[] args) {
        boolean exito;
        Scanner escaner2 = new Scanner(System.in);
        System.out.println("Ingrese IP del servidor:");
        String ipServer = escaner2.nextLine();
        System.out.println("Ingresa el puerto");
        int puerto = escaner2.nextInt();

        exito = conectarServidor(10, ipServer, puerto); //10 intentos

        if(!exito){
            System.err.println("Don't know about host:" + SERVER_ADDRESS);
            System.exit(1);           //abortar si hay problemas
        }

        PrintWriter canalSalidaAlServidor = null;
        BufferedReader canalEntradaDelServidor = null;
        try {
            canalSalidaAlServidor = new PrintWriter(socketAlServidor.getOutputStream(),true);
            canalEntradaDelServidor = new BufferedReader(new InputStreamReader(socketAlServidor.getInputStream()));
        } catch (IOException e) {      //abortar si hay problemas
            System.err.println("I/O problem:" + SERVER_ADDRESS);
            System.exit(1);
        }
        BufferedReader entradaStandard = new BufferedReader(new InputStreamReader(System.in));
        String userInput = "";

        try{
            while (!(userInput.equals("END OF SERVICE"))) {
                System.out.print("Text: ");
                userInput = entradaStandard.readLine();
                if (userInput != null) {
                    canalSalidaAlServidor.println(userInput);
                    String respuesta = canalEntradaDelServidor.readLine();
                    if (respuesta != null) {
                        System.out.println("Server answer: " + respuesta);
                    } else {
                        System.out.println("Comm. is closed!");
                    }
                } else {
                    System.err.println("Wrong input!");
                }
            }
            canalEntradaDelServidor.close();
            socketAlServidor.close();
        } catch (Exception e){
            System.err.println(e);
        }
    }
}