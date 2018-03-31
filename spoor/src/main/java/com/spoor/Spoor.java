package com.spoor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import okio.BufferedSink;
import okio.Okio;

import static com.spoor.LogLevel.DEBUG;
import static com.spoor.LogLevel.ERROR;
import static com.spoor.LogLevel.INFO;
import static com.spoor.LogLevel.WARNING;

/**
 * <p>Essential APIs for working with Spoor.
 * Spoor is a mini android log system ,which save logs to local file.
 * Its memory usage is low and spoor has good performance in multi-thread.
 *
 *<p>Spoor's IO is based on Okio.</p>
 *
 * <p>Author:Created by huibin</p>
 * <p>Date: Created on 18/3/26 13:08</p>
 */

public final class Spoor {


    private static final String DEFAULT_LOG_DIRECTORY_NAME = "Spoor";
    private static final String DEFAULT_LOG_FILE_NAME = "spoor_log.txt";
    private static AtomicBoolean isInit = new AtomicBoolean(false);
    private static LogLevel minLevel = LogLevel.INFO;
    private File logFile;
    private BufferedSink bufferedSink;
    private static BlockingQueue<Log> logsQueue;
    private static ThreadPoolExecutor logExecutor;

    private String appLabel;
    private String versionName;
    private int versionCode;


    private Spoor(@NonNull Context ctx, @NonNull String logFileName) {
        logFile = new File(ctx.getExternalFilesDir(DEFAULT_LOG_DIRECTORY_NAME),logFileName);
        logsQueue = new LinkedBlockingQueue<>();
        logExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60, TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(), Util.threadFactory("Spoor Log Executor", false));

        appLabel = Util.getAppName(ctx);
        versionName = Util.getVersionName(ctx);
        versionCode = Util.getVersionNo(ctx);
    }


    public static void setMinLevel(@Nullable LogLevel minLevel) {
        //if minLevel is null, all level's log will be saved
        Spoor.minLevel = minLevel;
    }

    private static Spoor sInstance;

    public static Spoor getInstance() {

        if (!isInit.get()) throw new IllegalStateException("You must call Spoor#init first");
        return sInstance;
    }


    public static synchronized void init(Context ctx) {

        if (!isInit.get()) {
            if (sInstance==null) {
                sInstance = new Spoor(ctx,DEFAULT_LOG_FILE_NAME);
            }
            sInstance.startLogService();
            isInit.compareAndSet(false,true);
        }

    }

    private void startLogService() {

        if (logFile!=null) {

            if (!logFile.exists()) {
                try {
                    logFile.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                bufferedSink = Okio.buffer(Okio.appendingSink(logFile));
                initStartLog();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Log log = logsQueue.take();
                        if (bufferedSink!=null) {
                            bufferedSink.writeUtf8(log.toString());
                            bufferedSink.flush();
                        }
                        LogPool.recycle(log);

                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } catch (Exception ignore) {}
                }
            }
        }).start();

    }

    public void closeLogService() {
        if (logExecutor!=null && !logExecutor.isShutdown()) {
            logExecutor.shutdown();
        }
    }

    /**
     * save a log
     * @param level 日志的 Level
     * @param log  需要记录的日志
     */
    public void log(@NonNull LogLevel level, @NonNull String log) {
        if (Util.isEmpty(log)) return;
        switch (level) {
            case INFO:
                i(log,3); break;
            case DEBUG:
                d(log,3); break;
            case WARNING:
                w(log,3); break;
            case ERROR:
                e(log,3); break;
            default:
                    i(log); break; //never arrive here

        }
    }

    public void i(@NonNull String logContent) {
        i(logContent,3);
    }

    public void d(@NonNull String logContent) {
        d(logContent,3);
    }

    public void w(@NonNull String logContent) {
        w(logContent,3);
    }

    public void e(@NonNull String logContent) {
        e(logContent,3);
    }

    private void i(@NonNull String logContent, int level) {
        if (Util.isEmpty(logContent)) return;
        if (minLevel!=null && INFO.compareTo(minLevel) < 0)  return;
        Log log = LogPool.take();
        log.fillLogInfo(INFO, System.currentTimeMillis(), logContent,Util.getCodeLoc(level));
        logExecutor.submit(new LogTask(log));
    }



    private void d(@NonNull String logContent, int level) {
        if (Util.isEmpty(logContent)) return;
        if (minLevel!=null && DEBUG.compareTo(minLevel) < 0)  return;
        Log log = LogPool.take();
        log.fillLogInfo(DEBUG, System.currentTimeMillis(), logContent,Util.getCodeLoc(level));
        logExecutor.submit(new LogTask(log));
    }


    private void w(@NonNull String logContent, int level) {
        if (Util.isEmpty(logContent)) return;
        if (minLevel!=null && WARNING.compareTo(minLevel) < 0)  return;
        Log log = LogPool.take();
        log.fillLogInfo(WARNING, System.currentTimeMillis(), logContent,Util.getCodeLoc(level));
        logExecutor.submit(new LogTask(log));
    }



    private void e(@NonNull String logContent, int level) {
        if (Util.isEmpty(logContent)) return;
        if (minLevel!=null && ERROR.compareTo(minLevel) < 0)  return;
        Log log = LogPool.take();
        log.fillLogInfo(ERROR, System.currentTimeMillis(), logContent,Util.getCodeLoc(level));
        logExecutor.submit(new LogTask(log));
    }



    /**
     * log a init info something like:
     * <pre> {@code
     * ----------------------------------------
     * Spoor start at 18/3/27 16:43:28
     * Name:SpoorDemo Version:1.0.0 Code:102
     * https://github.com/liuhuibin/Spoor
     * ----------------------------------------
     *  }
     *  </pre>
     */
    private void initStartLog() {
        try {
            bufferedSink.writeUtf8("---------------------------------------------------------------------------\n");
            bufferedSink.writeUtf8("Spoor start at " + new SimpleDateFormat("yy/MM/dd HH:mm:ss").format(System.currentTimeMillis())+ "\n");
            bufferedSink.writeUtf8("Name:" + appLabel + "  Version:" + versionName + " Code:" + versionCode + "\n");
            bufferedSink.writeUtf8("https://github.com/liuhuibin/Spoor\n");
            bufferedSink.writeUtf8("---------------------------------------------------------------------------\n");
            bufferedSink.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private class LogTask implements Runnable{

        private Log log;

        public LogTask(Log log) {
            this.log = log;
        }

        @Override
        public void run() {
            logsQueue.add(log);
        }
    }


}
