package biz.snowdragon.easylog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

class LogPrinter extends PrintWriter {

    LogPrinter(String filePath) throws IOException {
        super(parseFilePath(filePath));
    }

    private static FileOutputStream parseFilePath(String filePath) throws IOException {
        File f = new File(filePath);

        if (!f.exists()) {
            f.createNewFile();
        }

        return new FileOutputStream(f, true);
    }
}
