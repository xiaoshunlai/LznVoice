package com.lzn.voice.activity;

import com.lzn.voice.methed.Run_Methed;
import com.lzn.voice.R;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import static com.lzn.voice.util.Utils.TAG;

public class Main_Activity extends Activity {

	// 麦克风开关图片Button
	private ImageButton state;
	private Run_Methed run_Metned = new Run_Methed();
	private Thread thread = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		// 绑定控件
		this.state = (ImageButton) findViewById(R.id.state);
		this.state.setOnClickListener(new ocl());
	}

	// 记录麦克风状态的布尔值
	private boolean is = false;

	// 监听类
	class ocl implements OnClickListener {

		@Override
		public void onClick(View v) {

			if (v.getId() == R.id.state && is == true) {
				// 切换图片按钮的背景图【开】——————【关】
				state.setImageDrawable(getResources().getDrawable(
						R.drawable.close));
				run_Metned.stop();

				// 改变状态
				is = false;
			} else if (v.getId() == R.id.state && is == false) {
				// 切换图片按钮的背景图【关】——————【开】
				state.setImageDrawable(getResources().getDrawable(
						R.drawable.open));
				// 改变状态
				try {
					Thread thread = new Thread(run_Metned);
					thread.start();
					run_Metned.start();
				} catch (Exception e) {

				}
				is = true;
			}
		}
	}

	int i = 0;

	public boolean onKeyDown(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyDown keyCode:" + keyCode);
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			i++;
			if (i == 3) {
				finish();
			}

		}
		return true;
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {
		Log.d(TAG, "onKeyUp keyCode:" + keyCode);
		return true;
	}
}

