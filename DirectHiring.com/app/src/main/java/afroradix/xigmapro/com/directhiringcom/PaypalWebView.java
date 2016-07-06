package afroradix.xigmapro.com.directhiringcom;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import shared_pref.SharedStorage;
import utilities.data_objects.UserBean;

public class PaypalWebView extends AppCompatActivity {
    WebView web_pay;
    UserBean userBean=new UserBean();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_web_view);
        web_pay=(WebView)findViewById(R.id.web_pay);

        String user_id= SharedStorage.getValue(getApplicationContext(), "UserId");;

        //String url="http://website-design-company.in/dev13/recruitershack/payment?check=on&u_id="+user_id;
        String url="http://website-design-company.in/dev13/recruitershack/payment?check=on&u_id="+user_id;
        web_pay.getSettings().setLoadsImagesAutomatically(true);
        web_pay.getSettings().setJavaScriptEnabled(true);
        web_pay.setScrollBarStyle(web_pay.SCROLLBARS_INSIDE_OVERLAY);
        web_pay.loadUrl(url);

        web_pay.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                view.loadUrl(url);
                return true;
            }

            public void onPageFinished(WebView view, String url) {
                Log.e("Loading Complete-->", "Finished loading URL: " + url);

                if (url.equals("http://website-design-company.in/dev13/recruitershack/index.php/payment/thankyou") || url.equals("http://website-design-company.in/dev13/recruitershack/index.php/recruiter/membership")) {
                    //startActivity(new Intent(PaypalWebviewActivity.class,RecruiterActivity.class));
                    /*AlertDialog.Builder alerBuilder = new AlertDialog.Builder(PaypalWebviewActivity.this);
                    alerBuilder.setMessage("Your payment is complete. Click Continue to proceed");
                    alerBuilder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent intMain = new Intent(PaypalWebviewActivity.this, RecruiterActivity.class);
                            startActivity(intMain);
                        }
                    });

                    AlertDialog alertDialog=alerBuilder.create();
                   alertDialog.show();*/
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            userBean.setStatus("premium");
                            Intent intMain = new Intent(PaypalWebView.this, CriteriaType.class);
                            startActivity(intMain);
                        }
                    }, 1000);
                }
            }

            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {

            }
        });
    }
}
