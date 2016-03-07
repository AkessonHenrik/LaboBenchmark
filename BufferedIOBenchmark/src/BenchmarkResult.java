
enum IOStrategy {
    ByteByByteWithoutBufferedStream,
    ByteByByteWithBufferedStream,
    BlockByBlockWithoutBufferedStream,
    BlockByBlockWithBufferedStream
}

enum Action {
    WRITE,
    READ
}

public class BenchmarkResult {
    private IOStrategy strategy;
    private long time;
    private Action action;
    private long fileSize;
    private int blockSize;

    public BenchmarkResult(IOStrategy strategy, long time, Action action, long fileSize, int blockSize) {
        this.strategy = strategy;
        this.time = time;
        this.action = action;
        this.fileSize = fileSize;
        this.blockSize = blockSize;
    }


    public IOStrategy getStrategy() {
        return strategy;
    }

    public long getTime() {
        return time;
    }

    public Action getAction() {
        return action;
    }

    public long getFileSize() {
        return fileSize;
    }

    public int getBlockSize() {
        return blockSize;
    }
}