package fr.ikallali.fdjtest.base;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import fr.ikallali.fdjtest.R;
import fr.ikallali.fdjtest.custom.CustomToolbar;


public class BaseActivity extends AppCompatActivity {

    protected static final String TAG = "BaseActivity";
    protected CustomToolbar toolbar;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        // setup the toolbar
        toolbar = findViewById(R.id.toolbar);
        if(toolbar != null) {
            toolbar.setTitle(getTitle().toString());
        }

    }


    /**
     * Show a toast message
     * @param msg the message
     */
    protected void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    /**
     * Show loading dialog
     */
    protected void showLoadingDialog(){
        if(pd == null) {
            pd = new ProgressDialog(this);
            pd.setMessage(getText(R.string.loading));
            pd.setCancelable(true);
        }
        pd.show();
    }

    /**
     * Dismiss loading dialog
     */
    protected void dismissLoadingDialog(){
        if(pd != null && pd.isShowing()){
            pd.dismiss();
        }
    }

    /**
     * Show alert dialog
     * @param msg message
     */
    protected void showAlert(String msg) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name))
                .setMessage(msg)
                .setCancelable(false)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(getText(R.string.ok), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    @Override
    public void onBackPressed() {

        if(isTaskRoot()) {
            //Ask the user if they want to quit
            new AlertDialog.Builder(this)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setTitle(R.string.quit)
                    .setMessage(R.string.really_quit)
                    .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //Stop the activity
                            finish();
                        }
                    })
                    .setNegativeButton(R.string.no, null)
                    .show();
        }
        else{
            super.onBackPressed();
            //set transition back animation
            overridePendingTransition( R.anim.right_in, R.anim.right_out);
        }

    }

    @Override
    protected void onStop() {
        dismissLoadingDialog();
        super.onStop();
    }
}
