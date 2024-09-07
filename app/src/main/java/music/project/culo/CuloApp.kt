package music.project.culo

import android.app.Application
import android.content.Context

class CuloApp : Application() {
    var context = this

    override fun onCreate() {
        super.onCreate()
        CONTEXT = this
    }
    companion object{
        lateinit var CONTEXT : Context

        fun getContext() : Context{
            return CONTEXT
        }
    }
}