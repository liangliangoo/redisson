package com.xiaoxiong.lock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @Author 六月
 * @Date 2022/9/30 11:24
 * @Version 1.0
 */
public class RedisLockTest {

    private ExecutorService executorService;
    private RedissonClient redissonClient;

    @BeforeEach
    private void init() {
        this.executorService = Executors.newFixedThreadPool(2);
        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://127.0.0.1:6379");
        this.redissonClient = Redisson.create(config);
    }

    @Test
    public void testRedissonLock1() throws IOException {
        RLock lock1 = redissonClient.getLock("test-1");
        RLock lock2 = redissonClient.getLock("test-1");
        executorService.execute(() -> {
            try {
                lock1.lock();
                System.out.println(Thread.currentThread().getName() + "\t" + " 加锁成功" + "\t" + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock1.unlock();
                System.out.println(Thread.currentThread().getName() + "\t" + "解锁成功" + "\t" + System.currentTimeMillis());
            }
        });

        executorService.execute(() -> {
            try {
                lock2.lock();
                System.out.println(Thread.currentThread().getName() + "\t" + " 加锁成功" + "\t" + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock2.unlock();
                System.out.println(Thread.currentThread().getName() + "\t" + "解锁成功" + "\t" + System.currentTimeMillis());
            }
        });

        // 避免线程终止
        System.in.read();

    }

    @Test
    public void testRedissonLock2() throws IOException {
        RLock lock1 = redissonClient.getLock("test-1");
        RLock lock2 = redissonClient.getLock("test-1");
        executorService.execute(() -> {
            lock1.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t" + " 加锁成功" + "\t" + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock1.unlock();
                System.out.println(Thread.currentThread().getName() + "\t" + "解锁成功" + "\t" + System.currentTimeMillis());
            }
        });

        executorService.execute(() -> {
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            lock2.lock();
            try {
                System.out.println(Thread.currentThread().getName() + "\t" + " 加锁成功" + "\t" + System.currentTimeMillis());
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                lock2.unlock();
                System.out.println(Thread.currentThread().getName() + "\t" + "解锁成功" + "\t" + System.currentTimeMillis());
            }
        });

        // 避免线程终止
        System.in.read();

    }

}
