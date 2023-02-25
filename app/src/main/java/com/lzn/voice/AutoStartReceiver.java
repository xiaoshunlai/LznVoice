package com.lzn.voice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.lzn.voice.methed.Run_Methed;
import static com.lzn.voice.util.Utils.TAG;

public class AutoStartReceiver extends BroadcastReceiver {

    private boolean mbStartVoice = false;

    private Run_Methed run_Metned = new Run_Methed();
    private Thread thread = null;

    @Override
    public void onReceive(Context context, Intent intent) {
        if(null == intent) {
            Log.e(TAG, "voice receive 001 null!");
            return ;
        }
        String action = intent.getAction();
        Log.i(TAG, "voice receive action:" + action);

        if (action.equals(Intent.ACTION_BOOT_COMPLETED) ||
                action.equals("android.media.AUDIO_BECOMING_NOISY")) {
            try {
                if (!mbStartVoice) {
                    Thread thread = new Thread(run_Metned);
                    thread.setPriority(Thread.MAX_PRIORITY);
                    thread.start();
                    mbStartVoice = true;
                }
            } catch (Exception e) {
                Log.e(TAG, "voice receive ACTION_BOOT_COMPLETED!", e);
            }
        }
    }
}
