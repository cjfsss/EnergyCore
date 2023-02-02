package hos.base.view;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import java.lang.reflect.Method;

import hos.core.AppCompat;
import hos.utils.CoreLog;

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
        Class<? extends IFragmentActivity> target = this.getClass();
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
        } else if (TextUtils.equals(target.getName(), "androidx.fragment.app.Fragment")) {
            try {
                return (Bundle) target.getMethod("getArguments").invoke(this);
            } catch (Exception e) {
                CoreLog.e("getBundle", e);
            }
        } else if (this instanceof Dialog) {
            IFragmentActivity<?> activity = (IFragmentActivity<?>) activity();
            return activity.getBundle();
        }
        return null;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    default A activity() {
        Class<? extends IFragmentActivity> target = this.getClass();
        if (this instanceof Activity) {
            return (A) this;
        } else if (this instanceof Fragment) {
            Fragment fragment = (Fragment) this;
            Activity activity = fragment.getActivity();
            if (activity != null) {
                return (A) activity;
            }
        } else if (TextUtils.equals(target.getName(), "androidx.fragment.app.Fragment")) {
            try {
                Activity activity = (Activity) target.getMethod("getActivity").invoke(this);
                if (activity != null) {
                    return (A) activity;
                }
            } catch (Exception e) {
                CoreLog.e("activity", e);
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

    @SuppressWarnings("rawtypes")
    default void finish() {
        Class<? extends IFragmentActivity> target = this.getClass();
        if (this instanceof Activity) {
            activity().finish();
        } else if (TextUtils.equals(target.getName(), "androidx.fragment.app.DialogFragment")) {
            try {
                target.getMethod("dismiss").invoke(this);
            } catch (Exception e) {
                CoreLog.e("activity", e);
            }
        } else if (this instanceof Fragment) {
            activity().finish();
        } else if (TextUtils.equals(target.getName(), "androidx.fragment.app.Fragment")) {
            activity().finish();
        } else if (this instanceof Dialog) {
            Dialog dialog = (Dialog) this;
            dialog.dismiss();
        } else {
            activity().finish();
        }
    }
}
