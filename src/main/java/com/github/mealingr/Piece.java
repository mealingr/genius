package com.github.mealingr;

import java.awt.*;

public enum Piece
{
  PEG(new Color(225, 189, 147), 1, new int[][]{
      {1}
  }),
  ONE_SQUARE(new Color(14, 87, 241), 2, new int[][]{
      {2}
  }),
  FOUR_SQUARE(new Color(37, 164, 95), 3, new int[][]{
      {3, 3},
      {3, 3}
  }),
  TWO_LINE(new Color(207, 146, 92), 4, new int[][]{
      {4},
      {4}
  }),
  THREE_LINE(new Color(255, 151, 36), 5, new int[][]{
      {5},
      {5},
      {5}
  }),
  FOUR_LINE(new Color(174, 161, 153), 6, new int[][]{
      {6},
      {6},
      {6},
      {6}
  }),
  THREE_L(new Color(184, 76, 177), 7, new int[][]{
      {7, Grid.EMPTY},
      {7, 7}
  }),
  FOUR_L(new Color(46, 194, 254), 8, new int[][]{
      {8, Grid.EMPTY},
      {8, Grid.EMPTY},
      {8, 8}
  }),
  FOUR_S(new Color(227, 44, 38), 9, new int[][]{
      {Grid.EMPTY, 9, 9},
      {9, 9, Grid.EMPTY}
  }),
  FOUR_T(new Color(255, 249, 3), 10, new int[][]{
      {10, 10, 10},
      {Grid.EMPTY, 10, Grid.EMPTY}
  });

  private final Color color;

  private final int number;

  private final int[][][] shapes;

  Piece(Color color, int number, int[][] shape) {
    this.color = color;
    this.number = number;
    this.shapes = Util.getUniqueShapes(shape);
  }

  public Color getColor() {
    return color;
  }

  public int[][][] getShapes() {
    return Util.copy(shapes);
  }

  public int getNumber() {
    return number;
  }
}
