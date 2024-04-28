import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class SnakeBody {
  private int maxSizeGrid;
  private int quantCell;
  private int sizeCell;
  private BufferedImage buffer;
  private Graphics graphicsBuffer;

  Color snakeColor = Color.GREEN;
  Color foodColor = Color.RED;
  List<int[]> snake = new ArrayList<int[]>();
  int[] food = new int[2];
  String direction = "R";
  String nextDirection = "R";

  public SnakeBody(int maxSizeGrid, int quantCell, BufferedImage buffer) {
    this.maxSizeGrid = maxSizeGrid;
    this.quantCell = quantCell;
    this.sizeCell = maxSizeGrid / quantCell;
    this.buffer = buffer;
    this.graphicsBuffer = buffer.createGraphics();

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

  public void showSnake(Color color, int panelWidth, int panelHeight) {
    int gridWidth = quantCell * sizeCell;
    int gridHeight = quantCell * sizeCell;

    int emptySpaceX = (panelWidth - gridWidth) / 2;
    int emptySpaceY = (panelHeight - gridHeight) / 2;

    for (int[] point : snake) {
      int x = emptySpaceX + (point[0] * sizeCell);
      int y = emptySpaceY + (point[1] * sizeCell);
      int x2 = x + sizeCell;
      int y2 = y + sizeCell;
      Point[] vertices = {
          new Point(x, y),
          new Point(x, y2),
          new Point(x2, y2),
          new Point(x2, y)
      };
      scanLine(vertices, snakeColor);
      drawRect(x, y, x2, y2, color);
    }
  }

  public void showFood(Color color, int panelWidth, int panelHeight) {
    int gridWidth = quantCell * sizeCell;
    int gridHeight = quantCell * sizeCell;

    int emptySpaceX = (panelWidth - gridWidth) / 2;
    int emptySpaceY = (panelHeight - gridHeight) / 2;

    int squareCenterX = emptySpaceX + (food[0] * sizeCell) + sizeCell / 2;
    int squareCenterY = emptySpaceY + (food[1] * sizeCell) + sizeCell / 2;
    int radius = sizeCell / 3;

    int cx = 0;
    int cy = radius;
    int d = 3 - 2 * radius;

    while (cx <= cy) {
      for (int i = squareCenterX - cy; i <= squareCenterX + cy; i++) {
        putPixel(i, squareCenterY + cx, color);
        putPixel(i, squareCenterY - cx, color);
      }

      for (int i = squareCenterY - cy; i <= squareCenterY + cy; i++) {
        putPixel(squareCenterX + cx, i, color);
        putPixel(squareCenterX - cx, i, color);
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

  private void scanLine(Point[] vertices, Color color) {
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
      graphicsBuffer.setColor(Color.WHITE);
      showSnake(Color.WHITE, maxSizeGrid, maxSizeGrid);
    }
  }

  public void showFood() {
    if (buffer != null) {
      graphicsBuffer.setColor(Color.RED);
      showFood(Color.RED, maxSizeGrid, maxSizeGrid);
    }
  }

  public void advance() {
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
      System.out.println("You have lost!");
    } else {
      if (newHead[0] == food[0] && newHead[1] == food[1]) {
        snake.add(newHead);
        generateFood();
      } else {
        snake.add(newHead);
        snake.remove(0);
      }
    }
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
