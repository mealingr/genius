package com.github.mealingr;

import java.util.EnumMap;
import java.util.Random;

public class Game
{
  public static final EnumMap<Piece, Integer> DEFAULT_PIECES = new EnumMap<>(Piece.class);

  static {
    for (Piece piece : Piece.values()) {
      DEFAULT_PIECES.put(piece, 1);
    }
    DEFAULT_PIECES.put(Piece.PEG, 7);
  }

  private Random random;

  private Grid grid;

  private EnumMap<Piece, Integer> pieces;

  public Game() {
    initialize(null);
  }

  public Game(int... positions) {
    initialize(positions);
  }

  public Game(String... positions) {
    initialize(Util.stringPositionsToPositions(Util.normalise(positions)));
  }

  public Game(String positions) {
    initialize(Util.stringPositionsToPositions(Util.normalise(positions.split(","))));
  }

  private void initialize(int[] positions) {
    random = new Random();
    grid = new Grid();
    pieces = DEFAULT_PIECES.clone();
    if (positions == null) {
      positions = Util.convertTo1DArray(grid.getRandomPositions(random, pieces.get(Piece.PEG)));
    }
    if (positions.length % 2 != 0) {
      throw new RuntimeException("Bad Peg Positions");
    }
    if (positions.length / 2 > grid.size()) {
      throw new RuntimeException("Too Many Peg Positions");
    }
    pieces.put(Piece.PEG, positions.length / 2);
    for (int i = 0; i < positions.length; i += 2) {
      grid.insert(Piece.PEG.getShapes()[0], positions[i], positions[i + 1]);
    }
    pieces.put(Piece.PEG, 0);
  }

  public static void main(String[] args) throws Exception {
    new Gui(400, 400, new Grid().getGrid(), DEFAULT_PIECES.clone());
  }
}
