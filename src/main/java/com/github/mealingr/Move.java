package com.github.mealingr;

import java.util.Arrays;
import java.util.Objects;

public class Move
{
  private final Piece piece;

  private final int[][] shape;

  private final int row;

  private final int column;

  public Move(Piece piece, int[][] shape, int row, int column) {
    this.piece = piece;
    this.shape = Util.copy(shape);
    this.row = row;
    this.column = column;
  }

  public Piece getPiece() {
    return piece;
  }

  public int[][] getShape() {
    return Util.copy(shape);
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Move move = (Move) o;
    return row == move.row &&
        column == move.column &&
        piece == move.piece &&
        Arrays.equals(shape, move.shape);
  }

  @Override
  public int hashCode() {
    int result = Objects.hash(piece, row, column);
    result = 31 * result + Arrays.hashCode(shape);
    return result;
  }
}
