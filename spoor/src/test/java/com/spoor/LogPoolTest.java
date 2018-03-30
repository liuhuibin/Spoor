package com.spoor;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
/**
 * Desc:
 * Author:Created by huibin
 * Date: Created on 18/3/26 20:17
 */

public class LogPoolTest {

    //run this UT ,it's better to update LogPool#POOL_SIZE to not final
   /* @Before public void updatePoolSizeTemporarily() {
        LogPool.POOL_SIZE = 5;
        assertEquals(5,LogPool.POOL_SIZE);
    }

    @After public void recoverPoolSize() {
        LogPool.POOL_SIZE = 64;
        assertEquals(64,LogPool.POOL_SIZE);
    }*/

    @Test public void testTakeLogFromPool() {

       /* synchronized (this) {
            Log log = LogPool.take();
            assertEquals(null, log.log);
            assertEquals(0, LogPool.size);

            log.log = "1";
            LogPool.recycle(log);
            assertEquals(1, LogPool.size);

            Log log2 = LogPool.take();
            assertEquals(null, log2.log);
            assertEquals(0, LogPool.size);
        }*/

    }


    @Test public void testTakeLogConcurrently() throws InterruptedException {

           /* final Log[] log1 = new Log[1];
            final Log[] log2 = new Log[1];
            Thread t1 = new Thread(() -> log1[0] = LogPool.take());

            Thread t2 = new Thread(() -> log2[0] = LogPool.take());

            t1.start();
            t2.start();
            t1.join();
            t2.join();

            assertEquals(0, LogPool.size);
            LogPool.recycle(log1[0]);
            assertEquals(1, LogPool.size);
            LogPool.recycle(log2[0]);
            assertEquals(2, LogPool.size);

            LogPool.take();
            assertEquals(1, LogPool.size);*/

    }

    @Test public void testPoolSizeLimitation() {

        synchronized (this) {

            Log log1, log2, log3, log4, log5, log6;
            log1 = LogPool.take();
            log2 = LogPool.take();
            log3 = LogPool.take();
            log4 = LogPool.take();
            log5 = LogPool.take();
            log6 = LogPool.take();
            assertEquals(0, LogPool.size);

            LogPool.recycle(log1);
            assertEquals(1, LogPool.size);
            LogPool.recycle(log2);
            assertEquals(2, LogPool.size);
            LogPool.recycle(log3);
            assertEquals(3, LogPool.size);
            LogPool.recycle(log4);
            assertEquals(4, LogPool.size);
            LogPool.recycle(log5);
            assertEquals(5, LogPool.size);
            LogPool.recycle(log6);
            assertEquals(6, LogPool.size);
        }

    }




}
