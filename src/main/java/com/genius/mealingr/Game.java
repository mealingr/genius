package com.genius.mealingr;

public class Game
{
  private static final int EMPTY = 0;

  private static final int PEG = 1;

  private int rows = 6;

  private int columns = 6;

  private int pegs = 7;

  private int[][] grid = new int[rows][columns];

  private boolean canInsert(int[][] grid, int[][] shape, int row, int column) {
    for (int shapeRow = 0; shapeRow < shape.length; shapeRow++) {
      for (int shapeColumn = 0; shapeColumn < shape[shapeRow].length; shapeColumn++) {
        if (row + shapeRow >= grid.length)
      }
    }
  }

  private int[][] copyGrid() {
    int[][] gridCopy = new int[grid.length][];
    for (int row = 0; row < grid.length; row++) {
      gridCopy[row] = new int[grid[row].length];
      for (int column = 0; column < grid[row].length; column++) {
        gridCopy[row][column] = grid[row][column];
      }
    }
    return gridCopy;
  }

  public static void main(String[] args) {
    Game game = new Game();
  }
}
