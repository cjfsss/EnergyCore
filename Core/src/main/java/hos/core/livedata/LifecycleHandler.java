package hos.core.livedata;

import android.os.Handler;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * Description: 自动在UI销毁时移除msg和任务的Handler，不会有内存泄露
 * Create by dance, at 2019/5/21
 */
public class LifecycleHandler extends Handler implements LifecycleObserver {

    private final LifecycleOwner lifecycleOwner;

    public LifecycleHandler(@Nullable LifecycleOwner lifecycleOwner, @NonNull Looper looper) {
        super(looper);
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().addObserver(this);
        }
        this.lifecycleOwner = lifecycleOwner;
    }

    public LifecycleHandler(@Nullable LifecycleOwner lifecycleOwner) {
        this(lifecycleOwner, Looper.getMainLooper());
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void onDestroy() {
        removeCallbacksAndMessages(null);
        if (lifecycleOwner != null) {
            lifecycleOwner.getLifecycle().removeObserver(this);
        }
    }
}
