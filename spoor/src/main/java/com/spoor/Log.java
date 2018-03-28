package com.spoor;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;

/**
 * Desc: Entity of a log to save
 * Author:Created by huibin
 * Date: Created on 18/3/26 11:08
 */

final class Log implements Comparable<Log>{

    private static final int MAX_LENGTH = 60;

    LogLevel logLevel;
    long time;
    String log;
    String loc;

    /** Next log in a linked list. Do not to update this field in your code*/
    Log next;

    /** called by  LogPool*/
    public Log() {}

    /**
     * When construct a Log,we should use <strong>{@codeSystem.currentTimeMillis()}<strong/>to get the log time. But if
     * we use System.currentTimeMillis() when two logs written in same line and log contents are same,
     * then maybe the two logs will have same hashcode.Avoid to do that.
     */
    public Log(LogLevel logLevel, long time, String log, String loc) {
        this.logLevel = logLevel;
        this.time = time;
        this.log = log;
        this.loc = loc;
    }


    public void clear() {
        this.logLevel = null;
        this.time = 0;
        this.log = null;
        this.loc = null;
        this.next = null;
    }

    public void fillLogInfo(LogLevel logLevel, long time, String log, String loc) {
        this.logLevel = logLevel;
        this.time = time;
        this.log = log;
        this.loc = loc;
    }

    /**
     * <p>Format a spoor log and return a formatted string like:</p>
     * <pre>{@code
     *      //   W>2018/3/16 12:56:23 [This a log for test ...] (Log.java method:demo line:25)
     *      //   ^          ^                    ^                           ^
     *      // level       time                 log                       location
     * }<pre/>
     * <p>Note:content length of log will be at most 60 chars, otherwise it will be truncated<p/>
     * @return formatted log string
     */
    @Override
    public String toString() {

        StringBuffer logString = new StringBuffer();
        if(log.length()+3 > MAX_LENGTH)
            logString.append(log.substring(0,MAX_LENGTH-3)).append("...");
        else
            logString.append(log);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
        StringBuffer sb = new StringBuffer(logLevel.toString());
        sb.append(">")
                .append(dateFormat.format(time))
                .append("[")
                .append(logString)
                .append("]")
                .append("(")
                .append(loc)
                .append(")")
                .append("\n");//append a newline at the end can be more ergonomic to read by man


        return sb.toString();
    }

    @Override
    public int hashCode() {

        int result = 31;
        result += 17*logLevel.hashCode();
        result += 17*Long.valueOf(time).hashCode();
        result += 17*log.hashCode();
        result += 17*loc.hashCode();
        return result;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        return o instanceof Log
                && ((Log)o).logLevel == this.logLevel
                && ((Log)o).time == this.time
                && ((Log)o).log == this.log
                && ((Log)o).loc == this.loc;
    }

    @Override
    public int compareTo(@NonNull Log o) {
        if (this.time != o.time)
            return (int) (this.time - o.time);
        return  this.log.compareTo(o.log) ;
    }
}
