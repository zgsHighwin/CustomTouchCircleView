package highwin.zgs.customtouchcircleview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar;

public class MainActivity extends AppCompatActivity {

    private CustomTouchCircleView mCtcv;
    private SeekBar mSb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCtcv = ((CustomTouchCircleView) findViewById(R.id.ctcv));
        mSb = ((SeekBar) findViewById(R.id.sb));
        mSb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mCtcv.setAngle(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mCtcv.setOnAngleChangeListener(new CustomTouchCircleView.OnAngleChangeListener() {
            @Override
            public void onAngleChange(float angle) {
                mSb.setProgress((int) angle);
            }
        });
    }
}
