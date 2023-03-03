package com.lzn.voice;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.komect.base.util.CommonUtils;
import com.komect.base.bean.UserInfo;
import com.komect.skill.sdk.aar.ASkillForeignManager;
import com.komect.skill.sdk.aar.ASkillListener;
import com.komect.skill.sdk.bean.local.Chips;
import com.lzn.voice.util.Utils;

import static com.lzn.voice.util.Utils.TAG;

public class Voice_App extends Application {

    private Context mContext;

    private boolean initUser(UserInfo userInfo) {
        if(Utils.getCmccDeviceCmei() == null || Utils.getCmccDeviceCmei().length() != Utils.CMCC_DEVICECMEI_LENGTH) {
            Log.e(TAG, "getCmccDeviceCmei error!");
            return false;
        }
        userInfo.setPassword("12345678")
            .setDeviceSn(Utils.getCmccDeviceCmei())
            .setDeviceType(Utils.getCmccDeviceType())
            .setProductToken(Utils.getCmccProductToken())
            .setProductClass(Utils.getCmccProductClass())
            .setSwVersion(Utils.getCmccSwVersion())
            .setRecordBufSize(8192)
            .setBattery("0")// 是否有电池，1为有电池，0为没有电池
            .setVadMode("LOCAL")// 当前语音设备使用模式LOCAL 或 CLOUD
            .setCmei(Utils.getCmccDeviceCmei())
            .setMac(Utils.getWirelessMacAddress(mContext).replace(":", ""))
            .setDeviceMac(Utils.getWirelessMacAddress(mContext))
            .setChips(new Chips().setFactory("Hisilicon").setModel(Utils.getCmccProductClass()).setType("WiFi/BLE").toString())
            .setDmAppId(Utils.getCmccDmAppId());
        return true;
    }

    public void onCreate() {
        super.onCreate();

        mContext = this;

        Log.d(TAG, "Voice_App onCreate 001!");

        //设置登陆所用参数 以UserInfo 格式传入
        UserInfo userInfo = UserInfo.getInstance();

        if(!initUser(userInfo)) {
            return ;
        }

        CommonUtils.attachApplication(this);

        //开始初始化 ASkillListener回调方法按需取用
        ASkillForeignManager.getInstance().setAuthInfo(userInfo).showAsr(false).setListener(new ASkillListener() {
            @Override
            public void asrResult(int state, String s) { //语音识别结果返回
                Log.d(TAG, "asrState:" + state + " asrResult:"+ s);
            }
            @Override
            public void vadOption(int state) { //通知用户开启/结束拾音
                super.vadOption(state);
                Log.d(TAG, "vadOption:    "+ state);
                if (state==1){
                    //shellIEngineManager.start();
                } else {
                    //shellIEngineManager.myVadEngineStop();
                }
            }
            @Override
            public void nlpResult(String nlp) {//语音语义平台nlp返回数据
                Log.d(TAG, "nlpResult:    "+ nlp);
                super.nlpResult(nlp);
            }
            @Override
            public boolean isExecNlp(String nlp) {//是否由aar包处理nlp技能
                return true;
            }
            @Override
            public boolean ttsFinish() {
                Log.d(TAG, "ttsFinish");
                return true;
            }
            @Override
            public void ttsBegin() {
                Log.d(TAG, "ttsBegin");
            }
            @Override
            public void bindStatus(int sate) {//绑定状态回调
                Log.d(TAG, "bindStatus:    " + sate);
            }
            @Override
            public void loginStateCallBack(int sate) {//登录状态回调
                Log.d(TAG, "loginStateCallBack:    " + sate);
            }
            @Override
            public boolean otaUpdateResult(String result) {
                Log.d(TAG, "otaUdateResult:    " + result);
                return super.otaUpdateResult(result);
                //return true;
            }
        });

        //注：默认线上环境 测试环境 请在调用init方法之前调用
        //CloudConfig.setInstance(CloudConfig.TvBoxTest);

        //CloudConfig.setInstance(CloudConfig.TvBoxTest);
        ASkillForeignManager.getInstance().init(this);
        ASkillForeignManager.getInstance().needNlp(true);

        Utils.setVoiceInit(true);
    }
}
