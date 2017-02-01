import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.GridLayout;
import javax.swing.JButton;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.Random;
import java.util.Stack;

public class Main {
    static int colorMap = 0;
    static final int xmax = 16;
    static final int ymax = 16;
    static final int pixelsize = 512 / Math.max(xmax, ymax);
    static int[][] colormap = new int[xmax][ymax];
    static Color[] colors = new Color[] {
        new Color(200, 0, 0),
        new Color(0, 200, 0),
        new Color(0, 100, 200),
        new Color(200, 240, 0)
    };
    static JPanel panel1;
    static int moves = 0;
    
    public static void main(String[] args) {
        Random rand = new Random();
        {
            final int len = colors.length;
            for(int x = 0; x < xmax; x++) {
                for(int y = 0; y < ymax; y++) {
                    colormap[y][x] = rand.nextInt(len);
                }
            }
        }
        JFrame frame = new JFrame("Flood Fill");
        panel1 = new JPanel() {
            public void paintComponent(Graphics g) {
                for(int x = 0; x < xmax; x++) {
                    for(int y = 0; y < ymax; y++) {
                        g.setColor(colors[colormap[y][x]]);
                        g.fillRect(x * pixelsize, y * pixelsize, x * pixelsize + pixelsize, y * pixelsize + pixelsize);
                    }
                }
            }
        };
        panel1.setPreferredSize(new Dimension(512, 512));
        JPanel panel2 = new JPanel();
        {
            final int len = colors.length;
            panel2.setLayout(new GridLayout(Math.max(len, 8), 1));
            panel2.setPreferredSize(new Dimension(64, 64 * len));
            for(int i = 0; i < len; i++) {
                JButton b = new JButton();
                final int color = i;
                b.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        moves++;
                        floodfill(0, 0, color);
                        if(check()) {
                            System.out.println("You win! It took you " + moves + " moves.");
                            System.exit(0);
                        }
                    }
                });
                b.setPreferredSize(new Dimension(64, 64));
                b.setBackground(colors[i]);
                panel2.add(b);
            }
        }
        frame.setResizable(false);
        frame.getContentPane().add("Center", panel1);
        frame.getContentPane().add("East", panel2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    static void floodfill(int x, int y, int color) {
        final int oldcolor = colormap[y][x];
        Stack<Integer> s = new Stack<Integer>();
        s.push(x);
        s.push(y);
        int a, b;
        while(!s.empty()) {
            a = s.pop();
            b = s.pop();
            colormap[a][b] = color;
            if(a < ymax - 1 && colormap[a + 1][b] == oldcolor) {
                s.push(b);
                s.push(a + 1);
            }
            if(a > 0 && colormap[a - 1][b] == oldcolor) {
                s.push(b);
                s.push(a - 1);
            }
            if(b < xmax - 1 && colormap[a][b + 1] == oldcolor) {
                s.push(b + 1);
                s.push(a);
            }
            if(b > 0 && colormap[a][b - 1] == oldcolor) {
                s.push(b - 1);
                s.push(a);
            }
        }
        panel1.repaint();
    }
    
    static boolean check() {
        final int color = colormap[0][0];
        for(int x = 0; x < xmax; x++) {
            for(int y = 0; y < ymax; y++) {
                if(colormap[y][x] != color) {
                    return false;
                }
            }
        }
        return true;
    }
}
