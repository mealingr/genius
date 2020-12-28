package com.github.mealingr;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.stream.Collectors;

public class Util
{
  public static int[] copy(int[] row) {
    int[] copy = new int[row.length];
    for (int column = 0; column < row.length; column++) {
      copy[column] = row[column];
    }
    return copy;
  }

  public static int[][] copy(int[][] grid) {
    int[][] copy = new int[grid.length][];
    for (int row = 0; row < grid.length; row++) {
      copy[row] = copy(grid[row]);
    }
    return copy;
  }

  public static int[][][] copy(int[][][] grids) {
    int[][][] copy = new int[grids.length][][];
    for (int grid = 0; grid < grids.length; grid++) {
      copy[grid] = copy(grids[grid]);
    }
    return copy;
  }

  public static void fill(int[] row, int value) {
    for (int column = 0; column < row.length; column++) {
      row[column] = value;
    }
  }

  public static void fill(int[][] grid, int value) {
    for (int row = 0; row < grid.length; row++) {
      fill(grid[row], value);
    }
  }

  public static int[] reverse(int[] row) {
    int[] reverse = new int[row.length];
    for (int column = 0; column < row.length; column++) {
      reverse[row.length - 1 - column] = row[column];
    }
    return reverse;
  }

  public static int[][] reverseRows(int[][] grid) {
    int[][] reverse = new int[grid.length][];
    for (int row = 0; row < grid.length; row++) {
      reverse[row] = reverse(grid[row]);
    }
    return reverse;
  }

  public static int[][] transpose(int[][] grid) {
    int[][] transpose = new int[grid[0].length][grid.length];
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        transpose[column][row] = grid[row][column];
      }
    }
    return transpose;
  }

  private static int[][] rotate90(int[][] grid) {
    return reverseRows(transpose(grid));
  }

  public static int[][] rotate90(int[][] grid, int times) {
    int[][] rotation = copy(grid);
    for (int i = 0; i < times; i++) {
      rotation = rotate90(rotation);
    }
    return rotation;
  }

  public static int[][] flipVertically(int[][] grid) {
    int[][] flippedVertically = new int[grid.length][];
    for (int row = 0; row < grid.length; row++) {
      flippedVertically[grid.length - 1 - row] = copy(grid[row]);
    }
    return flippedVertically;
  }

  public static int[][] flipHorizontally(int[][] grid) {
    return reverseRows(grid);
  }

  public static boolean canInsert(int[][] grid, int[][] shape, int row, int column) {
    if (row < 0) {
      return false;
    }
    if (column < 0) {
      return false;
    }
    for (int shapeRow = 0; shapeRow < shape.length; shapeRow++) {
      int targetRow = row + shapeRow;
      if (targetRow >= grid.length) {
        return false;
      }
      for (int shapeColumn = 0; shapeColumn < shape[shapeRow].length; shapeColumn++) {
        int targetColumn = column + shapeColumn;
        if (targetColumn >= grid[targetRow].length) {
          return false;
        }
        if (shape[shapeRow][shapeColumn] != Grid.EMPTY && grid[targetRow][targetColumn] != Grid.EMPTY) {
          return false;
        }
      }
    }
    return true;
  }

  public static void insert(int[][] grid, int[][] shape, int row, int column) {
    for (int shapeRow = 0; shapeRow < shape.length; shapeRow++) {
      int targetRow = row + shapeRow;
      for (int shapeColumn = 0; shapeColumn < shape[shapeRow].length; shapeColumn++) {
        int targetColumn = column + shapeColumn;
        if (shape[shapeRow][shapeColumn] == Grid.EMPTY) {
          continue;
        }
        grid[targetRow][targetColumn] = shape[shapeRow][shapeColumn];
      }
    }
  }

  public static int max(int[][] grid) {
    int max = 0;
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        max = Math.max(max, grid[row][column]);
      }
    }
    return max;
  }

  public static String toString(int[][] grid) {
    int elementLength = String.valueOf(max(grid)).length();
    StringBuilder stringBuilder = new StringBuilder();
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        StringBuilder value = new StringBuilder(String.valueOf(grid[row][column]));
        while (value.length() < elementLength) {
          value.append(' ');
        }
        stringBuilder.append(value).append(',');
      }
      stringBuilder.deleteCharAt(stringBuilder.length() - 1).append("\n");
    }
    return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
  }

  public static List<Integer> getAllFlatPositions(int[][] grid) {
    List<Integer> allFlatPositions = new ArrayList<>();
    for (int flatPosition = 0; flatPosition < size(grid); flatPosition++) {
      allFlatPositions.add(flatPosition);
    }
    return allFlatPositions;
  }

  public static List<Integer> getRandomFlatPositions(Random random, int[][] grid, int number) {
    List<Integer> allFlatPositions = getAllFlatPositions(grid);
    List<Integer> randomFlatPositions = new ArrayList<>();
    for (int i = 0; i < number; i++) {
      randomFlatPositions.add(allFlatPositions.remove(random.nextInt(allFlatPositions.size())));
    }
    return randomFlatPositions;
  }

  public static List<Integer> flatPositionsToPositions(int[][] grid, List<Integer> flatPositions) {
    List<Integer> positions = new ArrayList<>();
    int flatPosition = 0;
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        if (flatPositions.contains(flatPosition++)) {
          positions.add(row);
          positions.add(column);
        }
      }
    }
    return positions;
  }

  public static List<Integer> getRandomPositions(Random random, int[][] grid, int number) {
    return flatPositionsToPositions(grid, getRandomFlatPositions(random, grid, number));
  }

  public static int size(int[] row) {
    return row.length;
  }

  public static int size(int[][] grid) {
    int size = 0;
    for (int row = 0; row < grid.length; row++) {
      size += size(grid[row]);
    }
    return size;
  }

  public static List<Integer> convertTo1DList(int[] row) {
    return Arrays.stream(row).boxed().collect(Collectors.toList());
  }

  public static List<List<Integer>> convertTo2DList(int[][] grid) {
    List<List<Integer>> conversion = new ArrayList<>();
    for (int row = 0; row < grid.length; row++) {
      conversion.add(convertTo1DList(grid[row]));
    }
    return conversion;
  }

  public static List<List<List<Integer>>> convertTo3DList(int[][][] grid) {
    List<List<List<Integer>>> conversion = new ArrayList<>();
    for (int row = 0; row < grid.length; row++) {
      conversion.add(convertTo2DList(grid[row]));
    }
    return conversion;
  }

  public static int[] convertTo1DArray(List<Integer> row) {
    return row.stream().mapToInt(i -> i).toArray();
  }

  public static int[][] convertTo2DArray(List<List<Integer>> grid) {
    int[][] conversion = new int[grid.size()][];
    for (int row = 0; row < grid.size(); row++) {
      conversion[row] = convertTo1DArray(grid.get(row));
    }
    return conversion;
  }

  public static int[][][] convertTo3DArray(List<List<List<Integer>>> grid) {
    int[][][] conversion = new int[grid.size()][][];
    for (int row = 0; row < grid.size(); row++) {
      conversion[row] = convertTo2DArray(grid.get(row));
    }
    return conversion;
  }

  public static int[][][] getUniqueShapes(int[][] shape) {
    List<List<List<Integer>>> shapes = new ArrayList<>();
    for (int rotation = 0; rotation < 4; rotation++) {
      int[][] rotated = Util.rotate90(shape, rotation);
      List<List<Integer>> rotatedLists = Util.convertTo2DList(rotated);
      if (!shapes.contains(rotatedLists)) {
        shapes.add(rotatedLists);
      }
    }
    int[][] flippedShape = Util.flipVertically(shape);
    for (int rotation = 0; rotation < 4; rotation++) {
      int[][] rotated = Util.rotate90(flippedShape, rotation);
      List<List<Integer>> rotatedLists = Util.convertTo2DList(rotated);
      if (!shapes.contains(rotatedLists)) {
        shapes.add(rotatedLists);
      }
    }
    return Util.convertTo3DArray(shapes);
  }

  public static List<Integer> getMoves(int[][] grid, int[][] shape) {
    List<Integer> moves = new ArrayList<>();
    for (int row = 0; row < grid.length; row++) {
      for (int column = 0; column < grid[row].length; column++) {
        if (Util.canInsert(grid, shape, row, column)) {
          moves.add(row);
          moves.add(column);
        }
      }
    }
    return moves;
  }

  public static List<Move> getMoves(int[][] grid, Piece piece) {
    List<Move> moves = new ArrayList<>();
    for (int[][] shape : piece.getShapes()) {
      for (int row = 0; row < grid.length; row++) {
        for (int column = 0; column < grid[row].length; column++) {
          if (Util.canInsert(grid, shape, row, column)) {
            moves.add(new Move(piece, shape, row, column));
          }
        }
      }
    }
    return moves;
  }

  public static List<Move> getMoves(int[][] grid, EnumMap<Piece, Integer> pieces) {
    List<Move> moves = new ArrayList<>();
    for (Entry<Piece, Integer> piece : pieces.entrySet()) {
      if (piece.getValue() > 0) {
        moves.addAll(getMoves(grid, piece.getKey()));
      }
    }
    return moves;
  }

  public static int sum(EnumMap<Piece, Integer> pieces) {
    int sum = 0;
    for (int value : pieces.values()) {
      sum += value;
    }
    return sum;
  }

  public static List<Piece> getAllPieces(EnumMap<Piece, Integer> pieces) {
    List<Piece> allPieces = new ArrayList<>();
    for (Entry<Piece, Integer> piece : pieces.entrySet()) {
      for (int number = 0; number < piece.getValue(); number++) {
        allPieces.add(piece.getKey());
      }
    }
    return allPieces;
  }

  public static Piece getRandomPiece(Random random, EnumMap<Piece, Integer> pieces) {
    List<Piece> allPieces = getAllPieces(pieces);
    return allPieces.get(random.nextInt(allPieces.size()));
  }

  public static List<int[][]> solveShot(Random random, int[][] grid, EnumMap<Piece, Integer> pieces) {
    List<int[][]> solution = new ArrayList<>();
    while (solution.isEmpty()) {
      int[][] gridCopy = Util.copy(grid);
      solution.add(Util.copy(gridCopy));
      EnumMap<Piece, Integer> piecesCopy = pieces.clone();
      while (sum(piecesCopy) != 0) {
        // Select a random piece
        Piece piece = getRandomPiece(random, piecesCopy);
        piecesCopy.put(piece, piecesCopy.get(piece) - 1);
        // Select a random move
        List<Move> moves = getMoves(gridCopy, piece);
        if (moves.isEmpty()) {
          piecesCopy.put(piece, piecesCopy.get(piece) + 1);
          solution = new ArrayList<>();
          break;
        }
        Move move = moves.get(random.nextInt(moves.size()));
        // Do the move
        Util.insert(gridCopy, move.getShape(), move.getRow(), move.getColumn());
        solution.add(Util.copy(gridCopy));
      }
      if (sum(piecesCopy) == 0) {
        return solution;
      }
    }
    return null;
  }

  public static List<int[][]> solveBrute(int[][] grid, EnumMap<Piece, Integer> pieces) {
    List<List<int[][]>> solutions = new ArrayList<>();
    solveBrute(solutions, grid, pieces);
    if (solutions.isEmpty()) {
      return null;
    }
    for (List<int[][]> solution : solutions) {
      Collections.reverse(solution);
    }
    return solutions.get(0);
  }

  private static List<int[][]> solveBrute(List<List<int[][]>> solutions, int[][] grid, EnumMap<Piece, Integer> pieces) {
    if (solutions.size() > 0) {
      return null;
    }
    if (sum(pieces) == 0) {
      List<int[][]> solution = new ArrayList<>();
      solution.add(grid);
      solutions.add(solution);
      return solution;
    }
    List<Move> moves = getMoves(grid, pieces);
    Collections.shuffle(moves);
    for (Move move : moves) {
      int[][] gridCopy = Util.copy(grid);
      EnumMap<Piece, Integer> piecesCopy = pieces.clone();

      Util.insert(gridCopy, move.getShape(), move.getRow(), move.getColumn());
      piecesCopy.put(move.getPiece(), piecesCopy.get(move.getPiece()) - 1);

      List<int[][]> solution = solveBrute(solutions, gridCopy, piecesCopy);
      if (solution != null) {
        solution.add(grid);
        return solution;
      }

      if (solutions.size() > 0) {
        return null;
      }
    }
    return null;
  }

  public static String normalise(String string) {
    return string.toUpperCase().trim();
  }

  public static String[] normalise(String[] strings) {
    String[] normalised = new String[strings.length];
    for (int i = 0; i < strings.length; i++) {
      normalised[i] = normalise(strings[i]);
    }
    return normalised;
  }

  public static int[] stringPositionsToPositions(String[] stringPositions) {
    List<Integer> positions = new ArrayList<>();
    for (String position : stringPositions) {
      int row = position.charAt(0) - 'A';
      int column = Character.getNumericValue(position.charAt(1)) - 1;
      positions.add(row);
      positions.add(column);
    }
    return convertTo1DArray(positions);
  }
}
