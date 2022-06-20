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
    }


    public static <T extends Application> T getApplication() {
        return (T) getInstance().getApplicationInner();
    }

    private Application application;

    private void initApp(Application application) {
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
    public boolean isAppDebug() {
        if (mIsAppDebug != null) {
            return mIsAppDebug;
        }
        if (TextUtils.isEmpty(getApplicationInner().getPackageName())) return mIsAppDebug = false;
        ApplicationInfo ai = getApplicationInner().getApplicationInfo();
        return mIsAppDebug = (ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }


    public Context getContext() {
        Activity topActivity = ActivityManager.getInstance().getTopActivity(true);
        if (topActivity != null) {
            return topActivity;
        }
        return getApp();
    }

}
