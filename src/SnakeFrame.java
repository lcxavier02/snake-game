import java.awt.Color;

import javax.swing.*;

public class SnakeFrame extends JFrame {
  SnakePanel snakePanel;

  public SnakeFrame() {
    snakePanel = new SnakePanel();
    add(snakePanel);
    setTitle("Snake Game");
    setResizable(false);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBackground(Color.WHITE);

    pack();
    setLocationRelativeTo(null);
    setVisible(true);
  }
}
