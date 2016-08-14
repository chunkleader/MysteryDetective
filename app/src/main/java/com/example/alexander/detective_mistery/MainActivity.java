package com.example.alexander.detective_mistery;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity {


    public String misterXPos;
    public String detectivePos;
    public TextView result;
    public EditText misterXInput;
    public EditText detectiveInput;
    public Thread thread;
    public boolean playmusic;
    public AndroidAudioDevice device = new AndroidAudioDevice();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = (TextView) findViewById(R.id.textView3);
        misterXInput = (EditText) findViewById(R.id.textView1);
        detectiveInput = (EditText) findViewById(R.id.textView2);


        final Button detective = (Button) findViewById(R.id.button);
        detective.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                misterXPos = misterXInput.getText().toString();
                misterXInput.setText("");
                showResult();
            }
        });

        final Button misterx = (Button) findViewById(R.id.button2);
        misterx.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                detectivePos = detectiveInput.getText().toString();
                detectiveInput.setText("");
                showResult();
            }
        });
        final Button reset = (Button) findViewById(R.id.button3);
        reset.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                reset();
            }
        });
            thread = new Thread(new Runnable() {
            public void run() {
                final float frequency = 300;
                float increment = (float) (2 * Math.PI) * frequency / 44100; // angular increment for each sample
                float angle = 0;
                float samples[] = new float[1024];
                boolean slept = false;
                long t = 0;
                boolean firstCycle = true;

                while (true) {                                                              //hier wird der Ton ausgegeben 2 Sekunden lang ausgegeben, allerdings als beep-Ton, da es unterbrochen wird
                    firstCycle = true;
                    do{
                        slept = false;
                        if(firstCycle) {
                            t = System.nanoTime();
                            firstCycle = false;
                        }
                        for (int i = 0; i < samples.length; i++) {
                            samples[i] = (float)Math.signum(Math.sin( angle ));
                            angle += increment;
                        }
                        device.writeSamples(samples);
                        if(t+2000000000 < System.nanoTime()){
                            slept=true;
                        }
                    }while(slept == false);                                                  //hier ist der Unterbrecher für 2 Sekunden
                    firstCycle = true;
                    do{
                        slept = false;
                        if(firstCycle) {
                            t = System.nanoTime();
                                firstCycle = false;
                            }
                        for (int i = 0; i < samples.length; i++) {
                            samples[i] = 0;
                        }
                        device.writeSamples(samples);
                        if(t+2000000000 < System.nanoTime()){
                            slept=true;
                        }
                    }while(slept == false);
                }
            }
        });
    }
    public void showResult(){
        if((misterXPos != null && !misterXPos.isEmpty()) && (detectivePos != null && !detectivePos.isEmpty())) {
            int MisterX = Integer.parseInt(misterXPos);
            int detective = Integer.parseInt(detectivePos);
            if (MisterX == detective) {
                result.setText("MisterX wurde überführt!!!");
                beep();

            }
        }
    }
    public void beep(){
        thread.start();
    }
    public void reset(){
        detectiveInput.setText("");
        misterXInput.setText("");
        misterXPos = "";
        detectivePos="";
        device.track.stop();
    }


}
