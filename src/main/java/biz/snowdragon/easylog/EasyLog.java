package biz.snowdragon.easylog;


import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * EasyLog isn't your granddads logging framework.
 * It uses a stupidly simple api and uses a buffer and a separate thread for
 */
@SuppressWarnings("WeakerAccess")
public class EasyLog {
    private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    private Date currentDate = new Date();
    private final EasyLogWriterRunnable writerRunnable;
    private ConcurrentLinkedQueue<String> messageQueue;
    private final int bufferSize;
    private Thread workerThread;

    public EasyLog(String filePath, int bufferSize) throws IOException {
        messageQueue = new ConcurrentLinkedQueue<>();
        this.bufferSize = bufferSize;
        writerRunnable = new EasyLogWriterRunnable(messageQueue, filePath);

        workerThread = new Thread();
        workerThread.start();
    }

    /**
     * Adds a message with date to the logging queue.
     */
    public void logMessageWithTime(String message) {
        String dateString = getCurrentFormattedDate();
        logMessagePlain(String.format("%s - %s", dateString, message));
    }

    /**
     * Adds a message to the logging queue.
     */
    public void logMessagePlain(String message) {
        addMessage(message);
    }

    /**
     * Constructs a formatted message from the parameters with date.
     */
    public void logIrcMessage(String fromUser, String message) {
        String printMessage = String.format("%s: %s", fromUser, message);
        logMessageWithTime(printMessage);
    }

    /**
     * Flushes the current buffer to disk
     */
    public void flush() {
        synchronized (writerRunnable) {
            workerThread = new Thread(writerRunnable);
            workerThread.start();
        }
    }

    public String getFilePath() {
        return writerRunnable.getFilePath();
    }

    /**
     * Messages will be flushed when you set the file path.
     */
    public void setFilePath(String filePath) {
        flush();
        writerRunnable.setFilePath(filePath);
    }
    public int getQueueSize() {
        return messageQueue.size();
    }

    public int getBufferSize() {
        return bufferSize;
    }

    private void addMessage(String message) {
        messageQueue.add(message);

        if (getQueueSize() >= bufferSize) {
            flush();
        }
    }

    private String getCurrentFormattedDate() {
        long millis = System.currentTimeMillis();
        currentDate.setTime(millis);
        return timeFormat.format(currentDate);
    }


}
