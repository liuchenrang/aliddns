package org.xinghuo;

import java.util.concurrent.ThreadFactory;

/**
 * @author xinghuo
 */
public class DdnsThreadFactory implements ThreadFactory {
    private String name;
    private Integer counter;
    public DdnsThreadFactory(String name) {
        super();
        this.name = name;
        counter = 0;
    }

    /**
     * Constructs a new {@code Thread}.  Implementations may also initialize
     * priority, name, daemon status, {@code ThreadGroup}, etc.
     *
     * @param r a runnable to be executed by new thread instance
     * @return constructed thread, or {@code null} if the request to
     * create a thread is rejected
     */
    @Override
    public Thread newThread(Runnable r) {
        Thread th = new Thread(r, name + ":" + counter);
        return th;
    }
}
