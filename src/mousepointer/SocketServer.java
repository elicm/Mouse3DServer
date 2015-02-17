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

        int x, y;
        try {
            Robot robot = new Robot();
            int portNumber = 8000;
            ServerSocket serverSocket = new ServerSocket(portNumber);
            System.out.println("Esperando Conección");
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in;
            in = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream())
            );

            System.out.println("Enviando saludo");
            out.println("Hi");

            String inputLine;
            String[] cachos = new String[2];

            int newX, newY, steep = 1;

            //Esperando conección
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Cliente dice: " + inputLine);

                Point location = MouseInfo.getPointerInfo().getLocation();
                x = location.x;
                y = location.y;

                cachos = inputLine.split(",");

                newX = (int) ((width / 2) - Integer.parseInt(cachos[0]));
                newY = (int) ((height / 2) - Integer.parseInt(cachos[1]));
                System.out.println("Go to " + newX + ", " + newY);
                robot.mouseMove(newX, newY);

//                double distancia = Math.ceil(Math.abs(newX - x) + Math.abs(newY - y));
//                steep = (int) (distancia / 100);
                while (newX != x || newY != y) {
                    if (steep > 1) {
                        steep = (int) Math.ceil(steep / 2);
                    }
                    if (newX > x) {
                        x += steep;
                    } else if (newX < x) {
                        x -= steep;
                    }
                    if (newY > y) {
                        y += steep;
                    } else if (newY < y) {
                        y -= steep;
                    }
                    robot.mouseMove(x, y);
                    Thread.sleep(1);
                }
                if (inputLine.equals("Bye.")) {
//                    out.println("Bye.");
                    break;
                }
                out.println("ok," + inputLine);
            }
            out.close();
            in.close();
            serverSocket.close();
        } catch (Exception e) {
        }
    }

}
