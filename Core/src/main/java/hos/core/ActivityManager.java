package hos.core;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import hos.utils.CoreLog;

/**
 * <p>Title: ActivityManager </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2022/3/20 19:12
 */
public class ActivityManager {
    private static ActivityManager activityManager;

    private final List<WeakReference<Activity>> activityRefs = new LinkedList<>();
    private final List<FrontBackCallback> frontBackCallbacks = new ArrayList<>();
    private int activityStartCount = 0;
    private boolean front = true;

    private ActivityManager() {
    }

    public static ActivityManager getInstance() {
        if (activityManager != null) {
            return activityManager;
        }
        return activityManager = new ActivityManager();
    }

    public static void init(Application application) {
        getInstance().initApp(application);
    }

    private void initApp(Application application) {
        application.registerActivityLifecycleCallbacks(new InnerActivityLifecycleCallbacks());
    }

    private void onFrontBackChanged(Activity activity, boolean front) {
        CoreLog.d("onFrontBackChanged front:" + front);
        for (FrontBackCallback callback : frontBackCallbacks) {
            callback.onChanged(activity, front);
        }
    }

    /**
     * 是否在后台
     *
     * @return true 在后台
     */
    public boolean isBackground() {
        return !front;
    }

    /**
     * 找出栈顶不为空，且没有被销毁的activity
     */

    public Activity getTopActivity() {
        return getTopActivity(true);
    }

    /**
     * 找出栈顶不为空，且没有被销毁的activity
     */

    public Activity getTopActivity(boolean onlyAlive) {
        if (activityRefs.size() <= 0) {
            return null;
        }
        WeakReference<Activity> activityRef = activityRefs.get(activityRefs.size() - 1);
        if (activityRef == null) {
            return null;
        }
        Activity activity = activityRef.get();
        if (activity == null) {
            return null;
        }
        if (onlyAlive) {
            if (activity.isFinishing() || Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && activity.isDestroyed()
            ) {
                activityRefs.remove(activityRef);
                return getTopActivity(true);
            }
        }
        return activity;
    }


    public void addFrontBackCallback(FrontBackCallback callback) {
        if (!frontBackCallbacks.contains(callback)) {
            frontBackCallbacks.add(callback);
        }
    }

    public void removeFrontBackCallback(FrontBackCallback callback) {
        frontBackCallbacks.remove(callback);
    }

    private class InnerActivityLifecycleCallbacks implements
            Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            if (bundle != null) {
                CoreLog.d("onActivityCreated bundle:" + bundle.toString());
            } else {
                CoreLog.d("onActivityCreated");
            }
            activityRefs.add(new WeakReference<Activity>(activity));
        }

        @Override
        public void onActivityStarted(Activity activity) {
            CoreLog.d("onActivityStarted");
            activityStartCount++;
            //activityStartCount>0  说明应用处在可见状态，也就是前台
            //!front 之前是不是在后台
            if (!front && activityStartCount > 0) {
                front = true;
                onFrontBackChanged(activity, front);
            }
        }

        @Override
        public void onActivityResumed(Activity activity) {
            CoreLog.d("onActivityResumed");
        }

        @Override
        public void onActivityPaused(Activity activity) {
            CoreLog.d("onActivityPaused");
        }

        @Override
        public void onActivityStopped(Activity activity) {
            CoreLog.d("onActivityStopped");
            activityStartCount--;
            if (activityStartCount <= 0 && front) {
                front = false;
                onFrontBackChanged(activity, front);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {
            if (bundle != null) {
                CoreLog.d("onActivitySaveInstanceState bundle:" + bundle.toString());
            } else {
                CoreLog.d("onActivitySaveInstanceState");
            }
        }

        @Override
        public void onActivityDestroyed(Activity activity) {
            CoreLog.d("onActivityDestroyed");
            for (WeakReference<Activity> reference : activityRefs) {
                if (reference != null && reference.get() == activity) {
                    activityRefs.remove(reference);
                    break;
                }
            }
        }
    }

    public interface FrontBackCallback {
        void onChanged(Activity activity, boolean front);
    }
}
