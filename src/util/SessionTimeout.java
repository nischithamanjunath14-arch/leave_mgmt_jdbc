package util;

import javax.swing.*;
import java.awt.AWTEvent;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;

public class SessionTimeout {
    private final Timer   timer;
    private final int     TIMEOUT_MS = 5 * 60 * 1000; // 5 minutes
    private final Runnable onTimeout;

    public SessionTimeout(Runnable onTimeout) {
        this.onTimeout = onTimeout;
        timer = new Timer(TIMEOUT_MS, e -> triggerLogout());
        timer.setRepeats(false);
    }

    public void start()       { timer.start(); }
    public void stop()        { timer.stop();  }
    public void resetTimer()  { timer.restart(); }

    private void triggerLogout() {
        JOptionPane.showMessageDialog(null,
                "Your session has expired due to inactivity.\nPlease log in again.",
                "Session Timeout", JOptionPane.WARNING_MESSAGE);
        onTimeout.run();
    }

    public void attachTo(JFrame frame) {
        AWTEventListener listener = event -> resetTimer();
        Toolkit.getDefaultToolkit().addAWTEventListener(listener,
                AWTEvent.KEY_EVENT_MASK | AWTEvent.MOUSE_EVENT_MASK);
    }
}