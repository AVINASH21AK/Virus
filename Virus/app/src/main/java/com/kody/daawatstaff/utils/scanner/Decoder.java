/*
 * MIT License
 *
 * Copyright (c) 2017 Yuriy Budiyev [yuriy.budiyev@yandex.ru]
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.kody.daawatstaff.utils.scanner;

import android.os.Process;

import com.google.zxing.*;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

final class Decoder {
    private final MultiFormatReader mReader;
    private final DecoderThread mDecoderThread;
    private final StateListener mStateListener;
    private final Map<DecodeHintType, Object> mHints;
    private final Object mTaskLock = new Object();
    private volatile DecodeCallback mCallback;
    private volatile DecodeTask mTask;
    private volatile State mState;

    public Decoder(@androidx.annotation.NonNull final StateListener stateListener,
                   @androidx.annotation.NonNull final List<BarcodeFormat> formats, @androidx.annotation.Nullable final DecodeCallback callback) {
        mReader = new MultiFormatReader();
        mDecoderThread = new DecoderThread();
        mHints = new EnumMap<>(DecodeHintType.class);
        mHints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        mReader.setHints(mHints);
        mCallback = callback;
        mStateListener = stateListener;
        mState = State.INITIALIZED;
    }

    public void setFormats(@androidx.annotation.NonNull final List<BarcodeFormat> formats) {
        mHints.put(DecodeHintType.POSSIBLE_FORMATS, formats);
        mReader.setHints(mHints);
    }

    public void setCallback(@androidx.annotation.Nullable final DecodeCallback callback) {
        mCallback = callback;
    }

    public void decode(@androidx.annotation.NonNull final DecodeTask task) {
        synchronized (mTaskLock) {
            if (mState != State.STOPPED) {
                mTask = task;
                mTaskLock.notify();
            }
        }
    }

    public void start() {
        if (mState != State.INITIALIZED) {
            throw new IllegalStateException("Illegal decoder state");
        }
        mDecoderThread.start();
    }

    public void shutdown() {
        mDecoderThread.interrupt();
        mTask = null;
    }

    @androidx.annotation.NonNull
    public State getState() {
        return mState;
    }

    private boolean setState(@androidx.annotation.NonNull final State state) {
        mState = state;
        return mStateListener.onStateChanged(state);
    }

    private final class DecoderThread extends Thread {
        public DecoderThread() {
            super("cs-decoder");
        }

        @Override
        public void run() {
            Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
            mainLoop:
            for (; ; ) {
                setState(Decoder.State.IDLE);
                Result result = null;
                try {
                    final DecodeTask task;
                    for (; ; ) {
                        synchronized (mTaskLock) {
                            final DecodeTask t = mTask;
                            if (t != null) {
                                mTask = null;
                                task = t;
                                break;
                            }
                            try {
                                mTaskLock.wait();
                            } catch (final InterruptedException e) {
                                setState(Decoder.State.STOPPED);
                                break mainLoop;
                            }
                        }
                    }
                    setState(Decoder.State.DECODING);
                    result = task.decode(mReader);
                } catch (final ReaderException ignored) {
                } finally {
                    if (result != null) {
                        mTask = null;
                        if (setState(Decoder.State.DECODED)) {
                            final DecodeCallback callback = mCallback;
                            if (callback != null) {
                                callback.onDecoded(result);
                            }
                        }
                    }
                }
            }
        }
    }

    public interface StateListener {
        boolean onStateChanged(@androidx.annotation.NonNull State state);
    }

    public enum State {
        INITIALIZED,
        IDLE,
        DECODING,
        DECODED,
        STOPPED
    }
}
