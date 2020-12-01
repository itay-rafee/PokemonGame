package gameClient.util;

import java.awt.Color;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Polygon;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Taken from: https://www.tutorialspoint.com/javaexamples/gui_polygon.htm
 */
public class check extends JPanel {
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Polygon p = new Polygon();
        for (int i = 0; i < 5; i++) p.addPoint((int) (
                100 + 50 * Math.cos(i * 2 * Math.PI / 5)),(int) (
                100 + 50 * Math.sin(i * 2 * Math.PI / 5)));

        g.drawPolygon(p);
        g.setColor(Color.BLACK);
        int w = getWidth();
        int h = getHeight();
        g.drawOval(w/3,h/3,w/4,h/4);//here we do the prank
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().setBackground(Color.YELLOW);
        frame.setTitle("DrawPoly");
        frame.setSize(850, 650);
        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        Container contentPane = frame.getContentPane();
        contentPane.add(new check());
        frame.show();
    }
}
