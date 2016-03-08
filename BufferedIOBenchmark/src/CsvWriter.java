/**
 * Created by Henrik Akesson
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CsvWriter {

    private PrintWriter pw;

    public CsvWriter(String fileName) throws FileNotFoundException {
        pw = new PrintWriter(new File(fileName + ".csv"));
        pw.println("tcsoperation,strategy,blockSize,fileSizeInBytes,durationInMs");
        pw.flush();
    }
    public void writeResults(BenchmarkResult benchmarkResult) {
        String toWrite = benchmarkResult.getAction() + ","
                + benchmarkResult.getStrategy() + ","
                + benchmarkResult.getBlockSize() + ","
                + benchmarkResult.getFileSize() + ","
                + benchmarkResult.getTime();
        pw.println(toWrite);
        pw.flush();
    }
}
