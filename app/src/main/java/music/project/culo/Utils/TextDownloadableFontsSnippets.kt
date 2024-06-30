package Fonts

import androidx.compose.ui.text.googlefonts.GoogleFont
import music.project.culo.R
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
// ...



val provider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

val fontName = GoogleFont("Inter")
val fontName2 = GoogleFont("Playfair Display")

val fontFamily = FontFamily(
    Font(googleFont = fontName, fontProvider = provider)
)

val fontFamily2 = FontFamily(
    Font(googleFont = fontName2, fontProvider = provider)
)