package com.spoor;

import org.junit.Test;

import static com.spoor.LogLevel.INFO;
import static com.spoor.LogLevel.DEBUG;
import static com.spoor.LogLevel.WARNING;
import static com.spoor.LogLevel.ERROR;
import static org.junit.Assert.*;

/**
 * Desc:
 * Author:Created by huibin
 * Date: Created on 18/3/26 14:07
 */

public class LogLevelTest {

    @Test
    public void testToString() {
        assertEquals("I",INFO.toString());
        assertEquals("D",DEBUG.toString());
        assertEquals("W",WARNING.toString());
        assertEquals("E",ERROR.toString());
    }

    @Test
    public void testEnumEquals() {
        assertTrue(LogLevel.DEBUG==LogLevel.DEBUG);
        assertFalse(LogLevel.DEBUG==LogLevel.ERROR);
    }

    @Test
    public void testCompare() {
        assertTrue(DEBUG.compareTo(INFO) > 0);
        assertTrue(INFO.compareTo(WARNING) < 0);
        assertTrue(DEBUG.compareTo(DEBUG) == 0);
        assertFalse(DEBUG.compareTo(ERROR) > 0);
    }

}
