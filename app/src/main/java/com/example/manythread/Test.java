package com.example.manythread;

class TicketWindow extends Thread {
    // 车票数量
    private static int ticket = 30;

    @Override
    public void run() {
        while (true) {
            if (ticket > 0) {
                System.out.println(Thread.currentThread().getName() + "卖出了第" + ticket-- + "张票，" + "剩余" + ticket + "张票");
            } else {
                System.out.println(Thread.currentThread().getName() + "余票不足,停止售票!");
                break;
            }
        }
    }
}

public class Test {
    public static void main(String[] args) {
        TicketWindow tw1 = new TicketWindow();
        TicketWindow tw2 = new TicketWindow();
        TicketWindow tw3 = new TicketWindow();

        tw1.setName("窗口1");
        tw2.setName("窗口2");
        tw3.setName("窗口3");

        tw1.start();
        tw2.start();
        tw3.start();
    }
}
