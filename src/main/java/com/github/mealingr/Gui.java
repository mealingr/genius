package com.github.mealingr;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
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

  private EnumMap<Piece, java.util.List<java.util.List<Integer>>> piecePositions = new EnumMap<>(Piece.class);

  private Piece piece;

  private int[][] shape;

  private int[][] grid;

  private EnumMap<Piece, Integer> pieces;

  private final AtomicBoolean isSolving = new AtomicBoolean();

  private final AtomicBoolean continueSolving = new AtomicBoolean();

  private int mouseX;

  private int mouseY;

  private boolean mouseInside;

  private Random random;

  public Gui(int width, int height, int[][] grid, EnumMap<Piece, Integer> pieces) {
    this.width = width;
    this.height = height;
    this.originalGrid = Util.copy(grid);
    this.originalPieces = pieces.clone();
    this.grid = Util.copy(grid);
    this.pieces = pieces.clone();
    this.random = new Random();
    initialize();
  }

  private void initialize() {
    setPreferredSize(new Dimension(width, height));
    addMouseMotionListener(new MouseMotionListener()
    {
      @Override
      public void mouseDragged(MouseEvent e) {

      }

      @Override
      public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
      }
    });
    addMouseListener(new MouseListener()
    {
      @Override
      public void mouseClicked(MouseEvent e) {
        new Thread(() -> {
          if (isSolving.get()) {
            return;
          }
          int[] position = getPosition(e.getX(), e.getY());
          Piece piece = PIECE_BY_NUMBER.get(grid[position[0]][position[1]]);
          if (piece != null && piece == Gui.this.piece) {
            List<Integer> positions = piecePositions.get(piece).get(findPositionsIndex(position[0], position[1]));
            Util.clear(grid, positions);
            pieces.put(piece, pieces.get(piece) + 1);
          }
          else if (Gui.this.piece != null) {
            if (pieces.get(Gui.this.piece) > 0 && Util.canInsert(grid, shape, position[0], position[1])) {
              List<Integer> positions = Util.insert(grid, shape, position[0], position[1]);
              pieces.put(Gui.this.piece, pieces.get(Gui.this.piece) - 1);
              List<List<Integer>> allPositions = piecePositions.computeIfAbsent(Gui.this.piece, k -> new ArrayList<>());
              allPositions.add(positions);
            }
          }
          repaint();
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
        mouseInside = true;
        repaint();
      }

      @Override
      public void mouseExited(MouseEvent e) {
        mouseInside = false;
        repaint();
      }
    });
    addMouseWheelListener(e -> {
      if (e.getWheelRotation() < 0) {
        previousPiece();
      }
      else {
        nextPiece();
      }
      repaint();
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
              for (int i = 0; i < solution.size(); i++) {
                int[][] grid = solution.get(i);
                if (i > 0) {
                  addPositions(solution.get(i - 1), solution.get(i));
                }
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
          else if (e.getKeyCode() >= KeyEvent.VK_0 && e.getKeyCode() <= KeyEvent.VK_9) {
            Piece potentialPiece = PIECE_BY_NUMBER.get((e.getKeyCode() - KeyEvent.VK_0) + 1);
            if (piece == potentialPiece) {
              piece = null;
              shape = null;
            }
            else {
              piece = potentialPiece;
              shape = piece.getShapes()[0];
            }
            repaint();
          }
          else if (e.getKeyCode() == KeyEvent.VK_R) {
            shape = Util.rotate90(shape, 1);
            repaint();
          }
          else if (e.getKeyCode() == KeyEvent.VK_F) {
            shape = Util.flipVertically(shape);
            repaint();
          }
          else if (e.getKeyCode() == KeyEvent.VK_E) {
            nextPiece();
            repaint();
          }
          else if (e.getKeyCode() == KeyEvent.VK_Q) {
            previousPiece();
            repaint();
          }
          else if (e.getKeyCode() == KeyEvent.VK_P) {
            if (isSolving.get()) {
              return;
            }
            int[] positions = Util.convertTo1DArray(Util.getRandomPositions(random, grid, pieces.get(Piece.PEG)));
            pieces.put(Piece.PEG, positions.length / 2);
            for (int i = 0; i < positions.length; i += 2) {
              Util.insert(grid, Piece.PEG.getShapes()[0], positions[i], positions[i + 1]);
            }
            pieces.put(Piece.PEG, 0);
            repaint();
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

  private void nextPiece() {
    if (piece == null) {
      piece = PIECE_BY_NUMBER.get(1);
      shape = piece.getShapes()[0];
    }
    else {
      piece = PIECE_BY_NUMBER.get((piece.getNumber() + 1));
      if (piece == null) {
        piece = PIECE_BY_NUMBER.get(1);
      }
      shape = piece.getShapes()[0];
    }
  }

  private void previousPiece() {
    if (piece == null) {
      piece = PIECE_BY_NUMBER.get(Piece.values().length);
      shape = piece.getShapes()[0];
    }
    else {
      piece = PIECE_BY_NUMBER.get((piece.getNumber() - 1));
      if (piece == null) {
        piece = PIECE_BY_NUMBER.get(Piece.values().length);
      }
      shape = piece.getShapes()[0];
    }
  }

  private Integer findPositionsIndex(int row, int column) {
    Piece piece = PIECE_BY_NUMBER.get(grid[row][column]);
    if (piece == null) {
      return null;
    }
    List<List<Integer>> allPositions = piecePositions.computeIfAbsent(piece, k -> new ArrayList<>());
    for (int i = 0; i < allPositions.size(); i++) {
      List<Integer> positions = allPositions.get(i);
      for (int j = 0; j < positions.size(); j += 2) {
        if (positions.get(j) == row && positions.get(j + 1) == column) {
          return i;
        }
      }
    }
    return null;
  }

  public void addPositions(int[][] gridBefore, int[][] gridAfter) {
    List<Integer> positions = new ArrayList<>();
    Piece piece = null;
    for (int row = 0; row < gridAfter.length; row++) {
      for (int column = 0; column < gridAfter[row].length; column++) {
        if (gridAfter[row][column] - gridBefore[row][column] != Grid.EMPTY) {
          piece = PIECE_BY_NUMBER.get(gridAfter[row][column]);
          positions.add(row);
          positions.add(column);
        }
      }
    }
    piecePositions.computeIfAbsent(piece, k -> new ArrayList<>()).add(positions);
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

    // Draw selected piece
    if (mouseInside && piece != null) {
      drawShapeAndCount(g, piece.getColor(), shape, mouseX - columnWidth / 2, mouseY - rowHeight / 2, columnWidth,
          rowHeight, pieces.get(piece));
    }

    // Draw winning smiley face
    if (!isSolving.get()) {
      EnumMap<Piece, Integer> clone = pieces.clone();
      clone.put(Piece.PEG, 0);
      if (Util.sum(clone) == 0) {
        drawSmileyFace(g, 0, 0, columnWidth, rowHeight);
      }
    }
  }

  private void drawShape(Graphics g, Color color, int[][] shape, int x, int y, int columnWidth, int rowHeight) {
    g.setColor(color);
    for (int shapeRow = 0; shapeRow < shape.length; shapeRow++) {
      for (int shapeColumn = 0; shapeColumn < shape[shapeRow].length; shapeColumn++) {
        if (shape[shapeRow][shapeColumn] != Grid.EMPTY) {
          if (piece == Piece.PEG) {
            g.fillOval(x + shapeColumn * columnWidth, y + shapeRow * rowHeight, columnWidth, rowHeight);
          }
          else {
            g.fillRect(x + (shapeColumn * columnWidth), y + (shapeRow * rowHeight), columnWidth, rowHeight);
          }
        }
      }
    }
    g.setColor(Color.BLACK);
    for (int shapeRow = 0; shapeRow < shape.length; shapeRow++) {
      for (int shapeColumn = 0; shapeColumn < shape[shapeRow].length; shapeColumn++) {
        if (shape[shapeRow][shapeColumn] != Grid.EMPTY) {
          if (piece == Piece.PEG) {
            g.drawOval(x + shapeColumn * columnWidth, y + shapeRow * rowHeight, columnWidth, rowHeight);
          }
          else {
            g.drawRect(x + (shapeColumn * columnWidth), y + (shapeRow * rowHeight), columnWidth, rowHeight);
          }
        }
      }
    }
  }

  private int[] getShapeCentre(int[][] shape, int columnWidth, int rowHeight) {
    return new int[]{
        (shape[0].length / 2) * columnWidth + ((shape[0].length % 2) * columnWidth / 2),
        (shape.length / 2) * rowHeight + ((shape.length % 2) * rowHeight / 2)
    };
  }

  private void drawShapeAndCount(
      Graphics g,
      Color color,
      int[][] shape,
      int x,
      int y,
      int columnWidth,
      int rowHeight,
      int count)
  {
    drawShape(g, color, shape, x, y, columnWidth, rowHeight);
    String s = String.valueOf(count);
    Rectangle2D bounds = g.getFontMetrics().getStringBounds(s, g);
    g.setColor(Color.WHITE);
    g.drawString(s, x + (columnWidth / 2) - (int) (Math.ceil(bounds.getWidth() / 2)),
        y + shape.length * rowHeight + rowHeight / 2 + (int) (Math.ceil(bounds.getHeight() / 2)) -
            g.getFontMetrics().getDescent());
  }

  private void drawSmileyFace(Graphics g, int x, int y, int columnWidth, int rowHeight) {
    g.setColor(Color.YELLOW);
    g.fillOval(x, y, columnWidth, rowHeight);
    g.setColor(Color.BLACK);
    g.drawOval(x, y, columnWidth, rowHeight);
    g.fillOval(x + (columnWidth / 4), y + rowHeight / 4, (columnWidth / 5), (rowHeight / 5));
    g.fillOval(x + (columnWidth - columnWidth / 4 - columnWidth / 5), y + rowHeight / 4, (columnWidth / 5),
        (rowHeight / 5));
    g.drawArc(x + (columnWidth / 4), y + (rowHeight - rowHeight / 2), (columnWidth / 2), (rowHeight / 3), 0, -180);
  }
}
