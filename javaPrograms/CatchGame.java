import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class CatchGame extends Frame implements Runnable, KeyListener {
    private int basketX = 350, basketY = 550, basketWidth = 100, basketHeight = 20;
    private int objectX, objectY = 0, objectDiameter = 20;
    private int width = 800, height = 600;
    private int score = 0, lives = 3;
    private boolean gameOver = false;
    private Random random = new Random();

    public CatchGame() {
        setSize(width, height);
        setTitle("Catch the Falling Object Game");
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(this);
        Thread t = new Thread(this);
        t.start();
    }

    @Override
    public void paint(Graphics g) {
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        if (!gameOver) {
            // Draw basket
            g.setColor(Color.BLUE);
            g.fillRect(basketX, basketY, basketWidth, basketHeight);

            // Draw falling object
            g.setColor(Color.RED);
            g.fillOval(objectX, objectY, objectDiameter, objectDiameter);

            // Display score and lives
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, 20, 50);
            g.drawString("Lives: " + lives, 700, 50);
        } else {
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", width / 2 - 100, height / 2 - 50);
            g.drawString("Final Score: " + score, width / 2 - 100, height / 2);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!gameOver) {
                objectY += 5;

                // Check if the object hits the ground
                if (objectY >= height) {
                    objectY = 0;
                    objectX = random.nextInt(width - objectDiameter);
                    lives--;
                    if (lives == 0) {
                        gameOver = true;
                    }
                }

                // Check if the object is caught by the basket
                if (objectY + objectDiameter >= basketY &&
                    objectX + objectDiameter >= basketX &&
                    objectX <= basketX + basketWidth) {
                    score += 10;
                    objectY = 0;
                    objectX = random.nextInt(width - objectDiameter);
                }

                repaint();

                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break;
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && basketX > 0) {
            basketX -= 20;
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && basketX + basketWidth < width) {
            basketX += 20;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new CatchGame();
    }
}
