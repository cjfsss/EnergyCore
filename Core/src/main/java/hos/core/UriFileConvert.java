package hos.core;

import android.content.Context;
import android.net.Uri;

/**
 * <p>Title: UriForFileCallback </p>
 * <p>Description:  </p>
 * <p>Company: www.mapuni.com </p>
 *
 * @author : 蔡俊峰
 * @version : 1.0
 * @date : 2022/3/20 20:25
 */
public interface UriFileConvert {

    Uri getUriForFile(Context context, String file);

}
