package com.example.manythread;

public class MoreThread {
    public static void main(String[] args) {
        //创建线程对象
        SellTicket t1 = new SellTicket();
        SellTicket t2 = new SellTicket();
        SellTicket t3 = new SellTicket();
        //各线程起名字
        t1.setName("一号窗口");
        t2.setName("二号窗口");
        t3.setName("三号窗口");
        //启动线程
        t1.start();
        t2.start();
        t3.start();
    }
    static class SellTicket extends Thread {
        static int ticket = 100;//初始有100张票
        @Override
        public void run() {
            while (ticket > 0) {//一直卖票，直到票卖完
                try {//模拟选中票后的网络延迟
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(getName() + "卖了第" + ticket-- + "张票");
            }
        }
    }
}
