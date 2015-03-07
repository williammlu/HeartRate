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
                box.setText("Tap Heart to Begin");
                times = new long[beatsToConsider];
            }

        });


        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                TextView box = (TextView) findViewById(R.id.RateCounter);
                long deltaTime;
                if (!currentlyCounting) {

                        currentlyCounting = true;
                        times[(counter % times.length)] = System.currentTimeMillis();
                    counter++;
                    box.setText("Continue Tapping");
                } else {
                    times[(counter % times.length)] = System.currentTimeMillis();
                    if (counter >= times.length) {
                        deltaTime = (long)((times[(counter % times.length)] - times[((counter + 1) % times.length)]) * ((times.length +1.0)/times.length));
                    } else {
                        // array not full yet
                        deltaTime = (long)((times[counter] - times[0]) * (counter +1.0 )/counter);
                    }

                    if ((times[counter % times.length] - times[(counter - 1 ) % times.length]) > 5000) {// alternate code
                        //reset from overtime
                        counter = 0;
                        currentlyCounting = false;
                        box.setText("5 seconds have passed.\nCounter has reset");
                        times = new long[times.length];
                    }

                    else {
                        counter++;
                        double heartRate = Math.min(counter, times.length) / (deltaTime/ 60000.0);
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
