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

  public void drawCoordinates(int[][] vertex, Color c) {
    for (int[] rect : vertex) {
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

      fill(vertices, c);
      drawRect(x1, y1, x2, y2, c);
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

    drawPLetter();
    drawRLetter();
    drawEGameLetter();
    drawDoubleSLetter();

    drawEGameLetter();
    drawNGameLetter();
    drawTLetter();
    drawEGameLetter();
    drawRLetter();

    drawTLetter();
    drawOLetter();

    drawDoubleSLetter();
    drawTLetter();
    drawAGameLetter();
    drawRLetter();
    drawTLetter();
  }

  public void drawPLetter() {
    int[][] rectCoordinates = {
        { 50, 260, 70, 265 },
        { 50, 260, 55, 290 },
        { 50, 270, 70, 275 },
        { 70, 260, 65, 275 }
    };

    drawCoordinates(rectCoordinates, Color.BLACK);
  }

  public void drawRLetter() {
    int[][] rectCoordinates = {
        { 75, 260, 90, 265 },
        { 75, 260, 80, 290 },
        { 75, 270, 90, 275 },
        { 95, 265, 90, 270 },
        { 85, 275, 90, 280 },
        { 90, 280, 95, 290 },

        { 300, 260, 315, 265 },
        { 300, 260, 305, 290 },
        { 300, 270, 315, 275 },
        { 320, 265, 315, 270 },
        { 310, 275, 315, 280 },
        { 315, 280, 320, 290 },

        { 495, 260, 510, 265 },
        { 495, 260, 500, 290 },
        { 495, 270, 510, 275 },
        { 515, 265, 510, 270 },
        { 505, 275, 510, 280 },
        { 510, 280, 515, 290 }
    };
    drawCoordinates(rectCoordinates, Color.BLACK);
  }

  public void drawEGameLetter() {
    int[][] rectCoordinates = {
        { 100, 260, 105, 290 },
        { 100, 260, 120, 265 },
        { 100, 272, 115, 277 },
        { 100, 285, 120, 290 },

        { 200, 260, 205, 290 },
        { 200, 260, 220, 265 },
        { 200, 272, 215, 277 },
        { 200, 285, 220, 290 },

        { 275, 260, 280, 290 },
        { 275, 260, 295, 265 },
        { 275, 272, 290, 277 },
        { 275, 285, 295, 290 }
    };
    drawCoordinates(rectCoordinates, Color.BLACK);
  }

  public void drawDoubleSLetter() {
    int[][] rectCoordinates = {
        { 125, 260, 145, 265 },
        { 125, 260, 130, 275 },
        { 125, 272, 145, 277 },
        { 140, 277, 145, 290 },
        { 125, 285, 145, 290 },

        { 150, 260, 170, 265 },
        { 150, 260, 155, 275 },
        { 150, 272, 170, 277 },
        { 165, 277, 170, 290 },
        { 150, 285, 170, 290 },

        { 420, 260, 440, 265 },
        { 420, 260, 425, 275 },
        { 420, 272, 440, 277 },
        { 435, 277, 440, 290 },
        { 420, 285, 440, 290 }
    };
    drawCoordinates(rectCoordinates, Color.BLACK);
  }

  public void drawNGameLetter() {
    int[][] rectCoordinates = {
        { 225, 260, 230, 290 },
        { 230, 270, 235, 275 },
        { 235, 275, 240, 280 },
        { 240, 260, 245, 290 }
    };
    drawCoordinates(rectCoordinates, Color.BLACK);
  }

  public void drawTLetter() {
    int[][] rectCoordinates = {
        { 250, 260, 270, 265 },
        { 258, 260, 262, 290 },

        { 350, 260, 370, 265 },
        { 358, 260, 362, 290 },

        { 445, 260, 465, 265 },
        { 453, 260, 457, 290 },

        { 520, 260, 540, 265 },
        { 528, 260, 532, 290 }
    };
    drawCoordinates(rectCoordinates, Color.BLACK);
  }

  public void drawOLetter() {
    int[][] rectCoordinates = {
        { 375, 265, 380, 285 },
        { 380, 260, 390, 265 },
        { 380, 285, 390, 290 },
        { 390, 265, 394, 285 }
    };
    drawCoordinates(rectCoordinates, Color.BLACK);
  }

  public void drawAGameLetter() {
    int[][] rectCoordinates = {
        { 470, 265, 475, 290 },
        { 475, 260, 485, 265 },
        { 485, 265, 490, 290 },
        { 470, 270, 490, 275 }
    };
    drawCoordinates(rectCoordinates, Color.BLACK);
  }

  public void drawSLetter() {
    int[][] rectCoordinates = {
        { 90, 50, 170, 90 },
        { 50, 90, 90, 130 },
        { 90, 130, 170, 170 },
        { 130, 170, 170, 250 },
        { 50, 210, 130, 250 },
    };

    drawCoordinates(rectCoordinates, Color.GREEN);
  }

  public void drawNLetter() {
    int[][] nRectCoordinates = {
        { 200, 50, 240, 250 },
        { 240, 70, 260, 120 },
        { 260, 120, 280, 170 },
        { 280, 50, 320, 250 }
    };

    drawCoordinates(nRectCoordinates, Color.GREEN);
  }

  public void drawALetter() {
    int[][] aRectCoordinates = {
        { 350, 90, 390, 250 },
        { 380, 50, 440, 90 },
        { 350, 130, 470, 170 },
        { 430, 90, 470, 250 }
    };

    drawCoordinates(aRectCoordinates, Color.GREEN);
  }

  public void drawKLetter() {
    int[][] aRectCoordinates = {
        { 500, 50, 540, 250 },
        { 500, 130, 580, 170 },
        { 580, 130, 620, 50 },
        { 580, 170, 620, 250 }
    };

    drawCoordinates(aRectCoordinates, Color.GREEN);
  }

  public void drawWhiteBackground() {
    int windowWidth = buffer.getWidth();
    int windowHeight = buffer.getHeight();
    Point[] windowVertices = {
        new Point(0, 0),
        new Point(windowWidth, 0),
        new Point(windowWidth, windowHeight),
        new Point(0, windowHeight)
    };

    Color whiteColor = Color.WHITE;
    fill(windowVertices, whiteColor);
  }

  public void drawELetter() {
    int[][] aRectCoordinates = {
        { 650, 50, 690, 250 },
        { 650, 50, 760, 90 },
        { 650, 130, 740, 170 },
        { 650, 210, 760, 250 }
    };

    drawCoordinates(aRectCoordinates, Color.GREEN);
  }

  public void showGameStart() {
    if (buffer != null) {
      drawStartGame();
    }
  }

  public void showWhiteBackground() {
    if (buffer != null) {
      drawWhiteBackground();
    }
  }
}
