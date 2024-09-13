package music.project.culo.di

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import music.project.culo.CuloApp
import music.project.culo.Data.local.SongDatabase
import javax.inject.Named


@Module
@InstallIn(SingletonComponent::class)
object TestAppModule {

    @Provides
    @Named("Database")
    fun ProvideDatabase(@ApplicationContext context : Context) : SongDatabase{
        return Room.inMemoryDatabaseBuilder(context, SongDatabase::class.java)
            .allowMainThreadQueries()
            .build()
    }
}