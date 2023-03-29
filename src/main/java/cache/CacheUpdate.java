package cache;

import common.message.MessageType;
import common.util.FileScanner;
import common.util.PropertyParser;
import service.BroadcastSenderService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CacheUpdate implements Runnable {
    private final ScheduledExecutorService executor;

    public CacheUpdate() {
        this.executor = Executors.newSingleThreadScheduledExecutor(runnable -> {
            Thread thread = new Thread(runnable);
            thread.setDaemon(true);
            return thread;
        });
    }

    public void start() {
        executor.scheduleAtFixedRate(this, 0, 5, TimeUnit.SECONDS);
    }

    @Override
    public void run() {
        Cache.add(Cache.localHost, FileScanner.getAllFiles(PropertyParser.getShareRoot()));
        try {
            BroadcastSenderService.sendMessage(MessageType.HEART_BEAT_REQ);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        executor.shutdown();
    }

}
