package com.amrendra.popularmovies.api;

import com.amrendra.popularmovies.utils.MoviesConstants;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by Amrendra Kumar on 24/11/15.
 */
public class MovieClientService {

    private static MoviesEndPointInterface moviesEndPointInterface = null;

    private MovieClientService() {
    }

    public static MoviesEndPointInterface getInstance() {
        if (moviesEndPointInterface == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(MoviesConstants.API_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            moviesEndPointInterface = retrofit.create(MoviesEndPointInterface.class);
        }
        return moviesEndPointInterface;
    }
}
