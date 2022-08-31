package com.tutorial.chapter1.myapplication.data.di

import android.content.Context
import androidx.room.Room
import com.tutorial.chapter1.myapplication.data.local.RestaurantDao
import com.tutorial.chapter1.myapplication.data.local.RestaurantDb
import com.tutorial.chapter1.myapplication.data.remote.RestaurantRepository
import com.tutorial.chapter1.myapplication.data.remote.RestaurantsApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ActivityScoped
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 *
 * SingletonComponent: Allows us to scope dependencies to the lifetime of the application,
 * @Singleton is requested, Dagger will provide the same instance
 *
 * ActivityComponent: Allows us to scope dependencies to the lifetime of an Activity
 * @ActivityScoped annotation. If the Activity is recreated, a new instance of the dependency will be provided
 *
 * ActivityRetainedComponent: Allows us to scope dependencies to the lifetime of an Activity
 * surpassing its recreation upon orientation change
 * @ActivityRetainedScoped If the Activity is recreated upon orientation change, the same instance of the dependency is provided
 *
 * ViewModelComponent: Allows us to scope dependencies to the lifetime of a ViewModel with
 * @ViewModelScoped annotation
 *
 * @Binds annotation tells Hilt which implementation to use when it needs to provide an instance of an interface
 *
 * provide different implementations of the same type as dependencies such as
 * @Qualifier
 * @Retention(AnnotationRetention.BINARY)
 * annotation class AuthInterceptorOkHttpClient
 *
 * or
 *
 * @OtherInterceptorOkHttpClient
 * @Provides
 * fun provideAuthInterceptorOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient
 */


@Module
@InstallIn(SingletonComponent::class)
object RestaurantModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://restaurants-827eb-default-rtdb.firebaseio.com/")
            .build()
    }

    @Provides
    fun provideRetrofitApi(retrofit: Retrofit): RestaurantsApiService {
        return retrofit.create(RestaurantsApiService::class.java)
    }

    @Singleton //create only one instance, allowing us to save memory
    @Provides
    fun provideRoomDatabase(@ApplicationContext appContext: Context): RestaurantDb {
        return Room.databaseBuilder(
            appContext,
            RestaurantDb::class.java,
            "restaurants_database"
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    fun provideRoomDao(database: RestaurantDb): RestaurantDao {
        return database.dao
    }

}

@Module
@InstallIn(ViewModelComponent::class)
object OtherModule {
    @Provides
    @ViewModelScoped
    fun providePermissionUtils(): PermissionUtils {
        return PermissionUtils()
    }
}
class PermissionUtils() {
    fun request() { }
}