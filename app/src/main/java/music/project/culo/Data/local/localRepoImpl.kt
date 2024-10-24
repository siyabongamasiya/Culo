package music.project.culo.Data.local

import android.content.Context
import kotlinx.coroutines.flow.Flow
import music.project.culo.Domain.LocalRepository.LocalRepo
import music.project.culo.Domain.Model.Playlist
import music.project.culo.Domain.Model.Post
import music.project.culo.Domain.Model.Song
import music.project.culo.Utils.getSongsFromMediaStorage

class LocalRepoImpl  : LocalRepo{
    override suspend fun getSongsFromDevice(context: Context): Flow<List<Song>> = context.contentResolver.getSongsFromMediaStorage(context)
    override suspend fun getSongsFromRoom(context: Context) : Flow<List<Song>>{
        return SongDatabase.getDatabaseInsatance(context).getDAO().getSongs()
    }

    override suspend fun getPostsFromRoom(context: Context): Flow<List<Post>> {
        return  SongDatabase.getDatabaseInsatance(context).getDAO().getPosts()
    }

    override suspend fun getPlaylistsFromRoom(context: Context): Flow<List<Playlist>> {
        return SongDatabase.getDatabaseInsatance(context).getDAO().getPlaylists()
    }



    override suspend fun saveSong(context: Context,song: Song) {
        val roomDao = SongDatabase.getDatabaseInsatance(context).getDAO()
        roomDao.SaveSong(song)
    }

    override suspend fun savePost(context: Context, post: Post) {
        val roomDao = SongDatabase.getDatabaseInsatance(context).getDAO()
        roomDao.SavePost(post)
    }

    override suspend fun savePlaylist(context: Context, playlist: Playlist) {
        SongDatabase.getDatabaseInsatance(context).getDAO().SavePlaylist(playlist)
    }



    override suspend fun deletePost(context: Context, post: Post) {
        SongDatabase.getDatabaseInsatance(context).getDAO().DeletePost(post)
    }

    override suspend fun deletePlaylist(context: Context, playlist: Playlist) {
        SongDatabase.getDatabaseInsatance(context).getDAO().DeletePlaylists(playlist)
    }

    override suspend fun updatePlaylist(context: Context, playlist: Playlist) {
        SongDatabase.getDatabaseInsatance(context).getDAO().UpdatePlaylist(playlist)
    }
}