package com.smartmapx.tts;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.Manifest;
import android.widget.Toast;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

/**
 * This class echoes a string called from JavaScript.
 */

public class ChineseTTS extends CordovaPlugin {

    private Context context;

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("init")) {
            System.out.println("初始化");
            this.init();
            return true;
        }


        if (action.equals("speak")) {
            System.out.println("播放");
            String message = args.getString(0);
            this.speak(message, callbackContext);
            return true;
        }

        if (action.equals("state")) {
            System.out.println("查训状态");
            if (this.state_speech()) {
                callbackContext.success(1);     //正在播放
            } else {
                callbackContext.success(0);     //播放完成
            }
            return true;
        }
        return false;
    }


    /**
     * 初始化引擎
     *
     * @param
     */
    private void init() {
        context = this.cordova.getActivity();
        permissionRequest();
    }

    /**
     * 讲话
     *
     * @param message
     * @param callbackContext
     */
    private void speak(String message, CallbackContext callbackContext) {
        if (message != null && message.length() > 0) {
            SpeechUtilOffline.getInstance(context).play(message, SpeechUtilOffline.PLAY_MODE.QUEUED);
        } else {
            callbackContext.error("信息为空.");
        }
    }

    /**
     * 获得播放状态
     */
    private boolean state_speech() {
        System.out.println(SpeechUtilOffline.getInstance(context).isSpeaking());
        return SpeechUtilOffline.getInstance(context).isSpeaking();
    }


    /**
     * 运行时权限--
     */
    public void permissionRequest() {
        //        Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                Manifest.permission.ACCESS_FINE_LOCATION,
//                Manifest.permission.READ_PHONE_STATE,
//                Manifest.permission.RECEIVE_BOOT_COMPLETED

        if (XXPermissions.isHasPermission(context, Permission.WRITE_EXTERNAL_STORAGE) && XXPermissions.isHasPermission(context, Permission.ACCESS_FINE_LOCATION) && XXPermissions.isHasPermission(context, Permission.READ_PHONE_STATE)) {
            SpeechUtilOffline.getInstance(context);
        } else {
            XXPermissions.with(this.cordova.getActivity())
                    // 可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                    .constantRequest()
                    // 支持请求6.0悬浮窗权限8.0请求安装权限
                    .permission(Permission.WRITE_EXTERNAL_STORAGE, Permission.ACCESS_FINE_LOCATION, Permission.READ_PHONE_STATE)
                    // 不指定权限则自动获取清单中的危险权限
//                .permission(Permission.Group.STORAGE, Permission.Group.CALENDAR)
                    .request(new OnPermission() {

                        @Override
                        public void hasPermission(List<String> granted, boolean isAll) {
                            if (isAll) {
                                Toast.makeText(context, "权限获取成功,正在初始化语音包", Toast.LENGTH_SHORT).show();
                                SpeechUtilOffline.getInstance(context);
                            }
                        }

                        @Override
                        public void noPermission(List<String> denied, boolean quick) {
                            Toast.makeText(context, "权限获取失败,语音包初始化失败", Toast.LENGTH_SHORT).show();
                        }
                    });
        }


    }


}
