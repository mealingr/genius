package com.github.mealingr;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class GridTest
{
  @Test
  public void testCreate() {
    assertThat(new Grid(1, 2).getGrid()).isDeepEqualTo(new int[][]{
        {0, 0}
    });
    assertThat(new Grid(2, 1).getGrid()).isDeepEqualTo(new int[][]{
        {0},
        {0}
    });
    assertThat(new Grid().getGrid()).isDeepEqualTo(new int[][]{
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0},
        {0, 0, 0, 0, 0, 0}
    });
  }
}
