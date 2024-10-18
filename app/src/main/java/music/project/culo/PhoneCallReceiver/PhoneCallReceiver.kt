package music.project.culo.PhoneCallReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import music.project.culo.ForegroundService.ForegroundService
import music.project.culo.Utils.MusicActions


class PhoneCallReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val telephonyManager : TelephonyManager = context?.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        telephonyManager.listen(object : PhoneStateListener(){
            override fun onCallStateChanged(state: Int, phoneNumber: String?) {
                super.onCallStateChanged(state, phoneNumber)
                Intent(context, ForegroundService::class.java).also { intent ->
                    intent.action = MusicActions.stop.toString()
                    context.startForegroundService(intent)
                }
            }
                                                             },
            PhoneStateListener.LISTEN_CALL_STATE)
    }
}