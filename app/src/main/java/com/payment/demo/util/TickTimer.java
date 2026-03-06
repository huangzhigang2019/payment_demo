package com.payment.demo.util;

import android.os.CountDownTimer;

/**
 * 简单倒计时，用于 EMV 设备层。
 */
public class TickTimer {
    public static final int DEFAULT_TIMEOUT = 60;
    private Timer timer;

    public interface OnTimerTickListener {
        void onTick(long leftTime);
    }

    public interface OnTimerFinishListener {
        void onFinish();
    }

    private static class Timer extends CountDownTimer {
        private OnTimerTickListener tickListener;
        private OnTimerFinishListener finishListener;

        Timer(long timeout, long tickInterval) {
            super(timeout * 1000, tickInterval * 1000);
        }

        void registerTickListener(OnTimerTickListener l) { this.tickListener = l; }
        void registerFinishListener(OnTimerFinishListener l) { this.finishListener = l; }

        @Override
        public void onFinish() {
            if (finishListener != null) finishListener.onFinish();
        }

        @Override
        public void onTick(long millisUntilFinished) {
            if (tickListener != null) tickListener.onTick(millisUntilFinished / 1000);
        }
    }

    public void start(int timeout, OnTimerTickListener tickListener, OnTimerFinishListener finishListener) {
        if (timer != null) timer.cancel();
        timer = new Timer(timeout, 1);
        timer.registerFinishListener(finishListener);
        timer.registerTickListener(tickListener);
        timer.start();
    }

    public void stop() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
