package hos.core.singleton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.WeakHashMap;

import hos.core.interfaces.Function1;


/**
 * 单例服用
 */
public final class SingletonWeakManager implements ISingletonManager {

    private WeakHashMap<Class<?>, ISingletonWrapper> mMap;

    private SingletonWeakManager() {
    }

    @NonNull
    public static SingletonWeakManager get() {
        return SingletonManagerHolder.getSingletonManager();
    }

    private static final class SingletonManagerHolder {

        private volatile static SingletonWeakManager singletonManager = null;

        static SingletonWeakManager getSingletonManager() {
            if (singletonManager == null) {
                synchronized (SingletonManagerHolder.class) {
                    if (singletonManager == null) {
                        return singletonManager = new SingletonWeakManager();
                    }
                }
            }
            return singletonManager;
        }
    }

    @Nullable
    private <P extends ISingletonWrapper> P getInstance(Class<P> key) {
        ISingletonWrapper singletonWrapper = get().getSingletonWrappers().get(key);
        if (singletonWrapper == null) {
            return null;
        }
        return key.cast(singletonWrapper);
    }

    /**
     * 单例，弱引用
     */
    @Override
    @NonNull
    public <P extends ISingletonWrapper> P getInstance(@NonNull final Class<P> key,
                                                       @NonNull final Function1<Class<P>, P> function) {
        P singleton = getInstance(key);
        if (singleton != null) {
            return singleton;
        }
        singleton = function.apply(key);
        register(key, singleton);
        return singleton;
    }


    @NonNull
    private WeakHashMap<Class<?>, ISingletonWrapper> getSingletonWrappers() {
        if (mMap != null) {
            return mMap;
        }
        return mMap = new WeakHashMap<>();
    }

    private <P extends ISingletonWrapper> void register(@NonNull Class<P> key,
                                                        @NonNull ISingletonWrapper ifWrapper) {
        get().getSingletonWrappers().put(key, ifWrapper);
    }


}
