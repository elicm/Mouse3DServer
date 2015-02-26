/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mousepointer;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eislas
 */
public class Raton implements Runnable {

    public int newX, newY, x, y, steepX = 1, steepY = 1;
    public boolean alive = true;

    @Override
    public void run() {

        try {
            Robot robot = new Robot();
            while (alive) {
                Point location = MouseInfo.getPointerInfo().getLocation();
                x = location.x;
                y = location.y;

                steepX = (int) Math.ceil(Math.abs(newX - x) / 10);
                steepY = (int) Math.ceil(Math.abs(newY - y) / 10);

                while (newX != x || newY != y) {
                    System.out.print("\t" + x + ", " + y);
                    if (steepX > 1) {
                        steepX = (int) Math.ceil(steepX / 2);
                    } else if (steepX < 1) {
                        steepX = 1;
                    }
                    if (newX > x) {
                        x += steepX;
                    } else if (newX < x) {
                        x -= steepX;
                    }

                    if (steepY > 1) {
                        steepY = (int) Math.ceil(steepY / 2);
                    } else if (steepY < 1) {
                        steepY = 1;
                    }
                    if (newY > y) {
                        y += steepY;
                    } else if (newY < y) {
                        y -= steepY;
                    }
                    robot.mouseMove(x, y);
                    Thread.sleep(1);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
