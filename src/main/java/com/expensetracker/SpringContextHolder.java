package com.expensetracker;


import org.springframework.context.ApplicationContext;


public class SpringContextHolder {
private static ApplicationContext ctx;
public static void setApplicationContext(ApplicationContext ac) { ctx = ac; }
public static ApplicationContext getApplicationContext() { return ctx; }
}