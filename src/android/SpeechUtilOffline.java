package com.smartmapx.tts;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.unisound.client.SpeechConstants;
import com.unisound.client.SpeechSynthesizer;
import com.unisound.client.SpeechSynthesizerListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 离线语音
 *
 * @author KeiferJu
 * @date 2019/8/15
 */
public class SpeechUtilOffline {
    public static final String appKey = "_appKey_";
    public static final String secret = "_secret_";
    private static SpeechUtilOffline instance;
    private SpeechSynthesizer mTTSPlayer;
    private boolean isSpeaking = false;
    private List<SpeechItem> speechList = new ArrayList<>();
    private boolean released = false;
    protected OfflineResource offlineResource;

    private SpeechUtilOffline(Context context) {
        init(context);
        released = false;
    }

    public static SpeechUtilOffline getInstance(Context context) {
        if (instance == null) {
            instance = new SpeechUtilOffline(context);
        }
        return instance;
    }


    /**
     * 设置/获取当前播放状态
     *
     * @return
     */
    public boolean isSpeaking() {   //this line modified by shichen.li
        return isSpeaking;
    }

    private void setSpeaking(boolean speaking) {    //this line modified by shichen.li
        isSpeaking = speaking;
    }


    /**
     * 初始化引擎
     */
    private void init(final Context context) {
        try {
            offlineResource = new OfflineResource(context);
        } catch (IOException e) {
            Log.e("ing", "offlineResouce failed , error msg : " + e.getMessage());
            e.printStackTrace();
        }
        // 初始化语音合成对象
        mTTSPlayer = new SpeechSynthesizer(context, appKey, secret);
        // 设置本地合成
        mTTSPlayer.setOption(SpeechConstants.TTS_SERVICE_MODE, SpeechConstants.TTS_SERVICE_MODE_LOCAL);
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_PITCH, 50);//音调
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_SPEED, 52);//语速
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_VOICE_VOLUME, 100);//音量
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_STREAM_TYPE, AudioManager.STREAM_NOTIFICATION);
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_FRONTEND_MODEL_PATH, offlineResource.getModelFilename());
        // 设置后端模型
        mTTSPlayer.setOption(SpeechConstants.TTS_KEY_BACKEND_MODEL_PATH, offlineResource.getBackFilename());
        // 设置回调监听
        mTTSPlayer.setTTSListener(new SpeechSynthesizerListener() {

            @Override
            public void onEvent(int type) {
                switch (type) {
                    case SpeechConstants.TTS_EVENT_INIT:
                        // 初始化成功回调
                        System.out.println("初始化成功回调");
                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_START:
                        // 开始合成回调
                        setSpeaking(true);
                        System.out.println("开始合成回调");

                        break;
                    case SpeechConstants.TTS_EVENT_SYNTHESIZER_END:
                        // 合成结束回调
                        System.out.println("开始合成回调");
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_BEGIN:
                        // 开始缓存回调
                        System.out.println("开始缓存回调");
                        break;
                    case SpeechConstants.TTS_EVENT_BUFFER_READY:
                        // 缓存完毕回调
                        System.out.println("缓存完毕回调");
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_START:
                        // 开始播放回调
                        System.out.println("开始播放回调");
                        break;
                    case SpeechConstants.TTS_EVENT_PLAYING_END:
                        // 播放完成回调
                        setSpeaking(false);
                        System.out.println("播放完成回调");
                        break;
                    case SpeechConstants.TTS_EVENT_PAUSE:
                        // 暂停回调
                        System.out.println("暂停回调");
                        break;
                    case SpeechConstants.TTS_EVENT_RESUME:
                        // 恢复回调
                        System.out.println("恢复回调");
                        break;
                    case SpeechConstants.TTS_EVENT_STOP:
                        // 停止回调
                        System.out.println("停止回调");
                        break;
                    case SpeechConstants.TTS_EVENT_RELEASE:
                        // 释放资源回调
                        System.out.println("释放资源回调");
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onError(int type, String errorMSG) {
                // 语音合成错误回调
                Log.e("ing", "TTS onError __ type : " + type + " errorMsg : " + errorMSG);
            }
        });
        // 初始化合成引擎
        mTTSPlayer.init("");

    }

    /**
     * 停止播放
     */
    public void stop() {
        mTTSPlayer.stop();
    }

    /**
     * 播放
     */
    public void play(String content) {
        playImmediately(content);
    }

    public void play(String content, PLAY_MODE playMode) {
        switch (playMode) {
            case QUEUED: {
                playQueued(content);
                break;
            }
            case IMMEDIATELY: {
                playImmediately(content);
                break;
            }
        }
    }

    private void updateSpeech() {
        if (!isSpeaking) {
            if (speechList.size() > 0) {
                speak(speechList.remove(speechList.size() - 1).content);
            }
        }
    }

    private void speak(String content) {
        mTTSPlayer.playText(content);
    }

    public void playQueued(String content) {
        speechList.add(new SpeechItem(content, PLAY_MODE.QUEUED));
        updateSpeech();
    }

    public void playImmediately(String content) {
        speak(content);
    }

    /**
     * 释放资源
     */
    public void release() {
        // 主动释放离线引擎
        if (released) {
            return;
        }
        if (mTTSPlayer != null) {
            mTTSPlayer.stop();
            mTTSPlayer.release(SpeechConstants.TTS_RELEASE_ENGINE, null);
        }
        instance = null;
        released = true;
    }


    public enum PLAY_MODE {
        QUEUED,
        IMMEDIATELY
    }

    private class SpeechItem {
        public String content;
        public PLAY_MODE playMode;

        public SpeechItem(String content, PLAY_MODE mode) {
            this.content = content;
            this.playMode = mode;
        }
    }
}
