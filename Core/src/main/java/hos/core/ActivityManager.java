package hos.core;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>Title: ActivityManager </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2022/3/20 19:12
 */
@RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
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

    public static void init(@NonNull Application application) {
        getInstance().initApp(application);
    }

    private void initApp(@NonNull Application application) {
        application.registerActivityLifecycleCallbacks(new InnerActivityLifecycleCallbacks());
    }

    private void onFrontBackChanged(Activity activity, boolean front) {
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
    @Nullable
    public Activity getTopActivity() {
        return getTopActivity(true);
    }

    /**
     * 找出栈顶不为空，且没有被销毁的activity
     */
    @Nullable
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

    @RequiresApi(api = Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private class InnerActivityLifecycleCallbacks implements
            Application.ActivityLifecycleCallbacks {

        @Override
        public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {
            activityRefs.add(new WeakReference<Activity>(activity));
        }

        @Override
        public void onActivityStarted(@NonNull Activity activity) {
            activityStartCount++;
            //activityStartCount>0  说明应用处在可见状态，也就是前台
            //!front 之前是不是在后台
            if (!front && activityStartCount > 0) {
                front = true;
                onFrontBackChanged(activity, front);
            }
        }

        @Override
        public void onActivityResumed(@NonNull Activity activity) {

        }

        @Override
        public void onActivityPaused(@NonNull Activity activity) {

        }

        @Override
        public void onActivityStopped(@NonNull Activity activity) {
            activityStartCount--;
            if (activityStartCount <= 0 && front) {
                front = false;
                onFrontBackChanged(activity, front);
            }
        }

        @Override
        public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(@NonNull Activity activity) {
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
