package com.github.mealingr;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UtilTest
{
  @Test
  public void testCopy_Row() {
    int[] row = new int[]{1, 2, 3};
    assertThat(Util.copy(row)).isEqualTo(row);
  }

  @Test
  public void testCopy_Grid() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    assertThat(Util.copy(grid)).isDeepEqualTo(grid);
  }

  @Test
  public void testFill_Row() {
    int[] row = new int[]{1, 2, 3};
    Util.fill(row, 0);
    assertThat(row).isEqualTo(new int[]{0, 0, 0});
  }

  @Test
  public void testFill_Grid() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    Util.fill(grid, 0);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {0, 0, 0},
        {0, 0, 0}
    });
  }

  @Test
  public void testReverse() {
    int[] row = new int[]{1, 2, 3};
    assertThat(Util.reverse(row)).isEqualTo(new int[]{3, 2, 1});
  }

  @Test
  public void testReverseRows() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    assertThat(Util.reverseRows(grid)).isDeepEqualTo(new int[][]{
        {3, 2, 1},
        {6, 5, 4}
    });
  }

  @Test
  public void testTranspose() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    assertThat(Util.transpose(grid)).isDeepEqualTo(new int[][]{
        {1, 4},
        {2, 5},
        {3, 6}
    });
  }

  @Test
  public void testRotate90_Zero() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    assertThat(Util.rotate90(grid, 0)).isDeepEqualTo(grid);
  }

  @Test
  public void testRotate90_One() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    assertThat(Util.rotate90(grid, 1)).isDeepEqualTo(new int[][]{
        {4, 1},
        {5, 2},
        {6, 3}
    });
  }

  @Test
  public void testRotate90_Two() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    assertThat(Util.rotate90(grid, 2)).isDeepEqualTo(new int[][]{
        {6, 5, 4},
        {3, 2, 1}
    });
  }

  @Test
  public void testRotate90_Three() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    assertThat(Util.rotate90(grid, 3)).isDeepEqualTo(new int[][]{
        {3, 6},
        {2, 5},
        {1, 4}
    });
  }

  @Test
  public void testRotate90_Four() {
    int[][] grid = new int[][]{
        {1, 2, 3},
        {4, 5, 6}
    };
    assertThat(Util.rotate90(grid, 4)).isDeepEqualTo(grid);
  }

  @Test
  public void testFlipHorizontally() {
    int[][] grid = new int[][]{
        {1, 1},
        {1, 0},
        {1, 0}
    };
    assertThat(Util.flipHorizontally(grid)).isDeepEqualTo(new int[][]{
        {1, 1},
        {0, 1},
        {0, 1}
    });
    assertThat(Util.rotate90(Util.flipVertically(grid), 2)).isDeepEqualTo(new int[][]{
        {1, 1},
        {0, 1},
        {0, 1}
    });
  }

  @Test
  public void testFlipVertically() {
    int[][] grid = new int[][]{
        {1, 1},
        {1, 0},
        {1, 0}
    };
    assertThat(Util.flipVertically(grid)).isDeepEqualTo(new int[][]{
        {1, 0},
        {1, 0},
        {1, 1}
    });
    assertThat(Util.rotate90(Util.flipHorizontally(grid), 2)).isDeepEqualTo(new int[][]{
        {1, 0},
        {1, 0},
        {1, 1}
    });
  }

  @Test
  public void testToString() {
    int[][] grid = new int[][]{
        {1, 10, 3},
        {4, 5, 6}
    };
    assertThat(Util.toString(grid)).isEqualTo("1 ,10,3 \n4 ,5 ,6 ");
  }

  @Test
  public void testCanInsert() {
    int[][] grid = {
        {0, 0, 1, 1, 0, 0},
        {0, 0, 1, 1, 0, 0},
        {1, 1, 1, 1, 1, 1},
        {1, 1, 1, 1, 1, 1},
        {0, 0, 1, 1, 0, 0},
        {0, 0, 1, 1, 0, 0}
    };
    int[][] shape = new int[][]{
        {1}
    };
    assertThat(Util.canInsert(grid, shape, 0, 0)).isTrue();
    assertThat(Util.canInsert(grid, shape, 0, 5)).isTrue();
    assertThat(Util.canInsert(grid, shape, 5, 0)).isTrue();
    assertThat(Util.canInsert(grid, shape, 5, 5)).isTrue();

    assertThat(Util.canInsert(grid, shape, -1, 0)).isFalse();
    assertThat(Util.canInsert(grid, shape, 0, -1)).isFalse();
    assertThat(Util.canInsert(grid, shape, 6, 0)).isFalse();
    assertThat(Util.canInsert(grid, shape, 0, 6)).isFalse();
    assertThat(Util.canInsert(grid, shape, 3, 0)).isFalse();

    shape = new int[][]{
        {1},
        {1}
    };
    assertThat(Util.canInsert(grid, shape, 4, 0)).isTrue();
    assertThat(Util.canInsert(grid, shape, 5, 0)).isFalse();
    assertThat(Util.canInsert(grid, shape, 1, 0)).isFalse();
    assertThat(Util.canInsert(grid, shape, 3, 0)).isFalse();

    shape = new int[][]{
        {1, 1}
    };
    assertThat(Util.canInsert(grid, shape, 0, 4)).isTrue();
    assertThat(Util.canInsert(grid, shape, 0, 5)).isFalse();
    assertThat(Util.canInsert(grid, shape, 0, 1)).isFalse();
    assertThat(Util.canInsert(grid, shape, 0, 3)).isFalse();

    shape = new int[][]{
        {1, 1},
        {1, 1}
    };
    assertThat(Util.canInsert(grid, shape, 4, 4)).isTrue();
    assertThat(Util.canInsert(grid, shape, 4, 5)).isFalse();
    assertThat(Util.canInsert(grid, shape, 5, 4)).isFalse();
    assertThat(Util.canInsert(grid, shape, 3, 4)).isFalse();
    assertThat(Util.canInsert(grid, shape, 4, 3)).isFalse();
  }

  @Test
  public void testInsert() {
    int[][] grid = {
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    };
    int[][] shape = new int[][]{
        {1}
    };
    Util.insert(grid, shape, 0, 0);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {1, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    });
    Util.fill(grid, 0);
    Util.insert(grid, shape, 0, 5);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {0, 0, 0, 0, 0, 1},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    });
    Util.fill(grid, 0);
    Util.insert(grid, shape, 5, 0);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {1, 0, 0, 0, 0, 0}
    });
    Util.fill(grid, 0);
    Util.insert(grid, shape, 5, 5);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 1}
    });
    Util.fill(grid, 0);
    shape = new int[][]{
        {1, 1}
    };
    Util.insert(grid, shape, 0, 0);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {1, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    });
    Util.fill(grid, 0);
    Util.insert(grid, shape, 0, 1);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {0, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    });
    Util.fill(grid, 0);
    Util.insert(grid, shape, 1, 0);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {0, 0, 0, 0, 0, 0},
        {1, 1, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    });
    Util.fill(grid, 0);
    Util.insert(grid, shape, 1, 1);
    assertThat(grid).isDeepEqualTo(new int[][]{
        {0, 0, 0, 0, 0, 0},
        {0, 1, 1, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    });
  }
}
