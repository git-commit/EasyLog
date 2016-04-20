package biz.snowdragon.easylog;

import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedQueue;

class EasyLogWriterRunnable implements Runnable {
    private LogPrinter logPrinter;
    private ConcurrentLinkedQueue<String> messageQueue;
    private String filePath;

    public EasyLogWriterRunnable(ConcurrentLinkedQueue<String> messageQueue, String filePath) throws IOException {
        this.messageQueue = messageQueue;
        this.filePath = filePath;
    }

    @Override
    public void run() {
        try {
            logPrinter = new LogPrinter(filePath);
            flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            logPrinter.close();
        }
    }

    public String getFilePath() {
        return filePath;
    }

    public synchronized void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    private synchronized void flush() {
        synchronized (logPrinter) {
            String currentMessage = messageQueue.poll();
            while (currentMessage != null) {
                logPrinter.println(currentMessage);
                currentMessage = messageQueue.poll();
            }
        }
    }
}
