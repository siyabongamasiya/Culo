package music.project.culo.Presentation.PostDetailsScreen

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import android.view.View
import androidx.core.graphics.applyCanvas
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import music.project.culo.Utils.writeBitmap
import java.io.File
import javax.inject.Inject

@HiltViewModel
class PostDetailsScreenViewModel @Inject constructor() : ViewModel() {


    fun CreateImage(view : View) : String{
//        val file = File(DESTINATIONPATH,"screenshot.png")
//        val bitmap = Bitmap.createBitmap(view.width,view.height,Bitmap.Config.ARGB_8888).applyCanvas {
//            view.draw(this)
//        }
//
//        viewModelScope.launch(Dispatchers.IO) {
//            bitmap.let { bitmap ->
//                file.writeBitmap(bitmap,Bitmap.CompressFormat.PNG,85)
//            }
//        }
//
    return ""
    }
}