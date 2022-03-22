package hos.core;

import android.app.Activity;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


/**
 * <p>Title: AppCompatApplication </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/4/19 23:16
 */
public abstract class AppCompatApplication extends Application implements ActivityManager.FrontBackCallback {

    @SuppressWarnings("unchecked")
    @NonNull
    public static <T extends AppCompatApplication> T getAppCompatApplication() {
        return (T) AppCompat.getApplication();
    }
//
//    // 全局数据
//    @Nullable
//    private MutableLiveData<Bundle> msg = null;
//
//    @NonNull
//    public MutableLiveData<Bundle> getMsg() {
//        if (msg != null) {
//            return msg;
//        }
//        return msg = new MutableLiveData<>();
//    }

    @Override
    public void onCreate() {
        super.onCreate();
        init(this);
    }

    protected void init(@NonNull Application application){
        AppCompat.getInstance().init(application);
        ActivityManager.getInstance().init(application);
        ActivityManager.getInstance().addFrontBackCallback(this);
    }

    @Nullable
    public Activity getCurrentActivity() {
        return ActivityManager.getInstance().getTopActivity();
    }

    /**
     * 是否在后台
     *
     * @return true 在后台
     */
    public boolean isBackground() {
        return ActivityManager.getInstance().isBackground();
    }

    /**
     * 是否是debug 模式
     *
     * @return true 是debug 模式
     */
    public boolean isAppDebug() {
        return AppCompat.getInstance().isAppDebug();
    }

    /**
     * 回到前台
     *
     * @param activity 上下文
     */
    protected abstract void onForeground(@NonNull Activity activity);

    /**
     * 回到后台
     *
     * @param activity 上下文
     */
    protected abstract void onBackground(@NonNull Activity activity);

    @Override
    public void onChanged(Activity activity, boolean front) {
        if (front) {
            onForeground(activity);
        } else {
            onBackground(activity);
        }
    }
}
