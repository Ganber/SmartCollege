package com.example.smartcollege;

public class DisplayDevices implements Runnable {
    private final int tenSecondsInMili = 10000;
    private Runnable listener;
    private boolean stop = false;
    public DisplayDevices(Runnable listener){
        this.listener = listener;
    }

    @Override
    public void run() {
        while(!stop){
            try{
                listener.run();
                Thread.sleep(tenSecondsInMili);
            }
            catch (Exception e){

            }
        }
    }

    public void Stop(){
        stop = true;
    }
}
