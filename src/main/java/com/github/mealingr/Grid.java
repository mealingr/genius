package com.github.mealingr;

import java.util.List;
import java.util.Random;

public class Grid
{
  public static final int EMPTY = 0;

  public static final int DEFAULT_ROWS = 6;

  public static final int DEFAULT_COLUMNS = 6;

  private final int[][] grid;

  public Grid(int rows, int columns) {
    grid = new int[rows][columns];
    Util.fill(grid, EMPTY);
  }

  public Grid() {
    this(DEFAULT_ROWS, DEFAULT_COLUMNS);
  }

  public int[][] getGrid() {
    return Util.copy(grid);
  }

  public int size() {
    return Util.size(grid);
  }

  public boolean canInsert(int[][] shape, int row, int column) {
    return Util.canInsert(grid, shape, row, column);
  }

  public void insert(int[][] shape, int row, int column) {
    Util.insert(grid, shape, row, column);
  }

  public List<Integer> getRandomPositions(Random random, int number) {
    return Util.getRandomPositions(random, grid, number);
  }

  @Override
  public String toString() {
    return Util.toString(grid);
  }
}
