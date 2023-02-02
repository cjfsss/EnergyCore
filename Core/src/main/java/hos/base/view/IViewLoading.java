package hos.base.view;

/**
 * <p>Title: IViewLoading </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2021/2/5 10:30
 */
public interface IViewLoading {

    /**
     * 显示加载进度
     */
    default void showLoading() {
        showLoading("正在加载...");
    }

    /**
     * 显示加载进度
     */
    default void showLoading(String title) {
        showLoading(title, true);
    }

    /**
     * 显示加载进度
     *
     * @param titleId 要显示的文字
     */
    void showLoading(int titleId);

    /**
     * 设置点击弹窗外面是否关闭弹窗，默认为true
     *
     * @param title                   要显示的文字
     * @param isDismissOnBackPressed  设置按下返回键是否关闭弹窗，默认为true
     * @param isDismissOnTouchOutside 设置点击弹窗外面是否关闭弹窗，默认为true
     */
    void showLoading(
            String title,
            boolean isDismissOnBackPressed,
            boolean isDismissOnTouchOutside
    );

    /**
     * 设置点击弹窗外面是否关闭弹窗，默认为true
     *
     * @param title 要显示的文字
     */
    default void showLoading(String title, boolean isDismissOutside) {
        showLoading(title, isDismissOutside, isDismissOutside);
    }

    /**
     * 隐藏加载进度
     */
    void hideLoading();

    void onDestroy();
}
