import util.Timer;

import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class Benchmarker {
    static final Logger LOG = Logger.getLogger(Benchmarker.class.getName());
    protected IOStrategy strategy;
    protected OutputStream out = null;
    protected InputStream in = null;
    protected String fileName;

    protected Benchmarker(IOStrategy strategy, String OUTPUT_FOLDER, String FILENAME_PREFIX, int blockSize) {
        this.strategy = strategy;
        this.fileName = OUTPUT_FOLDER + FILENAME_PREFIX + "-" + strategy + "-" + blockSize + ".bin";
        try {
            out = new FileOutputStream(fileName);
            in = new FileInputStream(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    protected BenchmarkResult produceTestData(long numberOfBytesToWrite, int blockSize) {
        LOG.log(Level.INFO, "Generating test data ({0}, {1} bytes, block size: {2}...", new Object[]{strategy, numberOfBytesToWrite, blockSize});
        Timer.start();
        try {
            // Now, let's call the method that does the actual work and produces bytes on the stream
            produceDataToStream(numberOfBytesToWrite, blockSize);

            // We are done, so we only have to close the output stream
            out.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        long timeTaken = Timer.takeTime();
        LOG.log(Level.INFO, "  > Done in {0} ms.", timeTaken);
        return new BenchmarkResult(strategy, timeTaken, Action.WRITE, numberOfBytesToWrite);
    }

    protected void produceDataToStream(long numberOfBytesToWrite, int blockSize) throws IOException {

    }

    protected BenchmarkResult consumeTestData(int blockSize) {
        LOG.log(Level.INFO, "Consuming test data ({0}, block size: {1}...", new Object[]{strategy, blockSize});
        File f = new File(fileName);
        long size = f.length();
        Timer.start();

        try {
            // Now, let's call the method that does the actual work and produces bytes on the stream
            consumeDataFromStream(in, strategy, blockSize);

            // We are done, so we only have to close the input stream
            in.close();
        } catch (IOException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        long timeTaken = Timer.takeTime();
        LOG.log(Level.INFO, "  > Done in {0} ms.", timeTaken);
        return new BenchmarkResult(strategy, timeTaken, Action.READ, size);
    }

    protected void consumeDataFromStream(InputStream is, IOStrategy ioStrategy, int blockSize) throws IOException {
    }

}

abstract class BenchmarkerWithBufferedStream extends Benchmarker {
    protected BenchmarkerWithBufferedStream(IOStrategy strategy, String OUTPUT_FOLDER, String FILENAME_PREFIX, int blockSize) {
        super(strategy, OUTPUT_FOLDER, FILENAME_PREFIX, blockSize);
        out = new BufferedOutputStream(out);
        in = new BufferedInputStream(in);
    }
}

abstract class BenchmarkerWithoutBufferedStream extends Benchmarker {
    protected BenchmarkerWithoutBufferedStream(IOStrategy strategy, String OUTPUT_FOLDER, String FILENAME_PREFIX, int blockSize) {
        super(strategy, OUTPUT_FOLDER, FILENAME_PREFIX, blockSize);
    }
}

class BenchmarkerByteByByteWithoutBufferedStream extends BenchmarkerWithoutBufferedStream {
    protected BenchmarkerByteByByteWithoutBufferedStream(String OUTPUT_FOLDER, String FILENAME_PREFIX, int blockSize) {
        super(IOStrategy.ByteByByteWithoutBufferedStream, OUTPUT_FOLDER, FILENAME_PREFIX, blockSize);
    }

    public void produceDataToStream(long numberOfBytesToWrite, int blockSize) throws IOException {
        for (int i = 0; i < numberOfBytesToWrite; i++) {
            out.write('h');
        }
    }
    public void consumeDataFromStream(InputStream is, IOStrategy ioStrategy, int blockSize) throws IOException {
        int totalBytes = 0;
        int c;
        while ((c = is.read()) != -1) {
            // here, we could cast c to a byte and process it
            totalBytes++;
        }
        LOG.log(Level.INFO, "Number of bytes read: {0}", new Object[]{totalBytes});
    }

}

class BenchmarkerByteByByteWithBufferedStream extends BenchmarkerWithBufferedStream {
    protected BenchmarkerByteByByteWithBufferedStream(String OUTPUT_FOLDER, String FILENAME_PREFIX, int blockSize) {
        super(IOStrategy.ByteByByteWithBufferedStream, OUTPUT_FOLDER, FILENAME_PREFIX, blockSize);
    }

    public void produceDataToStream(long numberOfBytesToWrite, int blockSize) throws IOException {
        for (int i = 0; i < numberOfBytesToWrite; i++) {
            out.write('h');
        }
    }

    public void consumeDataFromStream(InputStream is, IOStrategy ioStrategy, int blockSize) throws IOException {
        int totalBytes = 0;
        int c;
        while ((c = is.read()) != -1) {
            // here, we could cast c to a byte and process it
            totalBytes++;
        }
        LOG.log(Level.INFO, "Number of bytes read: {0}", new Object[]{totalBytes});
    }

}

class BenchmarkerBlockByBlockWithoutBufferedStream extends BenchmarkerWithoutBufferedStream {
    protected BenchmarkerBlockByBlockWithoutBufferedStream(String OUTPUT_FOLDER, String FILENAME_PREFIX, int blockSize) {
        super(IOStrategy.BlockByBlockWithoutBufferedStream, OUTPUT_FOLDER, FILENAME_PREFIX, blockSize);
    }

    public void produceDataToStream(IOStrategy ioStrategy, long numberOfBytesToWrite, int blockSize) throws IOException {
        long remainder = numberOfBytesToWrite % blockSize;
        long numberOfBlocks = (numberOfBytesToWrite / blockSize);
        byte[] block = new byte[blockSize];

        for (int i = 0; i < numberOfBlocks; i++) {
            for (int j = 0; j < blockSize; j++) {
                block[j] = 'b';
            }
            out.write(block);
        }

        if (remainder != 0) {
            for (int j = 0; j < remainder; j++) {
                block[j] = 'B';
            }
            out.write(block, 0, (int) remainder);
        }
    }

    public void consumeDataFromStream(InputStream is, IOStrategy ioStrategy, int blockSize) throws IOException {
        int totalBytes = 0;
        byte[] block = new byte[blockSize];
        int bytesRead = 0;
        while ((bytesRead = in.read(block)) != -1) {
            // here, we can process bytes block[0..bytesRead]
            totalBytes += bytesRead;
        }


        LOG.log(Level.INFO, "Number of bytes read: {0}", new Object[]{totalBytes});
    }

}

class BenchmarkerBlockByBlockWithBufferedStream extends BenchmarkerWithBufferedStream {
    protected BenchmarkerBlockByBlockWithBufferedStream(String OUTPUT_FOLDER, String FILENAME_PREFIX, int blockSize) {
        super(IOStrategy.BlockByBlockWithBufferedStream, OUTPUT_FOLDER, FILENAME_PREFIX, blockSize);
    }

    public void produceDataToStream(long numberOfBytesToWrite, int blockSize) throws IOException {
        long remainder = numberOfBytesToWrite % blockSize;
        long numberOfBlocks = (numberOfBytesToWrite / blockSize);
        byte[] block = new byte[blockSize];

        for (int i = 0; i < numberOfBlocks; i++) {
            for (int j = 0; j < blockSize; j++) {
                block[j] = 'b';
            }
            out.write(block);
        }

        if (remainder != 0) {
            for (int j = 0; j < remainder; j++) {
                block[j] = 'B';
            }
            out.write(block, 0, (int) remainder);
        }
        out.close();
    }

    public void consumeDataFromStream(InputStream is, IOStrategy IOstrategy, int blockSize) throws IOException {
        int totalBytes = 0;
        byte[] block = new byte[blockSize];
        int bytesRead = 0;
        while ((bytesRead = is.read(block)) != -1) {
            // here, we can process bytes block[0..bytesRead]
            totalBytes += bytesRead;
        }


        LOG.log(Level.INFO, "Number of bytes read: {0}", new Object[]{totalBytes});
    }
}