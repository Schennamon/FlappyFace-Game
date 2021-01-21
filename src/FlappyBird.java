import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

 public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public static FlappyBird flappyBird;

    private Render renderer = new Render();

    private Random rand = new Random();

    private final int WIDHT = 800, HEIGHT = 600;

    private Image bird, backGround, footer;

    private int ticks, yMotion, score;

    private int birdX = WIDHT / 2 - 10, birdY = HEIGHT / 2 - 10, birdWidth = 40, birdHeight = 40, space = 300;

    private int backX = 0, backY = -100, footX = 0, footY = HEIGHT - 120;

    private boolean gameOver, started;

    private ArrayList<Rectangle> columns = new ArrayList<Rectangle>();


    public FlappyBird(){
        JFrame jFrame = new JFrame();
        Timer timer = new Timer(20, this);
        ImageIcon brd = new ImageIcon("brdIconV4.png");
        ImageIcon bcg = new ImageIcon("back.png");
        ImageIcon ftr = new ImageIcon("footer.png");

        jFrame.add(renderer);
        jFrame.addMouseListener(this);
        jFrame.addKeyListener(this);
        jFrame.setTitle("FlappyFace");
        jFrame.setSize(WIDHT, HEIGHT);
        jFrame.setResizable(false);
        jFrame.setVisible(true);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        backGround = bcg.getImage();
        bird = brd.getImage();
        footer = ftr.getImage();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        timer.start();
    }

    public void addColumn(boolean start){
        int widht = 100;
        int height = 100+ rand.nextInt(150);

        if(start) {
            columns.add(new Rectangle(WIDHT + widht + columns.size() * 300, HEIGHT - height -120, widht, height));
            columns.add(new Rectangle(WIDHT + widht + (columns.size() - 1) * 300, 0, widht, HEIGHT - height - space));
        }
        else{
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, widht, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, widht, HEIGHT - height - space));
        }
    }

    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.green.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump(){
        if(gameOver){
            birdX = WIDHT / 2 -10;
            birdY = HEIGHT / 2 -10;
            columns.clear();
            yMotion = 0;
            score = 0;

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }

        if(!started){
            started = true;
        }
        else if(!gameOver){
            if(yMotion > 0){
                yMotion = 0;
            }

            yMotion -= 10;
        }
    }

    public void move(){
        ticks++;
        int speed = 10;

        for (int i = 0; i < columns.size(); i++) {
            Rectangle column = columns.get(i);
            column.x -= speed;
            backX -= 1;
            if(backX == -800){
                backX = 0;
            }
            footX -= 1;
            if(footX == -800){
                footX = 0;
            }
        }

        if (ticks % 2 == 0 && yMotion < 15) {
            yMotion += 2;
        }

        for (int i = 0; i < columns.size(); i++) {
            Rectangle column = columns.get(i);
            if (column.x + column.width < 0) {
                columns.remove(column);
                if (column.y == 0) {
                    addColumn(false);
                }
            }
        }

        birdY += yMotion;

    }

    public void scoreCount_collision(){
        for (Rectangle column : columns) {
            if (column.y == 0 && birdX + birdWidth / 2 > column.x + column.width / 2 - 5 && birdX + birdWidth / 2 < column.x + column.width / 2 + 5){
                score++;
            }

            if (column.intersects(birdX, birdY, birdWidth, birdHeight)) {       // коллизия
                gameOver = true;
                birdX = column.x - birdWidth;
            }
        }

        if (birdY > HEIGHT - 120 || birdY < 0) {
            gameOver = true;
        }
        if(birdY + yMotion >= HEIGHT - 120){
            birdY = HEIGHT - 120 - birdHeight;
        }
    }
     public void repaint(Graphics g) {

         g.drawImage(backGround, backX, backY, null);
         g.drawImage(backGround, backX + 800, backY, null);

         g.drawImage(footer, footX, footY, null);
         g.drawImage(footer, footX + 800, footY, null);

         g.drawImage(bird, birdX, birdY, null);

         for(Rectangle column : columns){
             paintColumn(g, column);
         }

         g.setColor(Color.white);
         g.setFont(new Font("Arial", 1, 90));

         if(!started){
             g.drawString("Click to start!", 50, HEIGHT / 2 - 50);
         }

         g.setColor(Color.white);
         g.setFont(new Font("Arial", 1, 100));

         if (gameOver) {
             g.drawString("GameOver!", 100, HEIGHT / 2 -50);
         }

         if(!gameOver && started){
             g.drawString(String.valueOf(score), WIDHT / 2 - 25, 100);
         }
     }

    public static void main(String[] args){

        flappyBird = new FlappyBird();

    }

     @Override
     public void actionPerformed(ActionEvent e) {

         if(started) {

             move();
             scoreCount_collision();
         }
         renderer.repaint();
     }

    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_SPACE){
            jump();
        }
    }
}
