package fr.ikallali.fdjtest.apis;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;


public interface Api {

    @GET
    Call<RssService> rssFeed(@Url String urlFeed);

}
