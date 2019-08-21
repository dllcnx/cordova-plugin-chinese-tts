package com.smartmapx.tts;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import java.io.IOException;

import static android.content.ContentValues.TAG;

/**
 * 离线文件
 *
 * @author ing
 * @date 2018/3/27
 */
public class OfflineResource {


    private AssetManager assets;
    private String destPath;

    private String backFilename;
    private String modelFilename;

    public OfflineResource(Context context) throws IOException {
        this.assets = context.getAssets();
        this.destPath = FileUtils.createTmpDir(context);
        setOfflineVoiceType();
    }

    public String getModelFilename() {
        return modelFilename;
    }

    public String getBackFilename() {
        return backFilename;
    }

    public void setOfflineVoiceType() throws IOException {
        String back = "backend_lzl";
        String model = "frontend_model";
        backFilename = copyAssetsFile(back);
        modelFilename = copyAssetsFile(model);

    }


    private String copyAssetsFile(String sourceFilename) throws IOException {
        String destFilename = destPath + "/" + sourceFilename;
        FileUtils.copyFromAssets(assets, sourceFilename, destFilename, false);
        Log.i(TAG, "Assets to sdcard successed：" + destFilename);
        return destFilename;
    }


}
