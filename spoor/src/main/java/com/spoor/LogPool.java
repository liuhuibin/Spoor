package com.spoor;

import android.support.annotation.Nullable;

/**
 * Desc: A collection of unused logs, necessary to avoid GC churn and zero-fill.
 * This pool is a thread-safe static singleton.
 *
 * Author:Created by huibin
 * Date: Created on 18/3/26 16:30
 */
final class LogPool {

    /** The maximum number of logs in pool. */
    static final int POOL_SIZE = 64;
    static int size;

    /** Singly-linked list of segments. */
    static @Nullable Log next;

    private LogPool() {}

    static synchronized Log take() {

        synchronized (LogPool.class) {
            if (next!=null) {
                Log result = next;
                next = result.next;
                result.next = null;
                size--;
                return result;
            }
        }
        return new Log();
    }

    static  void recycle(Log log) {
        if (log.next!=null) throw new IllegalArgumentException();
        synchronized (LogPool.class) {
            if (size == POOL_SIZE) return; //pool is full
            size++;
            log.clear();
            log.next = next;
            next = log;
        }
    }
}
