# Filmie
Movie browsing app, made using [themoviedb](https://www.themoviedb.org/) api,
as part of Udacity : Popular Movies (Part-1)

## Add API key
In app/build.gradle replace "COPY YOUR MOVIE DB API KEY HERE" with your API key 


## Preview
![Preview Video](../master/preview/filmie1.gif)   ![Preview Video](../master/preview/filmie2.gif)

## Overview
Find implementation details [here](../master/Implementation.txt)

## Third Party Libraries Used

- [Stetho](https://github.com/facebook/stetho) [For debugging]
- [Picasso](http://square.github.io/picasso/) [For image loading/caching]
- [Butterknife](http://jakewharton.github.io/butterknife/) [For binding]
- [Retrofit](http://square.github.io/retrofit/) [For rest api calls]
- [Gson](http://mvnrepository.com/artifact/com.squareup.retrofit/converter-gson/2.0.0-beta1) [For json handling]

# Learning Resources(Acknowledgement)

1. Working with secret API keys in Android Studio/Gradle 
[Link](http://www.rainbowbreeze.it/environmental-variables-api-key-and-secret-buildconfig-and-android-studio/
)
2. Retrofit
[Link](http://square.github.io/retrofit/)
[Link](http://www.vogella.com/tutorials/Retrofit/article.html)
[Link](https://bekk.github.io/android101/pages/retrofit.html)
[Link](http://blog.robinchutaux.com/blog/a-smart-way-to-use-retrofit/)
[Link](https://guides.codepath.com/android/Consuming-APIs-with-Retrofit)
3. Lifecycle of fragment 
[Link](http://www.javacodegeeks.com/2013/06/android-fragment-lifecycle-multiple-screen-support.html)
4. Loaders
[Link](http://www.androiddesignpatterns.com/2012/08/implementing-loaders.html)
5. Pallete
[Link](https://github.com/codepath/android_guides/wiki/Dynamic-Color-using-Palettes)
6. Share Element Transition
[Link](https://github.com/codepath/android_guides/wiki/Shared-Element-Activity-Transition)
7. EndlessScroll
[Link](https://github.com/codepath/android_guides/wiki/Endless-Scrolling-with-AdapterViews-and-RecyclerView)
8. Spinner in toolbar
[Link](http://android-pratap.blogspot.in/2015/01/spinner-in-toolbar-example-in-android.html)
[Link](https://dabx.io/2015/01/02/material-design-spinner-toolbar-style-fix/)
9. Android Studio Assets
[Link](https://romannurik.github.io/AndroidAssetStudio/index.html)

## ToDo (Part-2)
1. Endless Scroll
2. Dynamic change color of drawable without flickering
3. Add network connection listener and handling.
4. Add favourite, using db and content provider
5. Provide search bar
6. Add custom star rating.
7. Add trailers to view pager at top with poster brackdrop.
8. Retaining state across device rotation, and restart of app.