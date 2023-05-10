package snake;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;

public class SnakeGame extends JPanel implements KeyListener, Runnable {
    int x,y,size,speed,score, foodX, foodY;
    int[] snakeX = new int[50];
    int[] snakeY = new int[50];
    String direction = "right";
    Thread thread;

    public void init() {
        x = 150;
        y = 150;
        size = 5;
        speed = 100;
        score = 0;
        foodX = 100;
        foodY = 100;
        setPreferredSize(new Dimension(500, 500));
        addKeyListener(this);
        setFocusable(true);
        for(int i = 0; i < size; i++) {
            snakeX[i] = x-i*10;
            snakeY[i] = y;
        }
        thread = new Thread(this);
        thread.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Set the background color to light blue
        setBackground(new Color(173, 216, 230));

        // Draw the game objects
        g.fillRect(x, y, 10, 10);
        for(int i = 0; i < size; i++) {
            g.fillRect(snakeX[i], snakeY[i], 10, 10);
        }

        // Set a larger font size for the score text
        Font font = new Font("Arial", Font.BOLD, 20);
        g.setFont(font);

        g.drawString("Angel's Snake Game - Score: "+score, 10, 25);
        g.fillRect(foodX, foodY, 10, 10);
    }

    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_LEFT) {
            direction = "left";
        }
        if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
            direction = "right";
        }
        if(e.getKeyCode() == KeyEvent.VK_UP) {
            direction = "up";
        }
        if(e.getKeyCode() == KeyEvent.VK_DOWN) {
            direction = "down";
        }
    }

    public void keyReleased(KeyEvent e) {}

    public void keyTyped(KeyEvent e) {}

    public void run() {
        boolean running = true;
        while (running) {
            if(direction.equals("right")) {
                x = x+10;
            }
            if(direction.equals("left")) {
                x = x-10;
            }
            if(direction.equals("up")) {
                y = y-10;
            }
            if(direction.equals("down")) {
                y = y+10;
            }

            //Check for collision with food
            if(x == foodX && y == foodY) {
                score++;
                size++;
                generateFood();
            }

            // Check for collision with itself
            for(int i = 1; i < size; i++) {
                if(x == snakeX[i] && y == snakeY[i]) {
                    running = false;
                    gameOver();
                }
            }

            //Check for collision with wall
            if(x < 0 || x > 490 || y < 0 || y > 490) {
                running = false;
                gameOver();
            }
            for(int i = size; i > 0; i--) {
                snakeX[i] = snakeX[i-1];
                snakeY[i] = snakeY[i-1];
            }
            snakeX[0] = x;
            snakeY[0] = y;

            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            repaint();
        }
    }

    private void generateFood() {
        Random r = new Random();
        foodX = r.nextInt(50) * 10;
        foodY = r.nextInt(50) * 10;
    }

    private void gameOver() {
        // Stop the thread
        thread.interrupt();

        // Display "Game Over" message in red color
        Graphics g = getGraphics();
        g.setColor(Color.RED);
        g.setFont(new Font("Arial", Font.BOLD, 30));
        g.drawString("Game Over", 200, 250);

        // Wait for a few seconds before exiting the program
        try {
            Thread.sleep(3000); // wait for 3 seconds
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }
}
