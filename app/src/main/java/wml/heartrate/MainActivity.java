package wml.heartrate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;
import android.view.View.OnClickListener;

import java.text.NumberFormat;
import java.text.DecimalFormat;

import java.text.Format;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnButton();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    ImageButton imageButton; Button resetButton;
    //    double tapCounter;
    int counter;
    boolean currentlyCounting;
    long[] times;

    public void addListenerOnButton() {

        imageButton = (ImageButton) findViewById(R.id.HeartButton);
        resetButton = (Button) findViewById(R.id.resetButton);
        final int beatsToConsider = 20;
        times = new long[beatsToConsider];
        currentlyCounting = false;
        counter = 0;


        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TextView box = (TextView) findViewById(R.id.RateCounter);
                counter = 0;
                currentlyCounting = false;
                box.setText("Thump Heart to Start");
                times = new long[beatsToConsider];
            }

        });


        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TextView box = (TextView) findViewById(R.id.RateCounter);
                long deltaTime;
                if (!currentlyCounting) { //starting counting
                        currentlyCounting = true;
                        times[(counter % times.length)] = System.currentTimeMillis();
                    counter++;
                    box.setText("Continue Thumping!");
                } else {
                    times[(counter % times.length)] = System.currentTimeMillis();
                    // array times is filled in order
                    if (counter >= times.length) {
                        deltaTime = (long)((times[(counter % times.length)] - times[((counter + 1) % times.length)]) * ((times.length +1.0)/times.length));
                        //deltaTime = (newest thump time - oldest thump time) * factor to account for not having 1 delta time for each recorded thump
                        // e.g. for 20 taps, there are only 19 periods of time between the taps, so extra period must be extrapolated for 20 periods
                    } else {
                        // array not full yet, compare first and last recorded time instead
                        deltaTime = (long)((times[counter] - times[0]) * (counter +1.0 )/counter);
                    }
                    // built in reset after 5 seconds of inactivity
                    if ((times[counter % times.length] - times[(counter - 1 ) % times.length]) > 5000) {
                        counter = 0;
                        currentlyCounting = false;
                        box.setText("5 seconds have passed.\nCounter has reset");
                        times = new long[times.length];
                    }

                    else {
                        counter++;
                        double heartRate = Math.min(counter, times.length) / (deltaTime/ 60000.0); // number of taps/seconds passed
                        NumberFormat formatter = new DecimalFormat("##0.0");
                        box.setText(formatter.format(heartRate) + " BPM");
//                        long x = (times[counter % times.length] - times[(counter - 1 ) % times.length]);
////                        box.setText(formatter.format(heartRate) + " BPM\n" + " counter = " + counter +"\n deltaTime = " + deltaTime + " + \n" + x);
//                        box.setText(x + " \n" + times[0] + "\n counter" + counter);

                    }

                }


            }
        });
    }

}
