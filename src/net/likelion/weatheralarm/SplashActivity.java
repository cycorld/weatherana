	package net.likelion.weatheralarm;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import android.app.Activity;
import android.content.Intent;

public class SplashActivity extends Activity {

        //how long until we go to the next activity
        protected int _splashTime = 4000; 

        private Thread splashTread;

        /** Called when the activity is first created.  */
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.activity_splash);

            final SplashActivity sPlashScreen = this; 

            // thread for displaying the SplashScreen
            splashTread = new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized(this){

                                //wait 3 sec
                                wait(_splashTime);
                        }

                    } catch(InterruptedException e) {}
                    finally {
                        finish();

                        //start a new activity
                        Intent i = new Intent();
                        i.setClass(sPlashScreen, MainActivity.class);
                                startActivity(i);

                    }
                }
            };

            splashTread.start();
        }

        //Function that will handle the touch
        @Override
        public boolean onTouchEvent(MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                synchronized(splashTread){
                        splashTread.notifyAll();
                }
            }
            return true;
        }

} 