package GUI.Utils;

import java.util.Timer;
import java.util.TimerTask;

public class TimerManager {
    private Timer timer;

    public TimerManager() {
        this.timer = new Timer();
    }

    public void setChangeRate(TimerTask task, long delay, long duration) {
        timer.scheduleAtFixedRate(task, delay, duration);
    }

    public void stopTimer() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
}
