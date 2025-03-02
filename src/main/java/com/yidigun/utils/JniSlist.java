package com.yidigun.utils;

import java.lang.ref.Cleaner;

final class JniSlist implements SimpleLinkedList {
    private static final Cleaner cleaner = Cleaner.create();
    private final Cleaner.Cleanable cleanable;

    /**
     * finalizer is deprecated from java 9.
     *
     * protected void finalize() {
     *     close();
     * }
     */
    private static class NativeObjCleanable implements Runnable {
        private final long pointer;
        private boolean cleaned = false;

        public NativeObjCleanable(long pointer) {
            this.pointer = pointer;
        }

        public void setCleaned() { this.cleaned = true; }

        @Override
        public void run() {
            if (pointer != 0L && !cleaned)
                destroy(pointer);
        }
    }

    static {
        System.loadLibrary("jni_slist");
    }

    private long pointer;
    private final NativeObjCleanable nativeObj;

    /*
     * JNI methods
     */

    private static native long create();
    private static native void destroy(long pointer);
    private static native void add(long pointer, int value);
    private static native void addAll(long pointer, int[] values);
    private static native void insertAt(long pointer, int index, int value);
    private static native void removeAt(long pointer, int index);
    private static native void removeAll(long pointer);
    private static native int size(long pointer);
    private static native int get(long pointer, int index);
    private static native boolean contains(long pointer, int value);

    JniSlist() {
        pointer = create();
        if (pointer == 0L)
            throw new IllegalStateException();
        nativeObj = new NativeObjCleanable(pointer);
        cleanable = cleaner.register(this, nativeObj);
    }

    @Override
    public void close() {
        if (pointer != 0L) {
            destroy(pointer);
            nativeObj.setCleaned();
            pointer = 0L;
        }
    }

    @Override
    public void add(int value) {
        if (pointer == 0L)
            throw new IllegalStateException();
        add(pointer, value);
    }

    @Override
    public void addAll(int... values) {
        if (pointer == 0L)
            throw new IllegalStateException();
        addAll(pointer, values);
    }

    @Override
    public void insertAt(int index, int value) {
        if (pointer == 0L)
            throw new IllegalStateException();
        insertAt(pointer, index, value);
    }

    @Override
    public void removeAt(int index) {
        if (pointer == 0L)
            throw new IllegalStateException();
        removeAt(pointer, index);
    }

    @Override
    public void removeAll() {
        if (pointer == 0L)
            throw new IllegalStateException();
        removeAll(pointer);
    }

    @Override
    public int size() {
        if (pointer == 0L)
            throw new IllegalStateException();
        return size(pointer);
    }

    @Override
    public int get(int index) {
        if (pointer == 0L)
            throw new IllegalStateException();
        return get(pointer, index);
    }

    @Override
    public boolean contains(int value) {
        if (pointer == 0L)
            throw new IllegalStateException();
        return contains(pointer, value);
    }
}
