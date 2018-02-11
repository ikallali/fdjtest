package fr.ikallali.fdjtest.news;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import fr.ikallali.fdjtest.R;
import fr.ikallali.fdjtest.base.BaseActivity;
import fr.ikallali.fdjtest.custom.CustomToolbar;

import static fr.ikallali.fdjtest.Settings.NEWS_DATE_FORMAT;

public class NewsDetailActivity extends BaseActivity {

    static final String TAG = "NewsDetailActivity";

    int currentIdx;
    ArrayList<News> newsList;


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("fdj.si.detail.newsList", newsList);
        outState.putSerializable("fdj.si.detail.currentIdx", currentIdx);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        toolbar.setTitle("Item detail");


        //get the news item from indent
        newsList = (ArrayList<News>)getIntent().getSerializableExtra("fdj.newsItems");
        currentIdx = getIntent().getIntExtra("fdj.newsIdx",0);

        //if savedInstance, get saved value...
        if (savedInstanceState != null) {
            newsList = (ArrayList<News>) savedInstanceState.getSerializable("fdj.si.detail.newsList");
            currentIdx  = savedInstanceState.getInt("fdj.si.detail.currentIdx",0);
        }


        toolbar.setOnClickRightButtons(new CustomToolbar.OnRightButtonsClick() {
            @Override
            public void click(int direction) {
                int newIdx = currentIdx + direction;
                if(newIdx >= 0 && newIdx <= newsList.size() - 1)
                {
                    currentIdx += direction;
                    showCurrentNews();
                }


            }
        });



        showCurrentNews();

    }



    void showCurrentNews(){

        final News news = newsList.get(currentIdx);

        TextView title = findViewById(R.id.title);
        title.setText(Html.fromHtml(news.getTitle()));


        TextView description = findViewById(R.id.description);
        description.setText(Html.fromHtml(news.getDescription()));

        TextView date = findViewById(R.id.date);
        date.setText(news.getPubDateStr(NEWS_DATE_FORMAT)+ " (" + news.getPubDateAgo() + ")" );

        TextView categories = findViewById(R.id.categories);
        List<String> categoriesList = news.getCategories();
        if(categoriesList.size() > 0) {
            String catString = getText(R.string.keywords)+" : ";
            for (int i = 0; i < categoriesList.size(); i++) {
                if (i > 0)
                    catString += ", ";
                catString += categoriesList.get(i);
            }
            categories.setVisibility(View.VISIBLE);
            categories.setText(catString);
        } else {
            categories.setVisibility(View.GONE);
        }

        final ProgressBar pv = findViewById(R.id.imageProgress);
        final ImageView imageView = findViewById(R.id.imageView);

        // If image url exist, we load it with Glide
        if(news.getImage() != "") {

            pv.setVisibility(View.VISIBLE);
            RequestOptions requestOptions = new RequestOptions()
                    .centerCrop();

            Glide
                    .with(this)
                    .load(news.getImage())
                    //.apply(requestOptions)
                    .listener(new RequestListener<Drawable>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                            // If error with image loading, we hide the imageView
                            Log.d(TAG,getText(R.string.loading_image_error).toString());
                            imageView.setVisibility(View.GONE);
                            pv.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                            pv.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(imageView);
        } else {

            // If no image url, we hide the imageView
            imageView.setVisibility(View.GONE);
            pv.setVisibility(View.GONE);
        }

        // Set the click listener for the read more button
        Button button = findViewById(R.id.button_open_url);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Set the post url & open the webView
                Intent i = new Intent(NewsDetailActivity.this, NewsWebViewActivity.class);
                i.putExtra("fdj.url",news.getLink());
                startActivity(i);
                overridePendingTransition( R.anim.left_in, R.anim.left_out);
            }
        });


        refreshRightButton();

    }

    void refreshRightButton(){
        if(currentIdx == 0)
            toolbar.enablePreviousButton(false);
        else
            toolbar.enablePreviousButton(true);

        if(currentIdx == newsList.size() - 1)
            toolbar.enableNextButton(false);
        else
            toolbar.enableNextButton(true);
    }


}
