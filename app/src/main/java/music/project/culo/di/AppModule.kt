package music.project.culo.di

import android.content.Context
import androidx.media3.exoplayer.ExoPlayer
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import music.project.culo.CuloApp
import music.project.culo.Data.local.LocalRepoImpl
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.FFMPEG.FFMPEGprocessor
import music.project.culo.SongManager.SongManager
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {


    @Provides
    @Singleton
    fun ProvideFFMPEGprocessor() : FFMPEGprocessor {
        return FFMPEGprocessor()
    }

    @Provides
    @Singleton
    fun ProvideLocalRepo() : LocalRepo{
        return LocalRepoImpl()
    }


    @Provides
    @Singleton
    fun ProvidesApp(@ApplicationContext context: Context) : CuloApp{
        return context as CuloApp
    }

    @Provides
    @Singleton
    fun ProvideExoplayer(appContext : CuloApp) : ExoPlayer {
        return ExoPlayer.Builder(appContext).build()
    }


    @Provides
    @Singleton
    fun ProvideSongManager(exoPlayer: ExoPlayer,
                           localRepo: LocalRepo,
                           app: CuloApp
    ) : SongManager {
        return SongManager(exoPlayer = exoPlayer, localrepo = localRepo, context = app)
    }
}