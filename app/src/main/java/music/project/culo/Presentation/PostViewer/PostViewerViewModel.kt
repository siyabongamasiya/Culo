package music.project.culo.Presentation.PostViewer

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.ViewModel
import music.project.culo.Domain.Model.Post
import music.project.culo.Utils.audioMMIMETYPE

class PostViewerViewModel : ViewModel() {

    fun SharePost(url: String, context: Context){
        val intent = Intent(Intent.ACTION_SEND).apply {
            var uri = Uri.parse(url)
            setType(audioMMIMETYPE)
            putExtra(Intent.EXTRA_STREAM,uri)
        }

        context.startActivity(Intent.createChooser(intent,null))
    }
}