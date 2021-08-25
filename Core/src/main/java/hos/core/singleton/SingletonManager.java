package hos.core.singleton;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;
import androidx.collection.ArrayMap;

import java.util.Map;


/**
 * 单例服用
 */
public class SingletonManager implements ISingletonManager {

    private Map<Class<?>, ISingletonWrapper> mMap;

    private SingletonManager() {
    }

    @NonNull
    public static SingletonManager get() {
        return SingletonManagerHolder.getSingletonManager();
    }

    private static final class SingletonManagerHolder {

        private volatile static SingletonManager singletonManager = null;

        static SingletonManager getSingletonManager() {
            if (singletonManager == null) {
                synchronized (SingletonManagerHolder.class) {
                    if (singletonManager == null) {
                        return singletonManager = new SingletonManager();
                    }
                }
            }
            return singletonManager;
        }
    }

    @Nullable
    private <P extends ISingletonWrapper> P getInstance(@NonNull Class<P> key) {
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
                                                       @NonNull final Function<Class<P>, P> function) {
        P singleton = getInstance(key);
        if (singleton != null) {
            return singleton;
        }
        singleton = function.apply(key);
        register(key, singleton);
        return singleton;
    }


    @NonNull
    private Map<Class<?>, ISingletonWrapper> getSingletonWrappers() {
        if (mMap != null) {
            return mMap;
        }
        return newSingletonMap();
    }

    protected Map<Class<?>, ISingletonWrapper> newSingletonMap() {
        return mMap = new ArrayMap<>();
    }

    private <P extends ISingletonWrapper> void register(@NonNull Class<P> key,
                                                        @NonNull ISingletonWrapper ifWrapper) {
        get().getSingletonWrappers().put(key, ifWrapper);
    }


}
