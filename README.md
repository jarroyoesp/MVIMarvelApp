# MARVEL-APP MVVM-MVI

This project is an APP developed to show the Marvel characters information using the open [Marvel API](https://developer.marvel.com/).

# Main features:

This app lets you to:

1) Show character list
2) Show error view with Retry button (only shown when there is no data)
3) Pagination when user gets recyclerView's bottom
4) Character Detail fragment
5) Search Characters (when user writes a name in searchView requests data with a bounce of 500ms)
6) Save/Remove Favorite Characters
7) Get Favorite Characters

# Architecture

<img src="https://github.com/jarroyoesp/MVIMarvelApp/blob/master/images/arch_app.png">

# Languages, libraries and tools used

* [Kotlin](https://kotlinlang.org/)
* [Hilt-Dagger](https://dagger.dev/hilt/)
* [Kotlin-Coroutines + Flow](https://kotlinlang.org/docs/reference/coroutines-overview.html)
* [ConstraintLayout](https://developer.android.com/training/constraint-layout?hl=es-419)
* [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
* [Android View Binding](https://developer.android.com/topic/libraries/view-binding)
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture/index.html)
* [Retrofit](https://square.github.io/retrofit/)
* [OkHttp](https://square.github.io/okhttp/)
* [Moshi](https://github.com/square/moshi)
* [Room](https://developer.android.com/training/data-storage/room)
* [Picasso](https://square.github.io/picasso/)
* [Mockk](https://mockk.io/)

# IMPORTANT Api Keys

To avoid making the Marvel ApiKeys public, the file keys.properties must be added to the root directory. This contains your keys to consume MarvelAPI:
```
api.key="PUBLIC_KEY"
api.private_key="PRIVATE_KEY"
```

To avoid having to add the authentication parameters in each request, we have created an ApiParamsInterceptor. Here we add these params:
```
.addQueryParameter(PARAM_API_KEY, BuildConfig.API_KEY)
.addQueryParameter(PARAM_HASH, generateHash(timestamp))
.addQueryParameter(PARAM_TS, timestamp.toString())
```

# ScreenShots

<img src="https://github.com/jarroyoesp/MVIMarvelApp/blob/master/images/marvel.gif" width="200">
<img src="https://github.com/jarroyoesp/MVIMarvelApp/blob/master/images/app_capture.png" width="200">
<img src="https://github.com/jarroyoesp/MVIMarvelApp/blob/master/images/app_capture_detail.png" width="200">
<img src="https://github.com/jarroyoesp/MVIMarvelApp/blob/master/images/app_capture_favorite.png" width="200">

# Tests

Added UnitTests to check:
- DataSources (NetworkDataSource & diskDataSource)
- Repositories
- UseCases
- ViewModels
- Check request url
- Check request parse data



