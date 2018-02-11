package fr.ikallali.fdjtest.news;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;

import fr.ikallali.fdjtest.R;
import fr.ikallali.fdjtest.apis.Api;
import fr.ikallali.fdjtest.apis.RssService;
import fr.ikallali.fdjtest.base.BaseActivity;
import fr.ikallali.fdjtest.custom.CustomToolbar;

import static fr.ikallali.fdjtest.Settings.RSS_URL;


public class NewsActivity extends BaseActivity {

    static final String TAG = "NewsActivity";


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private NewsAdapter mAdapter;
    private NewsAdapter.ItemClickListener rvClickListener;

    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressDialog pd;

    private Call<RssService> call;
    private ArrayList<News> newsList = new ArrayList<>();


    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putSerializable("fdj.si.list.newsList", newsList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);


        toolbar.setTitle("RSS Feed");

        mLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // refresh !
                loadRss();
            }
        });

        rvClickListener = new NewsAdapter.ItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                Intent i = new Intent(NewsActivity.this, NewsDetailActivity.class);
                i.putExtra("fdj.newsItems",newsList);
                i.putExtra("fdj.newsIdx",position);
                startActivity(i);

                overridePendingTransition( R.anim.left_in, R.anim.left_out);
            }
        };

        if (savedInstanceState != null) {
            newsList  = (ArrayList<News>) savedInstanceState.getSerializable("fdj.si.list.newsList");
            mAdapter = new NewsAdapter(newsList, rvClickListener);
            recyclerView.setAdapter(mAdapter);
        } else {
            loadRss();
        }

        // On refresh au click sur le titre de la toolbar
        toolbar.setOnClickTitle(new CustomToolbar.OnTitleClick() {
            @Override
            public void click() {
                loadRss();
            }
        });


    }




    /**
     * load the RSS Feed
     */
    private void loadRss() {

        if(call != null)
            call.cancel();

        if(!swipeRefreshLayout.isRefreshing()) {
            showLoadingDialog();
        }

        //parse the rss feed url
        URL aURL = null;
        try {
            aURL = new URL(RSS_URL);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        if(aURL == null)
        {
            // error
            Log.d(TAG,getText(R.string.rss_url_invalid).toString());
            showToast(getText(R.string.rss_url_invalid).toString());
            finishLoadingAnimation();

            return;
        }

        // construct the retrofit query
        Retrofit retrofit = new Retrofit.Builder()
                .client(new OkHttpClient())
                .baseUrl(aURL.getProtocol()+"://"+aURL.getHost()+"/")
                .addConverterFactory(SimpleXmlConverterFactory.create())
                .build();

        //call the rssService
        Api service = retrofit.create(Api.class);
        call = service.rssFeed(aURL.getPath());

        call.enqueue(new Callback<RssService>() {
            @Override
            public void onResponse(Call<RssService> call, Response<RssService> response) {
                if (!response.isSuccessful()) {
                    // error
                    showToast(getText(R.string.loading_rss_error).toString());
                }
                else
                {
                    newsList.clear();
                    RssService rssService = response.body();
                    List<RssService.Channel.Item> items = rssService.getItems();
                    for(int i=0; i < items.size();i++)
                    {
                        RssService.Channel.Item item = items.get(i);

                        News news = new News();
                        news.setTitle(item.title);
                        news.setDescription(item.description);
                        news.setPubDate(item.getDate());
                        if(item.enclosures.size() > 0)
                            news.setImage(item.enclosures.get(0).url);
                        news.setLink(item.link);
                        news.setCategories(item.getCategoriesList());

                        newsList.add(news);

                    }
                    mAdapter = new NewsAdapter(newsList, rvClickListener);
                    recyclerView.setAdapter(mAdapter);
                }
                finishLoadingAnimation();
            }

            @Override
            public void onFailure(Call<RssService> call, Throwable t) {
                showToast(getText(R.string.loading_rss_error).toString());
                Log.d(TAG,t.getMessage());

                finishLoadingAnimation();
            }

        });
    }

    void finishLoadingAnimation(){
        if(swipeRefreshLayout.isRefreshing()) {
            swipeRefreshLayout.setRefreshing(false);
        } else {
            dismissLoadingDialog();
        }
    }
}
