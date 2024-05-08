import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import java.util.Collections;

public class SnakeBody {
  private BufferedImage buffer;
  private Graphics graphicsBuffer;
  private int maxSizeGrid;
  private int quantCell;
  private int sizeCell;
  private int score = 0;
  private boolean gameOver;

  Color snakeColor = Color.GREEN;
  Color foodColor = Color.RED;
  List<int[]> snake = new ArrayList<int[]>();
  String direction = "R";
  String nextDirection = "R";
  int[] food = new int[2];

  public SnakeBody(int maxSizeGrid, int quantCell, BufferedImage buffer) {
    this.maxSizeGrid = maxSizeGrid;
    this.quantCell = quantCell;
    this.sizeCell = maxSizeGrid / quantCell;
    this.buffer = buffer;
    this.graphicsBuffer = buffer.createGraphics();
    this.gameOver = false;

    int[] a = { quantCell / 2 - 1, quantCell / 2 - 1 };
    int[] b = { quantCell / 2, quantCell / 2 - 1 };

    snake.add(a);
    snake.add(b);

    generateFood();
  }

  public void putPixel(int x, int y, Color c) {
    if (x >= 0 && x < buffer.getWidth() && y >= 0 && y < buffer.getHeight()) {
      buffer.setRGB(x, y, c.getRGB());
    }
  }

  public void drawLine(int x1, int y1, int x2, int y2, Color color) {
    int dx = Math.abs(x2 - x1);
    int dy = Math.abs(y2 - y1);

    int xi = (x2 > x1) ? 1 : -1;
    int yi = (y2 > y1) ? 1 : -1;

    int A = 2 * dy;
    int B = 2 * dy - 2 * dx;
    int C = 2 * dx;
    int D = 2 * dx - 2 * dy;

    int x = x1;
    int y = y1;

    int pk;

    putPixel(x, y, color);

    if (dx > dy) {
      pk = 2 * dy - dx;
      while (x != x2) {
        if (pk > 0) {
          x = x + xi;
          y = y + yi;
          pk = pk + B;
        } else {
          x = x + xi;
          pk = pk + A;
        }
        putPixel(x, y, color);
      }
    } else {
      pk = 2 * dx - dy;
      while (y != y2) {
        if (pk > 0) {
          x = x + xi;
          y = y + yi;
          pk = pk + D;
        } else {
          y = y + yi;
          pk = pk + C;
        }
        putPixel(x, y, color);
      }
    }
  }

  public void drawRect(int x1, int y1, int x2, int y2, Color color) {
    drawLine(x1, y1, x2, y1, color);
    drawLine(x2, y1, x2, y2, color);
    drawLine(x2, y2, x1, y2, color);
    drawLine(x1, y2, x1, y1, color);
  }

  private void fillCircle(int x, int y, int sizeCell, Color color) {
    int centerX = x + sizeCell / 2;
    int centerY = y + sizeCell / 2;

    int radius = sizeCell;

    int cx = 0;
    int cy = radius;
    int d = 3 - 2 * radius;

    while (cx <= cy) {
      for (int i = centerX - cy; i <= centerX + cy; i++) {
        putPixel(i, centerY + cx, color);
        putPixel(i, centerY - cx, color);
      }

      for (int i = centerY - cy; i <= centerY + cy; i++) {
        putPixel(centerX + cx, i, color);
        putPixel(centerX - cx, i, color);
      }

      if (d < 0) {
        d = d + 4 * cx + 6;
      } else {
        d = d + 4 * (cx - cy) + 10;
        cy--;
      }
      cx++;
    }
  }

  public void drawSnake(Color color, int panelWidth, int panelHeight) {
    int gridWidth = quantCell * sizeCell;
    int gridHeight = quantCell * sizeCell;

    int emptySpaceX = (panelWidth - gridWidth) / 2;
    int emptySpaceY = (panelHeight - gridHeight) / 2;

    for (int i = 0; i < snake.size(); i++) {
      int[] point = snake.get(i);
      int x = emptySpaceX + (point[0] * sizeCell);
      int y = emptySpaceY + (point[1] * sizeCell);
      int x2 = x + sizeCell;
      int y2 = y + sizeCell;

      boolean isHead = (i == snake.size() - 1);

      if (isHead) {
        drawSnakeHead(x, y, sizeCell, snakeColor);
        drawEyes(x, y, sizeCell, Color.WHITE);
      } else {
        drawSnakeBody(x, y, sizeCell, i);
      }

      drawRect(x, y, x2, y2, color);
    }
  }

  private void drawSnakeHead(int x, int y, int sizeCell, Color color) {
    int headWidth = sizeCell / 2;
    int headHeight = sizeCell / 2;

    int headX = x + (sizeCell - headWidth) / 2;
    int headY = y + (sizeCell - headHeight) / 2;
    int backHeadX = x;
    int backHeadY = y;

    switch (direction) {
      case "U":
        headY = y;
        backHeadY = y + headHeight;
        break;
      case "D":
        headY = y + sizeCell - headHeight;
        backHeadY = y;
        break;
      case "L":
        headX = x;
        backHeadX = x + headWidth;
        break;
      case "R":
        headX = x + sizeCell - headWidth;
        backHeadX = x;
        break;
      default:
        System.out.println("Dirección no válida");
        break;
    }

    if (direction.equals("U") || direction.equals("D")) {
      fillRect(backHeadX, backHeadY, headWidth * 2, headHeight, color);
    } else {
      fillRect(backHeadX, backHeadY, headWidth, headHeight * 2, color);
    }
    fillRect(headX, headY, headWidth, headHeight, color);
  }

  private void drawSnakeBody(int x, int y, int sizeCell, int segmentIndex) {
    Color bodyColor = snakeColor;
    Color darkGreen = new Color(0, 100, 0);

    fillRect(x, y, sizeCell, sizeCell, bodyColor);

    int scaleSize = sizeCell / 5;
    int numScales = sizeCell / scaleSize;

    for (int i = 0; i < numScales; i++) {
      for (int j = 0; j < numScales; j++) {
        if ((i + j) % 2 == 0) {
          int scaleX = x + i * scaleSize + (sizeCell - numScales * scaleSize) / 2;
          int scaleY = y + j * scaleSize + (sizeCell - numScales * scaleSize) / 2;

          fillRect(scaleX, scaleY, scaleSize, scaleSize, darkGreen);
        }
      }
    }
  }

  private void fillRect(int x, int y, int width, int height, Color color) {
    for (int i = x; i < x + width; i++) {
      for (int j = y; j < y + height; j++) {
        putPixel(i, j, color);
      }
    }
  }

  private void drawEyes(int x, int y, int sizeCell, Color color) {
    int eyeSize = sizeCell / 5;
    int eyeOffsetX = sizeCell / 4;
    int eyeOffsetY = sizeCell / 4;

    int eye1X = x + eyeOffsetX;
    int eye1Y = y + eyeOffsetY;
    int eye2X = x + sizeCell - eyeOffsetX - eyeSize;
    int eye2Y = y + eyeOffsetY;

    fillCircle(eye1X, eye1Y, eyeSize, Color.WHITE);
    fillCircle(eye2X, eye2Y, eyeSize, Color.WHITE);

    int irisSize = eyeSize / 2;

    int iris1X = eye1X + irisSize / 2;
    int iris1Y = eye1Y + irisSize / 2;
    int iris2X = eye2X + irisSize / 2;
    int iris2Y = eye2Y + irisSize / 2;

    fillCircle(iris1X, iris1Y, irisSize, Color.BLACK);
    fillCircle(iris2X, iris2Y, irisSize, Color.BLACK);
  }

  public void drawFood(Color color, int panelWidth, int panelHeight) {
    int gridWidth = quantCell * sizeCell;
    int gridHeight = quantCell * sizeCell;

    int emptySpaceX = (panelWidth - gridWidth) / 2;
    int emptySpaceY = (panelHeight - gridHeight) / 2;

    int centerX = emptySpaceX + (food[0] * sizeCell) + sizeCell / 2;
    int centerY = emptySpaceY + (food[1] * sizeCell) + sizeCell / 2;

    int rindRadius = sizeCell / 3;
    fillCircle(centerX, centerY, rindRadius, new Color(0, 128, 0));

    int fleshRadius = rindRadius * 2 / 3;
    fillCircle(centerX, centerY, fleshRadius, Color.RED);

    int numSeeds = 15;
    double seedAngle = 2 * Math.PI / numSeeds;
    int seedRadius = fleshRadius / 5;
    int seedOffsetX = fleshRadius / 4;
    int seedOffsetY = fleshRadius / 4;

    for (int i = 0; i < numSeeds; i++) {
      double seedX = centerX + Math.cos(i * seedAngle) * (fleshRadius - seedRadius + seedOffsetX);
      double seedY = centerY + Math.sin(i * seedAngle) * (fleshRadius - seedRadius + seedOffsetY);

      fillCircle((int) seedX, (int) seedY, seedRadius, Color.BLACK);
    }
  }

  private void fill(Point[] vertices, Color color) {
    int minY = Integer.MAX_VALUE;
    int maxY = Integer.MIN_VALUE;
    for (Point vertex : vertices) {
      if (vertex.y < minY) {
        minY = vertex.y;
      }
      if (vertex.y > maxY) {
        maxY = vertex.y;
      }
    }

    for (int y = minY + 1; y < maxY; y++) {
      ArrayList<Integer> intersectionPoints = new ArrayList<>();
      for (int i = 0; i < vertices.length; i++) {
        Point p1 = vertices[i];
        Point p2 = vertices[(i + 1) % vertices.length];
        if ((p1.y <= y && p2.y > y) || (p2.y <= y && p1.y > y)) {
          double x = (double) (p1.x + (double) (y - p1.y) / (p2.y - p1.y) * (p2.x - p1.x));
          intersectionPoints.add((int) x);
        }
      }

      Collections.sort(intersectionPoints);
      for (int i = 0; i < intersectionPoints.size(); i += 2) {
        int x1 = intersectionPoints.get(i);
        int x2 = intersectionPoints.get(i + 1);
        for (int x = x1; x <= x2; x++) {
          putPixel(x, y, color);
        }
      }
    }
  }

  public void showSnake() {
    if (buffer != null) {
      drawSnake(snakeColor, maxSizeGrid, maxSizeGrid);
    }
  }

  public void showFood() {
    if (buffer != null) {
      drawFood(Color.RED, maxSizeGrid, maxSizeGrid);
    }
  }

  public void advance() {
    if (gameOver) {
      return; // Si el juego ya ha terminado, no hagas nada
    }
    equalDirection();

    int[] last = snake.get(snake.size() - 1);
    int addX = 0;
    int addY = 0;

    switch (direction) {
      case "R":
        addX = 1;
        break;
      case "L":
        addX = -1;
        break;
      case "U":
        addY = -1;
        break;
      case "D":
        addY = 1;
        break;
    }

    int[] newHead = { Math.floorMod(last[0] + addX, quantCell), Math.floorMod(last[1] + addY, quantCell) };

    boolean collideItself = false;

    for (int i = 0; i < snake.size(); i++) {
      if (newHead[0] == snake.get(i)[0] && newHead[1] == snake.get(i)[1]) {
        collideItself = true;
        break;
      }
    }

    if (collideItself) {
      JOptionPane.showMessageDialog(null, "You have lost!");
      gameOver = true;
    } else {
      if (newHead[0] == food[0] && newHead[1] == food[1]) {
        snake.add(newHead);
        generateFood();
        score++;
      } else {
        snake.add(newHead);
        snake.remove(0);
      }
    }
  }

  public void drawScore() {
    String scoreText = "Score: " + score;
    int fontSize = 16;
    Color textColor = Color.BLACK;
    Color backgroundColor = Color.WHITE;
    int x = 10;
    int y = 11;

    int rectX = x;
    int rectY = y - fontSize - 4;
    int rectWidth = 100;
    int rectHeight = fontSize + 5;

    Point[] rectVertices = {
        new Point(rectX, rectY),
        new Point(rectX + rectWidth, rectY),
        new Point(rectX + rectWidth, rectY + rectHeight),
        new Point(rectX, rectY + rectHeight)
    };
    fill(rectVertices, backgroundColor);

    drawText(scoreText, x, y, fontSize, textColor);
  }

  public void drawText(String text, int x, int y, int fontSize, Color color) {
    graphicsBuffer.setColor(color);
    graphicsBuffer.setFont(new Font("Arial", Font.BOLD, fontSize));
    graphicsBuffer.drawString(text, x, y);
  }

  public void generateFood() {
    boolean foodExists = false;
    int a = (int) (Math.random() * quantCell);
    int b = (int) (Math.random() * quantCell);
    for (int[] point : snake) {
      if (point[0] == a && point[1] == b) {
        foodExists = true;
        generateFood();
        break;
      }

      if (!foodExists) {
        this.food[0] = a;
        this.food[1] = b;
      }
    }
  }

  public void setDirection(String dir) {
    if ((this.direction.equals("R") || this.direction.equals("L")) && (dir.equals("U") || dir.equals("D"))) {
      this.nextDirection = dir;
    }
    if ((this.direction.equals("U") || this.direction.equals("D")) && (dir.equals("L") || dir.equals("R"))) {
      this.nextDirection = dir;
    }
  }

  public void equalDirection() {
    this.direction = this.nextDirection;
  }

}
