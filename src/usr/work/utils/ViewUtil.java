package usr.work.utils;

import android.content.res.Resources;

public class ViewUtil {
	public static int dpToPx(Resources resources, int dp) {
		return (int) (resources.getDisplayMetrics().density * dp + 0.5f);
	}
}
