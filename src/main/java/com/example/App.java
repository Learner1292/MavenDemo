package com.example;

public class App {


  public int add(int a, int b) {
    return a+b;
  }

  public static void main(String[] args) {

    App app = new App();

    System.out.println("Hello From Apache Maven on EC2");

    int result = app.add(10, 20);

    System.out.println("10 + 20 =" + result);
 }
}

