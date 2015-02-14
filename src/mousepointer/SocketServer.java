/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mousepointer;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

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

            Double inclinacionX, inclinacionY;
            int newX, newY, sentidoX, sentidoY;

            //Esperando conección
            while ((inputLine = in.readLine()) != null) {
                System.out.println("Cliente dice: " + inputLine);

                Point location = MouseInfo.getPointerInfo().getLocation();
                x = location.x;
                y = location.y;

                cachos = inputLine.split(",");

                if(Double.parseDouble(cachos[0])>0){
                    sentidoX=1;
                }else{
                    sentidoX=-1;
                }

                if(Double.parseDouble(cachos[1])>0){
                    sentidoY=1;
                }else{
                    sentidoY=-1;
                }
                
                inclinacionX = Math.pow(Double.parseDouble(cachos[0])*2, 2)*sentidoX;
                inclinacionY = Math.pow(Double.parseDouble(cachos[1])*2, 2)*sentidoY;

                newX = x - inclinacionX.intValue();
                newY = y - inclinacionY.intValue();

                while (newX != x || newY != y) {
                    if (newX > x) {
                        x++;
                    } else if (newX < x) {
                        x--;
                    }
                    if (newY > y) {
                        y++;
                    } else if (newY < y) {
                        y--;
                    }
                    robot.mouseMove(x, y);
                    Thread.sleep(1);
                }

                out.println("ok," + inputLine);
                if (inputLine.equals("Bye.")) {
                    break;
                }
            }
            out.close();
            in.close();
            serverSocket.close();
        } catch (Exception e) {
        }
    }

}
