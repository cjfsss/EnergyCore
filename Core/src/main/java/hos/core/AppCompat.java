package hos.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.text.TextUtils;

/**
 * <p>Title: Hos </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2022/3/15 23:38
 */
public class AppCompat {

    private static AppCompat hos;


    private Boolean mIsAppDebug = null;


    private AppCompat() {
    }

    public static AppCompat getInstance() {
        if (hos != null) {
            return hos;
        }
        return hos = new AppCompat();
    }


    public static Application getApp() {
        return getApplication();
    }

    public static void init(Application application) {
        getInstance().initApp(application);
        ActivityManager.init(application);
    }

    public static <T extends Application> T getApplication() {
        return (T) getInstance().getApplicationInner();
    }

    /**
     * 是否是debug 模式
     *
     * @return true 是debug 模式
     */
    public boolean isAppDebug() {
        return getInstance().isAppDebugInner();
    }

    public static Context getContext() {
        Activity topActivity = getCurrentActivity();
        if (topActivity != null) {
            return topActivity;
        }
        return getApp();
    }

    public static Activity getCurrentActivity() {
        return ActivityManager.getInstance().getTopActivity();
    }

    /**
     * 是否在后台
     *
     * @return true 在后台
     */
    public static boolean isBackground() {
        return ActivityManager.getInstance().isBackground();
    }

    private Application application;

    protected void initApp(Application application) {
        this.application = application;
    }

    private Application getApplicationInner() {
        if (application == null) {
            try {
                application = (Application) Class.forName("android.app.ActivityThread")
                        .getMethod("currentApplication")
                        .invoke(null);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return application;
    }

    /**
     * 是否是debug 模式
     *
     * @return true 是debug 模式
     */
    private boolean isAppDebugInner() {
        if (mIsAppDebug != null) {
            return mIsAppDebug;
        }
        if (TextUtils.isEmpty(getApplicationInner().getPackageName())) return mIsAppDebug = false;
        ApplicationInfo ai = getApplicationInner().getApplicationInfo();
        return mIsAppDebug = (ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }


}
