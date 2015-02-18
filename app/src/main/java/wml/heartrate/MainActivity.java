package wml.heartrate;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
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

    ImageButton imageButton;
    double tapCounter;
    long startTime;
    long currentTime;
    long previousTime;
    boolean currentlyCounting;
    long[] times;
        public void addListenerOnButton() {

//         imageButton = (ImageButton) findViewById(R.id.HeartButton);
//        imageButton.setOnClickListener();
        imageButton = (ImageButton) findViewById(R.id.HeartButton);
        tapCounter = 0;

       currentlyCounting = false;

        imageButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                currentTime = System.currentTimeMillis();
                TextView box = (TextView)findViewById(R.id.RateCounter);
                long deltaTime;
                if(!currentlyCounting)
                {
                    //just starting out
                    startTime = currentTime;
                    previousTime = startTime;
                    currentlyCounting = true;
                    tapCounter++;
                    box.setText("Continue Tapping");
                }
                else
                {
                            deltaTime = currentTime - startTime;
                            if((currentTime - previousTime)/1000.0 > 5)
                            {
                                //reset from overtime
                                tapCounter = 0;
                            currentlyCounting = false;

                        box.setText("5 seconds have passed.\nCounter has reset");
                    }
                    else
                    {
                        tapCounter++;
                        double heartRate = tapCounter/(deltaTime/60000.0);

                        NumberFormat formatter = new DecimalFormat("##0.0");
                        box.setText(formatter.format(heartRate) + " BPM");  previousTime = System.currentTimeMillis();
                    }
                }




            }

        });

    }

}
