import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;

public class SnakeGrid {
  private int maxSizeGrid;
  private int quantCell;
  private int sizeCell;
  private BufferedImage buffer;
  private Graphics graphicsBuffer;

  public SnakeGrid(int maxSizeGrid, int quantCell, BufferedImage buffer) {
    this.maxSizeGrid = maxSizeGrid;
    this.quantCell = quantCell;
    this.sizeCell = maxSizeGrid / quantCell;
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

  public void drawGrid(Color color, int panelWidth, int panelHeight) {
    int gridWidth = quantCell * sizeCell;
    int gridHeight = quantCell * sizeCell;

    int emptySpaceX = (panelWidth - gridWidth) / 2;
    int emptySpaceY = (panelHeight - gridHeight) / 2;

    for (int i = 0; i < quantCell; i++) {
      for (int j = 0; j < quantCell; j++) {
        int x1 = emptySpaceX + (i * sizeCell);
        int y1 = emptySpaceY + (j * sizeCell);
        int x2 = x1 + sizeCell;
        int y2 = y1 + sizeCell;
        Point[] vertices = {
            new Point(x1, y1),
            new Point(x1, y2),
            new Point(x2, y2),
            new Point(x2, y1)
        };
        scanLine(vertices, Color.GRAY);
        drawRect(x1, y1, x2, y2, color);
      }
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

  public void showGrid() {
    if (buffer != null) {
      graphicsBuffer.setColor(Color.WHITE);
      drawGrid(Color.WHITE, maxSizeGrid, maxSizeGrid);
    }
  }

}
