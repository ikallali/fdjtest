package fr.ikallali.fdjtest.custom;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import fr.ikallali.fdjtest.R;


public class CustomToolbar extends LinearLayout {

    public interface OnRightButtonsClick {
        void click(int direction);
    }
    public interface OnBackButtonClick {
        void click();
    }
    public interface OnTitleClick {
        void click();
    }


    private TextView titleView;
    private ImageButton backButton;
    private LinearLayout rightButtons;
    private ImageButton previousButton;
    private ImageButton nextButton;

    private OnBackButtonClick backClickListner;
    private OnTitleClick titleClickListner;
    private OnRightButtonsClick rightButtonClickListner;



    public CustomToolbar(Context context) {
        super(context);
        init(null);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomToolbar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {


        inflate(getContext(), R.layout.custom_toolbar, this);

        this.backButton = findViewById(R.id.toolbarBackButton);
        this.titleView = findViewById(R.id.toolbarTitle);
        this.rightButtons = findViewById(R.id.toolbarRightButtons);
        this.previousButton = findViewById(R.id.toolbarPreviousButton);
        this.nextButton = findViewById(R.id.toolbarNextButton);


        Boolean showBackButton = false;
        Boolean showRightButtons = false;
        Boolean showTitle = true;
        String title = null;

        if(attrs != null) {
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.CustomToolbar);

            showTitle = a.getBoolean(R.styleable.CustomToolbar_showTitle, showTitle);
            title = a.getString(R.styleable.CustomToolbar_title);

            showBackButton = a.getBoolean(R.styleable.CustomToolbar_showBackButton, showBackButton);
            showRightButtons = a.getBoolean(R.styleable.CustomToolbar_showRightButtons, showRightButtons);

            a.recycle();
        }

        if(showTitle != null) {
            showTitle(showTitle);
        }

        if(title != null) {
            setTitle(title);
        }

        showBackButton(showBackButton);
        showRightButtons(showRightButtons);

        titleView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(titleClickListner != null){
                    titleClickListner.click();
                }
            }
        });

        backButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if(backClickListner != null) {
                    backClickListner.click();
                } else {
                    Activity activity = getActivity();
                    if(activity != null) {
                        activity.onBackPressed();
                    }
                }
            }
        });

        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                nextOrPreviousButtonClicked(1);
            }
        });

        previousButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                nextOrPreviousButtonClicked(-1);
            }
        });

    }

    private Activity getActivity() {
        Context context = getContext();
        while (context instanceof ContextWrapper) {
            if (context instanceof Activity) {
                return (Activity)context;
            }
            context = ((ContextWrapper)context).getBaseContext();
        }
        return null;
    }

    public void setTitle(String title){
        titleView.setText(title);
    }

    public void showBackButton(Boolean visible){
        backButton.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showRightButtons(Boolean visible){
        rightButtons.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void showTitle(Boolean visible){
        titleView.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public void setOnClickRightButtons(OnRightButtonsClick listener){
        rightButtonClickListner = listener;
    }

    void nextOrPreviousButtonClicked(int direction){
        if(rightButtonClickListner != null)
            rightButtonClickListner.click(direction);
    }

    public void setOnClickTitle(OnTitleClick listener){
        titleClickListner = listener;
    }

    public void setOnClickBackButton(OnBackButtonClick listener){
        backClickListner = listener;
    }



    public void enablePreviousButton(boolean state){
        previousButton.setEnabled(state);
        previousButton.setAlpha(state ? 255 : 128);
    }
    public void enableNextButton(boolean state){
        nextButton.setEnabled(state);
        nextButton.setAlpha(state ? 255 : 128);
    }


}
