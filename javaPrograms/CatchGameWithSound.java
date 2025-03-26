import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CatchGameWithSound extends Frame implements Runnable, KeyListener {
    private int basketX = 350, basketY = 550, basketWidth = 100, basketHeight = 20;
    private int objectX, objectY = 0, objectDiameter = 20;
    private int width = 800, height = 600;
    private int score = 0, lives = 3;
    private boolean gameOver = false;
    private Random random = new Random();
    private Clip backgroundMusic, catchSound, missSound;

    public CatchGameWithSound() {
        setSize(width, height);
        setTitle("Catch the Falling Object with Sound");
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(this);
        loadSounds();
        playBackgroundMusic();
        Thread t = new Thread(this);
        t.start();
    }

    private void loadSounds() {
        try {
            backgroundMusic = loadSound("background_music.wav");
            catchSound = loadSound("catch_sound.wav");
            missSound = loadSound("miss_sound.wav");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Clip loadSound(String fileName) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        File file = new File(fileName);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(file);
        Clip clip = AudioSystem.getClip();
        clip.open(audioStream);
        return clip;
    }

    private void playBackgroundMusic() {
        if (backgroundMusic != null) {
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        }
    }

    private void playSound(Clip sound) {
        if (sound != null) {
            sound.setFramePosition(0);  // Reset sound to start
            sound.start();
        }
    }

    @Override
    public void paint(Graphics g) {
        // Create gradient background
        Graphics2D g2d = (Graphics2D) g;
        GradientPaint gradient = new GradientPaint(0, 0, Color.CYAN, width, height, Color.MAGENTA);
        g2d.setPaint(gradient);
        g2d.fillRect(0, 0, width, height);

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
                    playSound(missSound);
                    if (lives == 0) {
                        gameOver = true;
                        if (backgroundMusic != null) backgroundMusic.stop();
                    }
                }

                // Check if the object is caught by the basket
                if (objectY + objectDiameter >= basketY &&
                    objectX + objectDiameter >= basketX &&
                    objectX <= basketX + basketWidth) {
                    score += 10;
                    objectY = 0;
                    objectX = random.nextInt(width - objectDiameter);
                    playSound(catchSound);
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
        new CatchGameWithSound();
    }
}
