package com.github.mealingr;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

public class Gui
    extends Canvas
{
  private static final Map<Integer, Piece> PIECE_BY_NUMBER = new HashMap<>();

  static {
    for (Piece piece : Piece.values()) {
      PIECE_BY_NUMBER.put(piece.getNumber(), piece);
    }
  }

  private int[][] grid;

  public Gui(int width, int height, int[][] grid) {
    this.grid = grid;
    JFrame frame = new JFrame("Genius Square");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setSize(width, height);
    setBackground(Color.BLACK);
    frame.add(this);
    frame.pack();
    frame.setVisible(true);
  }

  public void setGrid(int[][] grid) {
    this.grid = grid;
    repaint();
  }

  private int maxColumnLength() {
    int max = 0;
    for (int[] row : grid) {
      max = Math.max(max, row.length);
    }
    return max;
  }

  private java.util.List<String> getStrings() {
    java.util.List<String> strings = new ArrayList();
    for (int column = 1; column < maxColumnLength() + 1; column++) {
      strings.add(String.valueOf(column));
    }
    for (int row = 1; row < grid.length + 1; row++) {
      strings.add(String.valueOf((char) (64 + row)));
    }
    return strings;
  }

  private int[] getMaxCharBounds(Graphics g, java.util.List<String> strings) {
    int[] max = new int[]{0, 0};
    for (String s : strings) {
      Rectangle2D stringBounds = g.getFontMetrics().getStringBounds(s, g);
      max[0] = Math.max(max[0], (int) Math.ceil(stringBounds.getWidth()));
      max[1] = Math.max(max[1], (int) Math.ceil(stringBounds.getHeight()));
    }
    return max;
  }

  private void setIdealFontSize(Graphics g, int columnWidth, int rowHeight) {
    java.util.List<String> strings = getStrings();
    int fontSize = 1;
    int[] maxCharBounds = getMaxCharBounds(g, strings);
    while (maxCharBounds[0] <= columnWidth && maxCharBounds[1] <= rowHeight) {
      fontSize++;
      g.setFont(new Font("Arial", Font.PLAIN, fontSize));
      maxCharBounds = getMaxCharBounds(g, strings);
    }
    g.setFont(new Font("Arial", Font.PLAIN, fontSize - 1));
  }

  public void paint(Graphics g) {
    int height = getHeight();
    int width = getWidth();
    int rowHeight = height / (grid.length + 1);
    int maxColumnLength = maxColumnLength();
    int columnWidth = width / (maxColumnLength + 1);

    setIdealFontSize(g, columnWidth, rowHeight);
    // Draw 1 to max column length
    g.setColor(Color.WHITE);
    for (int column = 1; column < maxColumnLength + 1; column++) {
      String s = String.valueOf(column);
      Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
      g.drawString(s, (column * columnWidth + columnWidth / 2) - (int) (Math.ceil(bounds.getWidth() / 2)),
          (rowHeight / 2) + (int) (Math.ceil(bounds.getHeight() / 2)) - g.getFontMetrics().getDescent());
    }
    // Draw A to max row length
    for (int row = 1; row < grid.length + 1; row++) {
      String s = String.valueOf((char) (64 + row));
      Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
      g.drawString(s, (columnWidth / 2) - (int) (Math.ceil(bounds.getWidth() / 2)),
          row * rowHeight + rowHeight / 2 + (int) (Math.ceil(bounds.getHeight() / 2)) -
              g.getFontMetrics().getDescent());
    }
    // Draw pieces
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        Piece piece = PIECE_BY_NUMBER.get(grid[row][column]);
        if (piece != null) {
          g.setColor(piece.getColor());
          if (piece == Piece.PEG) {
            g.fillOval((column + 1) * columnWidth, (row + 1) * rowHeight, columnWidth, rowHeight);
          }
          else {
            g.fillRect((column + 1) * columnWidth, (row + 1) * rowHeight, columnWidth, rowHeight);
          }
        }
      }
    }
    // Draw lines
    ((Graphics2D) g).setStroke(new BasicStroke(2));
    g.setColor(Color.BLACK);
    for (int row = 0; row < grid.length + 1; row++) {
      g.drawLine(0, row * rowHeight, width, row * rowHeight);
      for (int column = 0; column < maxColumnLength + 1; column++) {
        g.drawLine(column * columnWidth, 0, column * columnWidth, height);
      }
    }
  }

  public static void main(String[] args) {
    int[][] grid = new int[][]{
        {1, 1, 3, 3, 1, 10},
        {5, 6, 3, 3, 10, 10},
        {5, 6, 9, 1, 1, 10},
        {5, 6, 9, 9, 1, 7},
        {1, 6, 8, 9, 7, 7},
        {4, 4, 8, 8, 8, 2}
    };
    new Gui(400, 400, grid);
  }
}
