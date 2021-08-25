package hos.core.compat;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.content.Context;
import android.content.res.Resources;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;

/**
 * <p>Title: AnimUtils </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/6/18 9:26
 */
public class AnimCompat {

    public static Animation loadAnimation(@NonNull Context context, int nextAnim) {
        if (nextAnim <= 0) {
            return null;
        }
        String dir = context.getResources().getResourceTypeName(nextAnim);
        boolean isAnim = "anim".equals(dir);
        if (!isAnim) {
            return null;
        }
        // try AnimationUtils first
        try {
            return AnimationUtils.loadAnimation(context, nextAnim);
            // A null animation may be returned and that is acceptable
        } catch (Resources.NotFoundException e) {
            throw e; // Rethrow it -- the resource should be found if it is provided.
        } catch (RuntimeException e) {
            // Other exceptions can occur when loading an Animator from AnimationUtils.
        }
        return null;
    }

    public static Animator loadAnimator(@NonNull Context context, int nextAnim) {
        if (nextAnim <= 0) {
            return null;
        }
        String dir = context.getResources().getResourceTypeName(nextAnim);
        boolean isAnim = "anim".equals(dir);
        if (isAnim) {
            return null;
        }
        // try Animator
        try {
            return AnimatorInflater.loadAnimator(context, nextAnim);
        } catch (RuntimeException e) {
            if (isAnim) {
                // Rethrow it -- we already tried AnimationUtils and it failed.
                throw e;
            }
        }
        return null;
    }
}
