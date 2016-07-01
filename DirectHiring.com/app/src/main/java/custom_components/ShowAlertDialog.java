package custom_components;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import afroradix.xigmapro.com.directhiringcom.R;


/**
 * Created by USER on 23-Jun-15.
 */
public class ShowAlertDialog {
    public  static void showAlertDialog(Context context,String message)
    {
        AlertDialog alertDialog = new AlertDialog.Builder(
                context).create();

        // Setting Dialog Title
        //alertDialog.setTitle(title);
        alertDialog.setTitle(R.string.validation_name);

        // Setting Dialog Message
        alertDialog.setMessage(message);

        // Setting Icon to Dialog
        //alertDialog.setIcon(R.drawable.ic_app_icon);

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed

            }
        });

        // Showing Alert Message
        alertDialog.show();
    }

}
