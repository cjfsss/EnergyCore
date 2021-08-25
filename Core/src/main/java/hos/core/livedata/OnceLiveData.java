package hos.core.livedata;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Description: 只执行一次的LiveData
 * Create by lxj, at 2019/3/6
 */
public class OnceLiveData<T> extends MutableLiveData<T> {
    private final AtomicBoolean isRead = new AtomicBoolean(false);

    /**
     * ensure the event is non-null and can only been seen once
     */
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull final Observer<? super T> observer) {
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                if (isRead.compareAndSet(false, true)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    @Override
    public void postValue(T value) {
        isRead.set(false);
        super.postValue(value);
    }

    @Override
    public void setValue(T value) {
        isRead.set(false);
        super.setValue(value);
    }
}
