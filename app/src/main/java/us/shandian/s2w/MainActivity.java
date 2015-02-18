package us.shandian.s2w;

import android.app.Activity;
import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.io.IOException;

public class MainActivity extends Activity implements RadioGroup.OnCheckedChangeListener
{
	
	private RadioGroup mGroup;
	private RadioButton mDT2W;
	private RadioButton mS2W;
	private RadioButton mDisabled;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		getActionBar().setDisplayShowHomeEnabled(false);
		getActionBar().setDisplayUseLogoEnabled(false);
		
		mGroup = (RadioGroup) findViewById(R.id.radio_group);
		mDT2W = (RadioButton) findViewById(R.id.radio_dt2w);
		mS2W = (RadioButton) findViewById(R.id.radio_s2w);
		mDisabled = (RadioButton) findViewById(R.id.radio_disabled);
		
		Utility.State state = Utility.State.DISABLED;
		try {
			state = Utility.readState();
		} catch (IOException e) {
			
		}
		
		if (state == Utility.State.DT2W) {
			mDT2W.setChecked(true);
		} else if (state == Utility.State.S2W) {
			mS2W.setChecked(true);
		} else {
			mDisabled.setChecked(true);
		}
		
		Utility.writeStateToPref(this, state);
		
		mGroup.setOnCheckedChangeListener(this);
    }

	@Override
	public void onCheckedChanged(RadioGroup g, int id) {
		Utility.State state = Utility.State.DISABLED;
		switch (id) {
			case R.id.radio_dt2w:
				state = Utility.State.DT2W;
				break;
			case R.id.radio_s2w:
				state = Utility.State.S2W;
				break;
		}
		
		try {
			Utility.setState(state);
		} catch (IOException e) {
			
		}
		
		Utility.writeStateToPref(this, state);
	}
}
