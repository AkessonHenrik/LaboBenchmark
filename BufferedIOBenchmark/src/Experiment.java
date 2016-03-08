/**
 * Created by Henrik Akesson
 */

import util.Timer;

import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Experiment {

    static final Logger LOG = Logger.getLogger(Benchmarker.class.getName());
    final static String OUTPUT_FOLDER = "binOutput/";
    final static String FILENAME_PREFIX = "test-data"; // we will write and read test files at this location
    final static long NUMBER_OF_BYTES_TO_WRITE = 1024 * 1024 * 10; // we will write and read 10 MB files

    public static void main(String ... args) {

        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");

        BenchmarkResult result;
        CsvWriter csvWriter = null;
        try {
            csvWriter = new CsvWriter("results");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //Buffered Stream Benchmarkers
		Benchmarker bufferedBlock500 = new BenchmarkerBlockByBlockWithBufferedStream(OUTPUT_FOLDER, FILENAME_PREFIX, 500);
		Benchmarker bufferedBlock50 = new BenchmarkerBlockByBlockWithBufferedStream(OUTPUT_FOLDER, FILENAME_PREFIX, 50);
		Benchmarker bufferedBlock5 = new BenchmarkerBlockByBlockWithBufferedStream(OUTPUT_FOLDER, FILENAME_PREFIX, 5);
        Benchmarker bufferedByte = new BenchmarkerByteByByteWithBufferedStream(OUTPUT_FOLDER, FILENAME_PREFIX, 0);

        //Non-buffered Stream Benchmarkers
        Benchmarker nonbufferedBlock500 = new BenchmarkerBlockByBlockWithoutBufferedStream(OUTPUT_FOLDER, FILENAME_PREFIX, 500);
        Benchmarker nonbufferedBlock50 = new BenchmarkerBlockByBlockWithoutBufferedStream(OUTPUT_FOLDER, FILENAME_PREFIX, 50);
        Benchmarker nonbufferedBlock5 = new BenchmarkerBlockByBlockWithoutBufferedStream(OUTPUT_FOLDER, FILENAME_PREFIX, 5);
        Benchmarker nonbufferedByte = new BenchmarkerByteByByteWithoutBufferedStream(OUTPUT_FOLDER, FILENAME_PREFIX, 0);

		LOG.log(Level.INFO, "");
		LOG.log(Level.INFO, "*** BENCHMARKING WRITE OPERATIONS (with BufferedStream)", Timer.takeTime());
        result = bufferedBlock500.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 500);
        csvWriter.writeResults(result);
        result = bufferedBlock50.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 50);
        csvWriter.writeResults(result);
        result = bufferedBlock5.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 5);
        csvWriter.writeResults(result);
        result = bufferedByte.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 0);
        csvWriter.writeResults(result);

        LOG.log(Level.INFO, "");
        LOG.log(Level.INFO, "*** BENCHMARKING WRITE OPERATIONS (without BufferedStream)", Timer.takeTime());
        result = nonbufferedBlock500.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 500);
        csvWriter.writeResults(result);
        result = nonbufferedBlock50.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 50);
        csvWriter.writeResults(result);
        result = nonbufferedBlock5.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 5);
        csvWriter.writeResults(result);
        result = nonbufferedByte.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 0);
        csvWriter.writeResults(result);


        LOG.log(Level.INFO, "");
		LOG.log(Level.INFO, "*** BENCHMARKING READ OPERATIONS (with BufferedStream)", Timer.takeTime());
		result = bufferedBlock500.consumeTestData(500);
        csvWriter.writeResults(result);
        result = bufferedBlock50.consumeTestData(50);
        csvWriter.writeResults(result);
        result = bufferedBlock5.consumeTestData(5);
        csvWriter.writeResults(result);
        result = bufferedByte.consumeTestData(0);
        csvWriter.writeResults(result);

        LOG.log(Level.INFO, "");
        LOG.log(Level.INFO, "*** BENCHMARKING READ OPERATIONS (with BufferedStream)", Timer.takeTime());
        result = nonbufferedBlock500.consumeTestData(500);
        csvWriter.writeResults(result);
        result = nonbufferedBlock50.consumeTestData(50);
        csvWriter.writeResults(result);
        result = nonbufferedBlock5.consumeTestData(5);
        csvWriter.writeResults(result);
        result = nonbufferedByte.consumeTestData(0);
        csvWriter.writeResults(result);

    }
}
