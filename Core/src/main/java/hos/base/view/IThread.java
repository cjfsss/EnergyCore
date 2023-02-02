package hos.base.view;


import android.app.Activity;
import android.view.View;

/**
 * <p>Title: IThread </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/8/21 18:01
 */
public interface IThread {

    default void postIo(Runnable runnable) {
        new Thread(runnable).start();
    }

    default void postOnIo(Runnable runnable) {
        postIo(runnable);
    }

    default void postToMain(Runnable runnable) {
        if (this instanceof IFragmentActivity) {
            IFragmentActivity<?> fragmentActivity = (IFragmentActivity<?>) this;
            fragmentActivity.activity().runOnUiThread(runnable);
        } else if (this instanceof Activity) {
            Activity activity = (Activity) this;
            activity.runOnUiThread(runnable);
        } else if (this instanceof View) {
            View view = (View) this;
            view.post(runnable);
        }
    }

    default void postOnMain(Runnable runnable) {
        postToMain(runnable);
    }
}
