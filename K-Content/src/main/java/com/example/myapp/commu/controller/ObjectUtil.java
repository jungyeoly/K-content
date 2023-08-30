package com.example.myapp.commu.controller;

import org.springframework.stereotype.Component;

@Component("objectUtil")
public class ObjectUtil {
   public static int divideAndGetQuotient(int dividend, int divisor) {
      return dividend / divisor;
   }

}