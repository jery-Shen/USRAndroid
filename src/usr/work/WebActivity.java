package usr.work;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

public class WebActivity extends Activity {

	private WebView webView;
	private TextView top_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_web);

		top_title = (TextView) findViewById(R.id.top_title);
		top_title.setText(getIntent().getStringExtra("title"));
		backBtn();

		webView = (WebView) findViewById(R.id.webview);

//		webView.setWebViewClient(new WebViewClient() {
//
//			@Override
//			public boolean shouldOverrideUrlLoading(WebView view, String url) {
//				Intent i = new Intent(Intent.ACTION_VIEW);
//				i.setData(Uri.parse(url));
//				startActivity(i);
//				return true;
//			}
//		});

		webView.getSettings().setJavaScriptEnabled(true);
		webView.addJavascriptInterface(new JsBridge(), "JsBridge");
		webView.loadUrl(getIntent().getStringExtra("url"));

	}

	private void backBtn() {
		findViewById(R.id.btn_exit).setVisibility(View.VISIBLE);
		findViewById(R.id.btn_exit).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (webView.canGoBack()) {
					webView.goBack();// 返回上一页面
				} else {
					finish();
				}
			}
		});
	}

	@Override
	public void onBackPressed() {
		if (webView.canGoBack()) {
			webView.goBack();// 返回上一页面
		} else {
			finish();
		}
	}

	class JsBridge {
		@JavascriptInterface
		public void save() {

		}

		@JavascriptInterface
		public void updatePwd() {

		}
	}
}
