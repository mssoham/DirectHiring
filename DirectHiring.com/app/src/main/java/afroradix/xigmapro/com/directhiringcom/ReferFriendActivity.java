package afroradix.xigmapro.com.directhiringcom;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;

import utilities.data_objects.DirectHiringModel;

public class ReferFriendActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnFb;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    String content="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_friend);

        btnFb = (Button)findViewById(R.id.btnFb);
        btnFb.setOnClickListener(this);

        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {

            @Override
            public void onSuccess(Sharer.Result result) {

            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFb:

                String orig = DirectHiringModel.getInstance().userBean.getEmail();
                String bytesEncoded = Base64.encodeToString(orig.getBytes(), Base64.NO_WRAP);

                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("DirectHiring")
                            .setContentDescription(
                                    "Share this URL and earn credit points")
                            .setContentUrl(Uri.parse("http://xigmapro.website/dev4/directhiring/public/refer/fb_verify/"+ bytesEncoded))
                            .setImageUrl(Uri.parse("http://xigmapro.website/dev4/directhiring/public/resource/site/images/helper_logo.png"))
                            .build();

                    shareDialog.show(linkContent);

                }
                break;
        }
    }

}
