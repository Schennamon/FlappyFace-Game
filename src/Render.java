import javax.swing.JPanel;
import java.awt.Graphics;

class Render extends JPanel {

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        FlappyBird.flappyBird.repaint(g);
    }
}
