package com.smartmapx.tts;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;
import android.util.Log;


import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 文件操作类
 *
 * @author KeiferJu
 * @date 2019/8/
 */
public final class FileUtils {
    private static final String LOGTAG ="/ing/tts";
    private static FileUtils fileUtils = new FileUtils();

    private FileUtils() {
    }

    public static FileUtils getInstance() {
        return fileUtils;
    }

    /**
     * 获取SDCARD根路径
     *
     * @return
     */
    private static StringBuffer getRootDir() throws Exception {
        return new StringBuffer().append(Environment
                .getExternalStorageDirectory());
    }

    /**
     * 获取存在SDCARD上文件的绝对路径
     *
     * @param mContext
     * @param folderName
     */
    public static StringBuffer getExternalFileAbsoluteDir(Context mContext,
                                                          String folderName, String fileName) throws Exception {
        StringBuffer stringBuffer = getRootDir().append(File.separator);
        stringBuffer.append(getExternalFilesDir(mContext, folderName));
        if (fileName != null) {
            if (0 == fileName.indexOf(File.separator)) {
                stringBuffer.append(fileName);
            } else {
                stringBuffer.append(File.separator);
                stringBuffer.append(fileName);
            }
        }
        return stringBuffer;
    }


    /**
     * 获取SDCARD上应用存储路径
     *
     * @param mContext
     * @param folderName
     * @return
     */
    private static StringBuffer getExternalFilesDir(Context mContext,
                                                    String folderName) throws Exception {
        String packageName = mContext.getPackageName();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Android").append(File.separator).append("data")
                .append(File.separator).append(packageName);
        if (folderName != null) {
            if (0 == folderName.indexOf(File.separator)) {
                stringBuffer.append(folderName);
            } else {
                stringBuffer.append(File.separator);
                stringBuffer.append(folderName);
            }
        }
        Log.d(LOGTAG, "FileUtils getExternalFilesDir "
                + stringBuffer.toString());
        return stringBuffer;
    }


    /**
     * 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
     * @param context
     * @return
     */

    public static String createTmpDir(Context context) {
        String sampleDir = "/ing/tts";
        String tmpDir = Environment.getExternalStorageDirectory().toString() + sampleDir;
        if (!FileUtils.makeDir(tmpDir)) {
            tmpDir = context.getExternalFilesDir(sampleDir).getAbsolutePath();
            if (!FileUtils.makeDir(sampleDir)) {
                throw new RuntimeException("create model resources dir failed :" + tmpDir);
            }
        }
        return tmpDir;
    }

    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    /**
     * assets文件2 sdcard
     * @param assets
     * @param source
     * @param dest
     * @param isCover
     * @throws IOException
     */
    public static void copyFromAssets(AssetManager assets, String source, String dest, boolean isCover) throws IOException {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }
        }
    }
}
