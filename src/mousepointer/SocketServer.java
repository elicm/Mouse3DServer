/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mousepointer;

import java.awt.Dimension;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.awt.Toolkit;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eislas
 */
public class SocketServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Servidor.");

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        double width = screenSize.getWidth();
        double height = screenSize.getHeight();
        System.out.println(width + " x " + height);

        try {

            int portNumber = 8000;
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Esperando Conección");
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in;
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            System.out.println("Enviando resolución");
            out.println(width + "," + height);
            out.flush();

            String inputLine;
            String[] cachos = new String[2];

            int newX, newY;
            Raton raton = new Raton();
            Thread thread = new Thread(raton);
            thread.start();

            //Esperando conección
            while ((inputLine = in.readLine()) != null) {
//                System.out.println("Cliente dice: " + inputLine);

                cachos = inputLine.split(",");

                newX = Integer.parseInt(cachos[0]);
                newY = Integer.parseInt(cachos[1]);
                System.out.println("\nGo to " + newX + ", " + newY);
                raton.newX = newX;
                raton.newY = newY;

                if (inputLine.equals("Bye.")) {
//                    out.println("Bye.");
                    raton.alive = false;
                    break;
                }
                out.println("ok," + inputLine);
                out.flush();
            }
            out.close();
            in.close();
            serverSocket.close();
        } catch (Exception e) {
        }
    }

}
