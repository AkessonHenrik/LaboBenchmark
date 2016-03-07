import java.io.PrintWriter;

/**
 * Created by Henrik on 07.03.2016.
 */
public class CsvWriter {
    private static CsvWriter writer = new CsvWriter();
    private PrintWriter pw;

    public static CsvWriter getInstance() {
        return writer;
    }
    public void writeResults(BenchmarkResult benchmarkResult, String fileName) {
        pw.println("tcsoperation,strategy,blockSize,fileSizeInBytes,durationInMs");
    }
}
