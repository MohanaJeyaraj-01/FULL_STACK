import java.awt.*;
import java.awt.event.*;

public class BouncingBallAWT extends Frame implements Runnable, KeyListener {
    private int ballX = 400, ballY = 300; // Ball position
    private int ballDiameter = 100;
    private int xDirection = 5, yDirection = 5; // Ball direction
    private int width = 800, height = 600; // Window size
    private int paddleX = 350, paddleY = 550, paddleWidth = 100, paddleHeight = 10; // Paddle details
    private boolean gameOver = false;

    public BouncingBallAWT() {
        setSize(width, height);
        setTitle("Interactive Bouncing Ball Game");
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
        // Clear the background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        if (!gameOver) {
            // Draw the ball
            g.setColor(Color.RED);
            g.fillOval(ballX, ballY, ballDiameter, ballDiameter);

            // Draw the paddle
            g.setColor(Color.BLUE);
            g.fillRect(paddleX, paddleY, paddleWidth, paddleHeight);
        } else {
            // Display Game Over message
            g.setColor(Color.BLACK);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", width / 2 - 100, height / 2);
        }
    }

    @Override
    public void run() {
        while (true) {
            if (!gameOver) {
                // Update ball position
                ballX += xDirection;
                ballY += yDirection;

                // Check for collisions with window edges
                if (ballX <= 0 || ballX + ballDiameter >= width) {
                    xDirection = -xDirection;
                }
                if (ballY <= 0) {
                    yDirection = -yDirection;
                }

                // Check for paddle collision
                if (ballY + ballDiameter >= paddleY &&
                    ballX + ballDiameter >= paddleX &&
                    ballX <= paddleX + paddleWidth) {
                    yDirection = -yDirection;
                    xDirection += (xDirection > 0) ? 1 : -1; // Increase speed slightly
                    yDirection += (yDirection > 0) ? 1 : -1;
                }

                // Check if the ball touches the bottom
                if (ballY + ballDiameter >= height) {
                    gameOver = true;
                }

                repaint();

                try {
                    Thread.sleep(20); // Control the speed of the game
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                break; // Exit the game loop on game over
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_LEFT && paddleX > 0) {
            paddleX -= 20; // Move paddle left
        }
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && paddleX + paddleWidth < width) {
            paddleX += 20; // Move paddle right
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        new BouncingBallAWT();
    }
}
