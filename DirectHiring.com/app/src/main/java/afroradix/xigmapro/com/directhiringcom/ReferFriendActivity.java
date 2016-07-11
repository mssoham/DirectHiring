package afroradix.xigmapro.com.directhiringcom;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.MessageDialog;
import com.facebook.share.widget.ShareDialog;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import utilities.data_objects.DirectHiringModel;

public class ReferFriendActivity extends AppCompatActivity implements View.OnClickListener {

    private Button btnFb;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

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
                if (ShareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentTitle("DirectHiring")
                            .setContentDescription(
                                    "Share this URL and earn credit points")
                            .setContentUrl(Uri.parse("http://xigmapro.website/dev4/directhiring/public/refer/fb_verify/"+ DirectHiringModel.getInstance().userBean.getEmail()))
                            .setImageUrl(Uri.parse("http://xigmapro.website/dev4/directhiring/public/resource/site/images/helper_logo.png"))
                            .build();

                    shareDialog.show(linkContent);

                }
                break;
        }
    }

    private void shareAppLinkViaFacebook() throws MalformedURLException {
        String urlToShare = "http://xigmapro.website/dev4/directhiring/public/refer/fb_verify/"+ DirectHiringModel.getInstance().userBean.getEmail();

        /*try {
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
        }*/

        URL url = new URL("http://xigmapro.website/dev4/directhiring/public/resource/site/images/helper_logo.png");
        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse(urlToShare))
                .build();
        try{
            Bitmap image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(image)
                    .build();
            SharePhotoContent content1 = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();
        }catch (IOException e) {
            e.printStackTrace();
        }

    }
}
