import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class CatchGameWithImageAndSound extends Frame implements Runnable, KeyListener {
    private int basketX = 350, basketY = 550, basketWidth = 100, basketHeight = 20;
    private int objectX, objectY = 0, objectDiameter = 20;
    private int width = 800, height = 600;
    private int score = 0, lives = 3;
    private boolean gameOver = false;
    private Random random = new Random();
    private Image backgroundImage, offScreenImage;
    private Graphics offScreenGraphics;
    private Clip backgroundMusic, catchSound, missSound;

    public CatchGameWithImageAndSound() {
        setSize(width, height);
        setTitle("Catch the Falling Object with Enhanced Graphics and Sound");
        setVisible(true);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        addKeyListener(this);
        loadResources();
        playBackgroundMusic();
        Thread t = new Thread(this);
        t.start();
    }

    private void loadResources() {
        // Load background image
        backgroundImage = Toolkit.getDefaultToolkit().getImage("background.jpg");

        // Load sounds
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
            new Thread(() -> backgroundMusic.start()).start();
        }
    }

    private void playSound(Clip sound) {
        if (sound != null) {
            sound.setFramePosition(0);  // Reset sound to start
            sound.start();
        }
    }

    @Override
    public void update(Graphics g) {
        if (offScreenImage == null) {
            offScreenImage = createImage(width, height);
            offScreenGraphics = offScreenImage.getGraphics();
        }

        offScreenGraphics.clearRect(0, 0, width, height);
        paint(offScreenGraphics);
        g.drawImage(offScreenImage, 0, 0, this);
    }

    @Override
    public void paint(Graphics g) {
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, width, height, this);
        }

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

                if (objectY >= height) {
                    objectY = 0;
                    objectX = random.nextInt(width - objectDiameter);
                    lives--;
                    playSound(missSound);
                    if (lives == 0) {
                        gameOver = true;
                        backgroundMusic.stop();
                    }
                }

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
        new CatchGameWithImageAndSound();
    }
}
