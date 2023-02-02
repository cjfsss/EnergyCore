package hos.base;

import android.app.Dialog;

import hos.base.view.IViewLoading;


/**
 * <p>Title: LoadingDialog </p>
 * <p>Description: 对话框 </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2023-02-02 16:05
 */
public class LoadingDialog implements IViewLoading {
    private Dialog mProgressDialog;

    public LoadingDialog(Dialog dialog) {
        this.mProgressDialog = dialog;
    }

    @Override
    public void showLoading(int titleId) {
        if (mProgressDialog == null) {
            return;
        }
        showLoading(mProgressDialog.getContext().getResources().getString(titleId));
    }

    @Override
    public void showLoading(String title, boolean isDismissOnBackPressed, boolean isDismissOnTouchOutside) {
        if (mProgressDialog == null) {
            return;
        }
        mProgressDialog.setTitle(title);
        mProgressDialog.setCanceledOnTouchOutside(isDismissOnTouchOutside);
        mProgressDialog.setCancelable(isDismissOnBackPressed);
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        hideLoading();
        mProgressDialog = null;
    }
}
