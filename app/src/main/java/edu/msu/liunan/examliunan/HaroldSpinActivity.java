package edu.msu.liunan.examliunan;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class HaroldSpinActivity extends AppCompatActivity {


    private static String SPIN="Spins: ";
    private static String WINNINGS ="Winnings: $";
    private static final String PARAMETERS = "parameters";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_harold_spin);

         /*
         * Restore any state
         */
        if(savedInstanceState != null) {
            getHaroldSpinView().getFromBundle(PARAMETERS, savedInstanceState);
            setTextSpin();
            setButtonSpin();
            getHaroldSpinView().invalidate();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_harold_spin, menu);
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

    public void onStartSpin(View view) {
        Button spin= (Button)findViewById(R.id.buttonSpin);
        String buttonText = spin.getText().toString();
        String startSpin = this.getString(R.string.start_spin);
        //String stopSpin = this.getString(R.string.stop_spin);
        if (buttonText.equals(startSpin)){
            getHaroldSpinView().setSpinning(true);
            spin.setText(R.string.stop_spin);
            getHaroldSpinView().calculateSpinCount();
            setTextSpin();
        }
        else {
            getHaroldSpinView().setSpinning(false);
            getHaroldSpinView().setIsStop(true);
            spin.setText(R.string.start_spin);
            getHaroldSpinView().calculateMoney();
            setTextSpin();

        }
        getHaroldSpinView().invalidate();
    }

    public void onStartNewGame(View view){
        getHaroldSpinView().setSpinCount(0);
        getHaroldSpinView().setSpinMoney(0);
        setTextSpin();
        Button spin= (Button)findViewById(R.id.buttonSpin);
        spin.setText(R.string.start_spin);
        getHaroldSpinView().startNewGame();
        getHaroldSpinView().invalidate();
    }


    public HaroldSpinView getHaroldSpinView(){
        return (HaroldSpinView)this.findViewById(R.id.haroldSpinView);
    }

    public void setButtonSpin(){
        Button spin= (Button)findViewById(R.id.buttonSpin);
        if (getHaroldSpinView().getSpinning()){
            spin.setText(R.string.stop_spin);
        }
        else{
            spin.setText(R.string.start_spin);
        }
    }
    public void setTextSpin(){
        TextView tx = (TextView)findViewById(R.id.textSpin);
        int spinCount = getHaroldSpinView().getSpinCount();
        int spinMoney = getHaroldSpinView().getSpinMoney();
        tx.setText(SPIN+spinCount+" "+WINNINGS+spinMoney);

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        getHaroldSpinView().putToBundle(PARAMETERS, outState);

    }
}
