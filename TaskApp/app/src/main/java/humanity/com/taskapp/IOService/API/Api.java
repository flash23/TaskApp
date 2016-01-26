package humanity.com.taskapp.IOService.API;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;

import humanity.com.taskapp.Application.TaskApplication;
import humanity.com.taskapp.R;
import humanity.com.taskapp.Utils.NetworkUtil;
import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;

/**
 * Created by mirkomesner on 01/25/16.
 */
//For network requests we are using RetroFit library 1.9... Should be updated to 2.0 but
//We use OkHttpClient so we can use some fancy caching ;)... 2 lines of code to achieve 1hour cache
//If Internet is down previously loaded tasks can be seen so we don't louse time
public class Api {

    public RestAdapter restAdapter;

    private static Api instance = null;

    public static Api getInstance() {
        if(instance == null) {
            instance = new Api();
        }
        return instance;
    }

    private Api()
    {
        Context context = TaskApplication.getContext();


        File httpCacheDirectory = new File(context.getCacheDir(), "responses");

        Cache cache = null;
        try {
            cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        } catch (Exception e) {
            Log.e("OKHttp", "Could not create http cache", e);
        }

        //We use OkHttpClient so we can use some fancy caching ;)
        OkHttpClient okHttpClient = new OkHttpClient();
        if (cache != null) {
            okHttpClient.setCache(cache);
        }

        RequestInterceptor requestInterceptor = new RequestInterceptor() {
            @Override
            public void intercept(RequestInterceptor.RequestFacade request) {
                request.addHeader("Accept", "application/json;versions=1");
                if (NetworkUtil.getConnectivityStatus()>0) {
                    int maxAge = 0;//IF we have internet download dont read from cache
                    request.addHeader("Cache-Control", "public, max-age=" + maxAge);
                } else {
                    int maxStale = 60 * 60;//1 hour cash!!!!!!!!!!!!!!!!!!!!!!!!
                    request.addHeader("Cache-Control", "public, only-if-cached, max-stale=" + maxStale);
                    if(TaskApplication.getCurrentAtivity()!=null)
                    TaskApplication.getCurrentAtivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(TaskApplication.getContext(), TaskApplication.getContext().getResources().getString(R.string.readingcash), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }
        };

        restAdapter  = new RestAdapter.Builder()
                //.setConverter(new CustomConverterNDJSON())////we convert to ndjson
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(ApiConstants.API_URL)
                .setClient(new OkClient(okHttpClient))
                .setRequestInterceptor(requestInterceptor)
                .build();
    }
}
