package com.lzn.voice.methed;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.util.Log;

import com.komect.skill.sdk.aar.ASkillForeignManager;
import com.lzn.voice.Voice_App;
import com.lzn.voice.util.Utils;
import static com.lzn.voice.util.Utils.TAG;

public class Run_Methed implements Runnable {
	static final int frequency = 16000;//44100;
	@SuppressWarnings("deprecation")
	static final int channelConfiguration = AudioFormat.CHANNEL_IN_MONO;
	static final int audioEncoding = AudioFormat.ENCODING_PCM_16BIT;
	// 定义录放音缓冲区大小
	private int recBufSize;
	//private int playBufSize;
	// 实例化录音对象
	private AudioRecord audiorecord;
	// 实例化播放对象
	//private AudioTrack audioTrack;
	// 是否录放的标记
	private boolean isRecording = false;
	// 是否录放的标记
	private boolean isFinish = false;

	private void isKeyVoice() {
		String strKeyVoice = Utils.getProperty("persist.sys.lzn.key.voice");
		if(strKeyVoice != null && strKeyVoice.equals("1")) {
			isRecording = true;
		} else {
			isRecording = false;
		}
	}

	public Run_Methed() {
		// 调用getMinBufferSize方法获得录音的最小缓冲空间
		recBufSize = audiorecord.getMinBufferSize(frequency,
				channelConfiguration, audioEncoding);

		// 调用getMinBufferSize方法获得放音最小的缓冲区大小
		//playBufSize = AudioTrack.getMinBufferSize(frequency,
		//		AudioFormat.CHANNEL_CONFIGURATION_MONO, audioEncoding);

		Log.d(TAG, "Run_Methed recBufSize:" + recBufSize);
		// -----------------------------------------、
		// 调用构造函数实例化录音对象
		audiorecord = new AudioRecord(MediaRecorder.AudioSource.MIC, frequency,
				channelConfiguration, audioEncoding, recBufSize);

		// 调用构造函数实例化放音对象
		//audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC, frequency,
		//		AudioFormat.CHANNEL_CONFIGURATION_MONO, audioEncoding, playBufSize,
		//		AudioTrack.MODE_STREAM);
	}

	// 线程体
	public void run() {

		try {
			int bufferReadResult = 0;
			byte[] buffer = new byte[recBufSize];
			while(!isFinish) {
				isKeyVoice();
				//Log.d(TAG, "Run_Methed isRecording:" + isRecording);
				if (isRecording && Utils.getVoiceInit()) {
					Log.d(TAG, "Run_Methed startVoice!");
					audiorecord.startRecording();// 开始录制
					//audioTrack.play();// 开始播放
					ASkillForeignManager.getInstance().startSendVoice();
					// 从MIC保存数据到缓冲区
					while (isRecording) {
						bufferReadResult = audiorecord.read(buffer, 0, recBufSize);
						byte[] tmpBuf = new byte[bufferReadResult];
						System.arraycopy(buffer, 0, tmpBuf, 0, bufferReadResult);
						//audioTrack.write(tmpBuf, 0, tmpBuf.length);
						ASkillForeignManager.getInstance().sendVoiceBytes(tmpBuf);
						isKeyVoice();
						Thread.sleep(2);
					}
					ASkillForeignManager.getInstance().stopSendVoiceBytes();
					//audioTrack.stop();
					audiorecord.stop();
				}

				Thread.sleep(200);
			}
		} catch (Throwable e) {
			Log.e(TAG, "Run_Methed Error", e);
		}
	}

	public void stop() {
		isRecording = false;
	}

	public void start() {
		isRecording = true;
	}

}

