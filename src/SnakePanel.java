import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.*;

public class SnakePanel extends JPanel implements Runnable, KeyListener {
  private static final int WINDOW_WIDTH = 820;
  private static final int WINDOW_HEIGHT = 820;
  static final Dimension WINDOW_SIZE = new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT);

  Graphics graphicsBuffer;
  Image image;
  Thread thread;
  public BufferedImage buffer;
  int maxSizeGrid = WINDOW_WIDTH;
  int quantCell = 32;
  private boolean startPressed = false;
  private boolean showStartGame = true;

  private SnakeGrid snakeGrid;
  private SnakeBody snakeBody;
  private PressStartGame gameStart;

  public SnakePanel() {
    if (buffer == null) {
      buffer = new BufferedImage(WINDOW_WIDTH, WINDOW_HEIGHT, BufferedImage.TYPE_INT_ARGB);
    }

    makeStartGame();
    makeGrid();
    makeSnake();

    setPreferredSize(WINDOW_SIZE);

    thread = new Thread(this);
    thread.start();

    Thread updateStartGameThread = new Thread(() -> {
      try {
        while (!startPressed) {
          showStartGame = !showStartGame;
          SwingUtilities.invokeLater(this::repaint);
          Thread.sleep(300);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    });

    updateStartGameThread.start();

    setFocusable(true);
    addKeyListener(this);
  }

  public void makeStartGame() {
    gameStart = new PressStartGame(buffer);
  }

  public void makeGrid() {
    snakeGrid = new SnakeGrid(maxSizeGrid, quantCell, buffer);
  }

  public void makeSnake() {
    snakeBody = new SnakeBody(maxSizeGrid, quantCell, buffer);
  }

  @Override
  public void paintComponent(Graphics g) {
    image = createImage(getWidth(), getHeight());
    graphicsBuffer = image.getGraphics();
    if (!startPressed) {
      if (showStartGame) {
        gameStart.showGameStart();
      } else {
        gameStart.showWhiteBackground();
      }
    } else {
      showComponents();
    }
    g.drawImage(buffer, 0, 0, this);
  }

  public void showComponents() {
    snakeGrid.showGrid();
    snakeBody.showSnake();
    snakeBody.drawScore();
    snakeBody.showFood();
  }

  @Override
  public void run() {
    while (true) {
      if (startPressed) {
        snakeBody.advance();
        repaint();
      }
      try {
        Thread.sleep(100);
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {

  }

  @Override
  public void keyPressed(KeyEvent e) {
    int keyCode = e.getKeyCode();

    if (!startPressed && keyCode == KeyEvent.VK_ENTER) {
      startPressed = true;
    } else if (startPressed) {
      switch (keyCode) {
        case KeyEvent.VK_UP:
          snakeBody.setDirection("U");
          break;
        case KeyEvent.VK_DOWN:
          snakeBody.setDirection("D");
          break;
        case KeyEvent.VK_LEFT:
          snakeBody.setDirection("L");
          break;
        case KeyEvent.VK_RIGHT:
          snakeBody.setDirection("R");
          break;
        default:
          System.out.println("Key not accepted: " + keyCode);
          break;
      }
    }
  }

  @Override
  public void keyReleased(KeyEvent e) {
  }
}
