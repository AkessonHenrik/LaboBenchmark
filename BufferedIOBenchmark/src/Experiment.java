import util.Timer;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by Henrik on 07.03.2016.
 */

public class Experiment {

    static final Logger LOG = Logger.getLogger(Benchmarker.class.getName());
    final static String OUTPUT_FOLDER = "binOutput/";
    final static String FILENAME_PREFIX = "test-data"; // we will write and read test files at this location
    final static long NUMBER_OF_BYTES_TO_WRITE = 1024 * 1024 * 10; // we will write and read 10 MB files

    public static void main(String ... args) {

        System.setProperty("java.util.logging.SimpleFormatter.format", "%5$s %n");


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
        bufferedBlock500.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 500);
        bufferedBlock50.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 50);
        bufferedBlock5.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 5);
        bufferedByte.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 0);

        LOG.log(Level.INFO, "");
        LOG.log(Level.INFO, "*** BENCHMARKING WRITE OPERATIONS (without BufferedStream)", Timer.takeTime());
        nonbufferedBlock500.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 500);
        nonbufferedBlock50.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 50);
        nonbufferedBlock5.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 5);
        nonbufferedByte.produceTestData(NUMBER_OF_BYTES_TO_WRITE, 0);


        LOG.log(Level.INFO, "");
		LOG.log(Level.INFO, "*** BENCHMARKING READ OPERATIONS (with BufferedStream)", Timer.takeTime());
		bufferedBlock500.consumeTestData(500);
		bufferedBlock50.consumeTestData(50);
		bufferedBlock5.consumeTestData(5);
		bufferedByte.consumeTestData(0);

        LOG.log(Level.INFO, "");
        LOG.log(Level.INFO, "*** BENCHMARKING READ OPERATIONS (with BufferedStream)", Timer.takeTime());
        nonbufferedBlock500.consumeTestData(500);
        nonbufferedBlock50.consumeTestData(50);
        nonbufferedBlock5.consumeTestData(5);
        nonbufferedByte.consumeTestData(0);

    }
}
