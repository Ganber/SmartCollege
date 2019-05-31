package com.example.smartcollege;

import android.util.Log;

public class DisplayDevices implements Runnable {
    private final int tenSecondsInMili = 10000;
    private Runnable listener;
    private boolean stop = false;
    public DisplayDevices(Runnable listener){
        this.listener = listener;
    }

    @Override
    public void run() {
        //refresh every 10 seconds the status on the devices in dashboard activity
        while(!stop){
            try{
                listener.run();
                Thread.sleep(tenSecondsInMili);
            }
            catch (Exception e){
                Log.d("DisplayDevices","Error during refresh operation");
            }
        }
    }

    public void Stop(){
        stop = true;
    }
}
