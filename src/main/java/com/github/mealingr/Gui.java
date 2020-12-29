package com.github.mealingr;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.*;

public class Gui
    extends JPanel
{
  private static final Map<Integer, Piece> PIECE_BY_NUMBER = new HashMap<>();

  static {
    for (Piece piece : Piece.values()) {
      PIECE_BY_NUMBER.put(piece.getNumber(), piece);
    }
  }

  private int width;

  private int height;

  private int[][] originalGrid;

  private EnumMap<Piece, Integer> originalPieces;

  private int[][] grid;

  private EnumMap<Piece, Integer> pieces;

  private final AtomicBoolean isSolving = new AtomicBoolean();

  private final AtomicBoolean continueSolving = new AtomicBoolean();

  public Gui(int width, int height, int[][] grid, EnumMap<Piece, Integer> pieces) {
    this.width = width;
    this.height = height;
    this.originalGrid = Util.copy(grid);
    this.originalPieces = pieces.clone();
    this.grid = Util.copy(grid);
    this.pieces = pieces.clone();
    initialize();
  }

  private void initialize() {
    setPreferredSize(new Dimension(width, height));
    addMouseListener(new MouseListener()
    {
      @Override
      public void mouseClicked(MouseEvent e) {
        new Thread(() -> {
          if (isSolving.get()) {
            return;
          }
          int[] position = getPosition(e.getX(), e.getY());
          if (grid[position[0]][position[1]] == Piece.PEG.getNumber()) {
            grid[position[0]][position[1]] = Grid.EMPTY;
            pieces.put(Piece.PEG, pieces.get(Piece.PEG) + 1);
            repaint();
          }
          else if (grid[position[0]][position[1]] == Grid.EMPTY) {
            if (pieces.get(Piece.PEG) > 0) {
              grid[position[0]][position[1]] = Piece.PEG.getNumber();
              pieces.put(Piece.PEG, pieces.get(Piece.PEG) - 1);
              repaint();
            }
          }
        }).start();
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
    });
    JFrame frame = new JFrame("Genius Square");
    frame.addKeyListener(new KeyListener()
    {
      @Override
      public void keyTyped(KeyEvent e) {

      }

      @Override
      public void keyPressed(KeyEvent e) {
        new Thread(() -> {
          if (isSolving.get()) {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
              continueSolving.set(false);
            }
            return;
          }
          if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            continueSolving.set(true);
            isSolving.set(true);
            repaint();
            java.util.List<int[][]> solution;
            if (e.isShiftDown()) {
              solution = Util.solveBrute(grid, pieces, continueSolving);
            }
            else {
              solution = Util.solveShot(new Random(), grid, pieces, continueSolving);
            }
            if (solution != null) {
              for (Piece piece : pieces.keySet()) {
                if (piece != Piece.PEG) {
                  pieces.put(piece, 0);
                }
              }
              for (int[][] grid : solution) {
                try {
                  Thread.sleep(200);
                }
                catch (InterruptedException ignored) {
                }
                setGrid(grid);
              }
            }
            isSolving.set(false);
            repaint();
          }
          else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            if (e.isShiftDown()) {
              setGrid(Util.copyJustPegs(grid));
              EnumMap<Piece, Integer> clone = originalPieces.clone();
              clone.put(Piece.PEG, pieces.get(Piece.PEG));
              setPieces(clone);
            }
            else {
              setGrid(Util.copy(originalGrid));
              setPieces(originalPieces.clone());
            }
          }
        }).start();
      }

      @Override
      public void keyReleased(KeyEvent e) {
      }
    });
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.add(this);
    frame.pack();
    frame.setVisible(true);
  }

  private int[] getPosition(int x, int y) {
    int height = getHeight();
    int width = getWidth();
    int rowHeight = height / (grid.length + 1);
    int maxColumnLength = maxColumnLength();
    int columnWidth = width / (maxColumnLength + 1);
    int row = y / rowHeight;
    int column = x / columnWidth;
    return new int[]{row - 1, column - 1};
  }

  public void setGrid(int[][] grid) {
    this.grid = grid;
    repaint();
  }

  public void setPieces(EnumMap<Piece, Integer> pieces) {
    this.pieces = pieces;
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

  @Override
  public void paintComponent(Graphics g) {
    int height = getHeight();
    int width = getWidth();
    int rowHeight = height / (grid.length + 1);
    int maxColumnLength = maxColumnLength();
    int columnWidth = width / (maxColumnLength + 1);

    setIdealFontSize(g, columnWidth, rowHeight);

    // Draw the background
    g.setColor(Color.BLACK);
    g.fillRect(0, 0, width, height);

    // Draw a green circle if it's solving
    if (isSolving.get()) {
      g.setColor(Color.GREEN);
      g.fillOval(0, 0, columnWidth, rowHeight);
    }
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
}
