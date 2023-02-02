package hos.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import hos.core.AppCompat;

/**
 * <p>Title: IFragmentActivity </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/6 20:02
 */
public interface IFragmentActivity<A extends Activity> {

    /**
     * 获取Bundle
     *
     * @return 返回Intent中的Bundle
     */
    default Bundle getBundle() {
        if (this instanceof Activity) {
            Activity activity = (Activity) this;
            Intent intent = activity.getIntent();
            if (intent == null) {
                return null;
            }
            return intent.getExtras();
        } else if (this instanceof Fragment) {
            Fragment fragment = (Fragment) this;
            return fragment.getArguments();
        } else if (this instanceof Dialog) {
            IFragmentActivity<?> activity = (IFragmentActivity<?>) activity();
            return activity.getBundle();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    default A activity() {
        if (this instanceof Activity) {
            return (A) this;
        } else if (this instanceof Fragment) {
            Fragment fragment = (Fragment) this;
            Activity activity = fragment.getActivity();
            if (activity != null) {
                return (A) activity;
            }
        } else if (this instanceof Dialog) {
            Dialog dialog = (Dialog) this;
            Context context = dialog.getContext();
            if (context instanceof Activity) {
                return (A) context;
            }
            Activity activity = dialog.getOwnerActivity();
            if (activity != null) {
                return (A) activity;
            }
        }
        return (A) AppCompat.getCurrentActivity();
    }

    void onViewShowed(View view);

    default void finish() {
        if (this instanceof Activity) {
            activity().finish();
        } else if (this instanceof Fragment) {
            activity().finish();
        } else if (this instanceof Dialog) {
            Dialog dialog = (Dialog) this;
            dialog.dismiss();
        } else {
            activity().finish();
        }
    }
}
