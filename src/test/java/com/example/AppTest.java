package com.example;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppTest {

  @Test
  public void testAddition() {

    App app = new App();

    int result = app.add(10, 20);

   assertEquals(30, result);

   }
}
