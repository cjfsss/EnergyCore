package hos.core;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;


/**
 * <p>Title: AppCompatApplication </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/4/19 23:16
 */
public class AppCompatApplication extends Application implements LifecycleOwner,
        ViewModelStoreOwner, HasDefaultViewModelProviderFactory, Application.ActivityLifecycleCallbacks {
    @Nullable
    private LifecycleRegistry mLifecycleRegistry = null;

    // Lazily recreated from NonConfigurationInstances by getViewModelStore()
    @Nullable
    private ViewModelStore mViewModelStore;
    @Nullable
    private ViewModelProvider.Factory mDefaultFactory;

    @NonNull
    private static AppCompatApplication mAppCompatApplication = null;

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends AppCompatApplication> T getAppCompatApplication() {
        return (T) mAppCompatApplication;
    }

    /**
     * 当前的活动
     */
    @Nullable
    private Activity mCurrentActivity;

    @Nullable
    private Boolean mIsAppDebug = null;

    private boolean mIsBackground = false;

    // 全局数据
    @Nullable
    private MutableLiveData<Bundle> msg = null;

    public int getPageSize() {
        return 30;
    }

    @NonNull
    public MutableLiveData<Bundle> getMsg() {
        if (msg != null) {
            return msg;
        }
        return msg = new MutableLiveData<>();
    }

    public AppCompatApplication() {
        Lifecycle lifecycle = getLifecycle();
        // 监听Activity生命周期
        registerActivityLifecycleCallbacks(this);
        //noinspection ConstantConditions
        if (lifecycle == null) {
            throw new IllegalStateException("getLifecycle() returned null in ComponentActivity's "
                    + "constructor. Please make sure you are lazily constructing your Lifecycle "
                    + "in the first call to getLifecycle() rather than relying on field "
                    + "initialization.");
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mAppCompatApplication = this;
        ((LifecycleRegistry) getLifecycle()).handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        Log.d("AppCompatApplication", "onCreate");
    }

    @Nullable
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * {@inheritDoc}
     * <p>
     * Overriding this method is no longer supported and this method will be made
     * <code>final</code> in a future version of Fragment.
     */
    @Override
    @NonNull
    public Lifecycle getLifecycle() {
        if (mLifecycleRegistry != null) {
            return mLifecycleRegistry;
        }
        return mLifecycleRegistry = new LifecycleRegistry(this);
    }

    /**
     * Returns the {@link ViewModelStore} associated with this Fragment
     * <p>
     * Overriding this method is no longer supported and this method will be made
     * <code>final</code> in a future version of Fragment.
     *
     * @return a {@code ViewModelStore}
     * @throws IllegalStateException if called before the Fragment is attached i.e., before
     *                               onAttach().
     */
    @NonNull
    @Override
    public ViewModelStore getViewModelStore() {
        if (mViewModelStore == null) {
            mViewModelStore = new ViewModelStore();
        }
        return mViewModelStore;
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        if (mDefaultFactory == null) {
            mDefaultFactory = ViewModelProvider.AndroidViewModelFactory.getInstance(this);
        }
        return mDefaultFactory;
    }

    @Override
    public void onTerminate() {
        Lifecycle lifecycle = getLifecycle();
        ((LifecycleRegistry) lifecycle).handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        unregisterActivityLifecycleCallbacks(this);
        mCurrentActivity = null;
        Log.d("AppCompatApplication", "onTerminate");
        super.onTerminate();
    }

    /**
     * 是否在后台
     *
     * @return true 在后台
     */
    public boolean isBackground() {
        return mIsBackground;
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
        if (TextUtils.isEmpty(getPackageName())) return mIsAppDebug = false;
        ApplicationInfo ai = getApplicationInfo();
        return mIsAppDebug = (ai != null && (ai.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0);
    }

    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
        mCurrentActivity = activity;
        if (isAppDebug()) {
            Log.d("AppCompatApplication", activity.getClass().getSimpleName() + " onActivityCreated  currentActivity:" + mCurrentActivity);
        }
    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        mCurrentActivity = activity;
        Lifecycle lifecycle = getLifecycle();
        ((LifecycleRegistry) lifecycle).handleLifecycleEvent(Lifecycle.Event.ON_START);
        if (isAppDebug()) {
            Log.d("AppCompatApplication", activity.getClass().getSimpleName() + " onActivityStarted  currentActivity:" + mCurrentActivity);
        }
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        mIsBackground = false;
        onForeground(activity);
        mCurrentActivity = activity;
        Lifecycle lifecycle = getLifecycle();
        ((LifecycleRegistry) lifecycle).handleLifecycleEvent(Lifecycle.Event.ON_RESUME);
        if (isAppDebug()) {
            Log.d("AppCompatApplication", activity.getClass().getSimpleName() + " onActivityResumed  currentActivity:" + mCurrentActivity);
        }
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {
        Lifecycle lifecycle = getLifecycle();
        ((LifecycleRegistry) lifecycle).handleLifecycleEvent(Lifecycle.Event.ON_PAUSE);
        if (isAppDebug()) {
            Log.d("AppCompatApplication", activity.getClass().getSimpleName() + " onActivityPaused  currentActivity:" + mCurrentActivity);
        }
    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {
        mIsBackground = true;
        onBackground(activity);
        Lifecycle lifecycle = getLifecycle();
        ((LifecycleRegistry) lifecycle).handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        if (isAppDebug()) {
            Log.d("AppCompatApplication", activity.getClass().getSimpleName() + " onActivityStopped  currentActivity:" + mCurrentActivity);
        }
    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
        Lifecycle lifecycle = getLifecycle();
        if (lifecycle instanceof LifecycleRegistry) {
//            ((LifecycleRegistry) lifecycle).setCurrentState(Lifecycle.State.CREATED);
            ((LifecycleRegistry) lifecycle).handleLifecycleEvent(Lifecycle.Event.ON_STOP);
            if (isAppDebug()) {
                Log.d("AppCompatApplication", activity.getClass().getSimpleName() + " onActivitySaveInstanceState  currentActivity:" + mCurrentActivity);
            }
        }
    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        if (mCurrentActivity == activity) {
            mCurrentActivity = null;
        }
        if (isAppDebug()) {
            Log.d("AppCompatApplication", activity.getClass().getSimpleName() + " onActivityDestroyed  currentActivity:" + mCurrentActivity);
        }
    }

    /**
     * 回到前台
     *
     * @param activity 上下文
     */
    protected void onForeground(@NonNull Activity activity) {

    }

    /**
     * 回到后台
     *
     * @param activity 上下文
     */
    protected void onBackground(@NonNull Activity activity) {

    }
}
