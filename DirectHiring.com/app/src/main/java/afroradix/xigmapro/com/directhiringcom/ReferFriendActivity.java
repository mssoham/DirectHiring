package afroradix.xigmapro.com.directhiringcom;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import utilities.data_objects.DirectHiringModel;

public class ReferFriendActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);

        btnFb = (Button)findViewById(R.id.btnFb);
        btnFb.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFb:
                shareAppLinkViaFacebook();
                break;
        }
    }

    private void shareAppLinkViaFacebook() {
        String urlToShare = "http://xigmapro.website/dev4/directhiring/public/refer/fb_verify/"+ DirectHiringModel.getInstance().userBean.getEmail();

        try {
            Intent intent1 = new Intent();
            intent1.setClassName("com.facebook.katana", "com.facebook.katana.activity.composer.ImplicitShareIntentHandler");
            intent1.setAction("android.intent.action.SEND");
            intent1.setType("text/plain");
            intent1.putExtra("android.intent.extra.TEXT", urlToShare);
            intent1.putExtra("android.intent.extra.EXTRA_STREAM", "http://xigmapro.website/dev4/directhiring/public/resource/site/images/helper_logo.png");
            startActivity(intent1);
        } catch (Exception e) {
            // If we failed (not native FB app installed), try share through SEND
            Intent intent = new Intent(Intent.ACTION_SEND);
            String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
            startActivity(intent);
        }
    }
}
