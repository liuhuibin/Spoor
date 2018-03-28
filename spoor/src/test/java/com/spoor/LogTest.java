package com.spoor;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThat;

/**
 * Desc:
 * Author:Created by huibin
 * Date: Created on 18/3/26 13:46
 */

public class LogTest {




    @Test
    public void testFormatLog() {

        Log log1 = new Log(LogLevel.INFO,System.nanoTime(),"test format a log test format",Util.getCodeLoc(2));
        Log log2 = new Log(LogLevel.INFO,System.currentTimeMillis(),"test format a log test format test format a log test form",Util.getCodeLoc(2));
        Log log3 = new Log(LogLevel.INFO,System.currentTimeMillis(),"test format a log test format test format a log test forma",Util.getCodeLoc(2));
        Log log4 = new Log(LogLevel.DEBUG,System.currentTimeMillis(),"test format a log test format test format a log test format test format a log test format",Util.getCodeLoc(2));
        //watch the output in console something like "D>18/03/26 14:31:36[test format a log](LogTest.java line:19)"
        //don not use assertEquals cause when we do the UT time cannot be same
        System.out.println(log1.toString());
        System.out.println(log2.toString());
        System.out.println(log3.toString());
        System.out.println(log4.toString());
//        assertEquals("D>18/03/26 14:28:47[test format a log](LogTest.java line:21)",log.toString());
    }

    @Test
    public void testHashCode() {

        Log log1 = new Log(LogLevel.INFO,System.nanoTime(),"test format a log test format",Util.getCodeLoc(2)); Log log2 = new Log(LogLevel.INFO,System.nanoTime(),"test format a log test format",Util.getCodeLoc(2));
        assertNotEquals(log1.hashCode(),log2.hashCode());

       //Don't use System.currentTimeMillis(),we prefer to use System.nanoTime()
        Log log3 = new Log(LogLevel.INFO,System.currentTimeMillis(),"test format a log test format",Util.getCodeLoc(2)); Log log4 = new Log(LogLevel.INFO,System.currentTimeMillis(),"test format a log test format",Util.getCodeLoc(2));
        assertEquals(log3.hashCode(),log4.hashCode());
    }

    @Test
    public void testLogSort() throws InterruptedException {

        Log log1 = new Log(LogLevel.INFO,System.nanoTime(),"test format a log test format",Util.getCodeLoc(2));TimeUnit.MILLISECONDS.sleep(10);
        Log log2 = new Log(LogLevel.INFO,System.nanoTime(),"test format a log test format test format a log test form",Util.getCodeLoc(2));TimeUnit.MILLISECONDS.sleep(10);
        Log log3 = new Log(LogLevel.INFO,System.nanoTime(),"test format a log test format test format a log test forma",Util.getCodeLoc(2));TimeUnit.MILLISECONDS.sleep(10);
        Log log4 = new Log(LogLevel.DEBUG,System.nanoTime(),"test format a log test format test format a log test format test format a log test format",Util.getCodeLoc(2));TimeUnit.MILLISECONDS.sleep(10);

        List<Log> originalList = new ArrayList<>();
        originalList.add(log2);
        originalList.add(log4);
        originalList.add(log1);
        originalList.add(log3);

        System.out.println("Original:");
        System.out.println(originalList.toString());

        List<Log> sortedList = new ArrayList<>(originalList);
        Collections.sort(sortedList);
        System.out.println("Sorted:");
        System.out.println(sortedList.toString());

        List<Log> expectList = Arrays.asList(log1, log2, log3, log4);
        assertThat(sortedList, is(expectList));



    }

}
