import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class PressStartGame {
  private BufferedImage buffer;
  private Graphics graphicsBuffer;

  public PressStartGame(BufferedImage buffer) {
    this.buffer = buffer;
    this.graphicsBuffer = buffer.createGraphics();
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

  public void drawStartGame() {
    drawSLetter();
    drawNLetter();
    drawALetter();
    drawKLetter();
    drawELetter();
  }

  public void drawSLetter() {
    int[][] rectCoordinates = {
        { 50, 50, 90, 90 },
        { 90, 50, 130, 90 },
        { 130, 50, 170, 90 },
        { 50, 90, 90, 130 },
        { 50, 130, 90, 170 },
        { 90, 130, 130, 170 },
        { 130, 130, 170, 170 },
        { 130, 170, 170, 210 },
        { 130, 210, 170, 250 },
        { 90, 210, 130, 250 },
        { 50, 210, 90, 250 }
    };

    for (int[] rect : rectCoordinates) {
      int x1 = rect[0];
      int y1 = rect[1];
      int x2 = rect[2];
      int y2 = rect[3];

      Point[] vertices = {
          new Point(x1, y1),
          new Point(x2, y1),
          new Point(x2, y2),
          new Point(x1, y2)
      };

      fill(vertices, Color.GREEN);

      drawRect(x1, y1, x2, y2, Color.GREEN);
    }
  }

  public void drawNLetter() {
    int[][] nRectCoordinates = {
        { 200, 50, 240, 250 },
        { 240, 50, 280, 100 },
        { 280, 100, 320, 250 }
    };

    for (int[] rect : nRectCoordinates) {
      int x1 = rect[0];
      int y1 = rect[1];
      int x2 = rect[2];
      int y2 = rect[3];

      Point[] vertices = {
          new Point(x1, y1),
          new Point(x2, y1),
          new Point(x2, y2),
          new Point(x1, y2)
      };

      fill(vertices, Color.GREEN);

      drawRect(x1, y1, x2, y2, Color.GREEN);
    }
  }

  public void drawALetter() {
    int[][] aRectCoordinates = {
        { 350, 50, 390, 250 },
        { 350, 50, 470, 90 },
        { 350, 130, 470, 170 },
        { 430, 50, 470, 250 }
    };

    for (int[] rect : aRectCoordinates) {
      int x1 = rect[0];
      int y1 = rect[1];
      int x2 = rect[2];
      int y2 = rect[3];

      Point[] vertices = {
          new Point(x1, y1),
          new Point(x2, y1),
          new Point(x2, y2),
          new Point(x1, y2)
      };

      fill(vertices, Color.GREEN);

      drawRect(x1, y1, x2, y2, Color.GREEN);
    }
  }

  public void drawKLetter() {
    int[][] aRectCoordinates = {
        { 500, 50, 540, 250 },
        { 500, 130, 580, 170 },
        { 580, 130, 620, 50 },
        { 580, 170, 620, 250 }
    };

    for (int[] rect : aRectCoordinates) {
      int x1 = rect[0];
      int y1 = rect[1];
      int x2 = rect[2];
      int y2 = rect[3];

      Point[] vertices = {
          new Point(x1, y1),
          new Point(x2, y1),
          new Point(x2, y2),
          new Point(x1, y2)
      };

      fill(vertices, Color.GREEN);

      drawRect(x1, y1, x2, y2, Color.GREEN);
    }
  }

  public void drawELetter() {
    int[][] aRectCoordinates = {
        { 650, 50, 690, 250 },
        { 650, 50, 760, 90 },
        { 650, 130, 740, 170 },
        { 650, 210, 760, 250 }
    };

    for (int[] rect : aRectCoordinates) {
      int x1 = rect[0];
      int y1 = rect[1];
      int x2 = rect[2];
      int y2 = rect[3];

      Point[] vertices = {
          new Point(x1, y1),
          new Point(x2, y1),
          new Point(x2, y2),
          new Point(x1, y2)
      };

      fill(vertices, Color.GREEN);

      drawRect(x1, y1, x2, y2, Color.GREEN);
    }
  }

  public void showGameStart() {
    if (buffer != null) {
      graphicsBuffer.setColor(Color.RED);
      drawStartGame();
    }
  }
}
