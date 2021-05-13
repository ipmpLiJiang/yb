package cc.mrbird.febs.cn.webxml.sms;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author lijiang
 * @createDate 2021/5/6
 */
public class testXc implements Runnable {

    private Lock lock = new ReentrantLock();

    @Override
    public void run() {
        if (lock.tryLock()) {
            try {
                System.out.println(Thread.currentThread().getName() + "Yes");
                Thread.sleep(1); // 此处等待是给B能锁住机会
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                lock.unlock();
            }
        } else {
            System.out.println(Thread.currentThread().getName() + "No");
        }
    }
}
