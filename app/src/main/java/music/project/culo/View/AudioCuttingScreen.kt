package music.project.culo.View

import Fonts.fontFamily
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import music.project.culo.R
import music.project.culo.Utils.Instruction
import music.project.culo.Utils.OutofRange

@Composable
fun AudioCuttingScreen(navController: NavHostController){
    Scaffold (topBar = {
        topSection(navController = navController)
    },
        bottomBar = {
            audioCuttingControls(modifier = Modifier
                .fillMaxWidth()
                .background(
                    brush = Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface,
                            MaterialTheme.colorScheme.secondaryContainer
                        )
                    )
                )
                .padding(10.dp),navController)
        }){paddingValues ->
        midSectionAudioCutting(navController = navController, paddingValues = paddingValues)
    }
}

@Composable
fun midSectionAudioCutting(navController: NavHostController,
                           paddingValues: PaddingValues){
    
    ConstraintLayout(modifier = Modifier
        .fillMaxSize()
        .background(
            brush = Brush.linearGradient(
                listOf(
                    MaterialTheme.colorScheme.background,
                    Color.DarkGray
                )
            )
        )
        .padding(
            top = paddingValues.calculateTopPadding(),
            start = 10.dp,
            end = 10.dp
        )) {
        
        val (instruction,add) = createRefs()
        
        
        //instruction
        Column(modifier = Modifier
            .fillMaxWidth()
            .constrainAs(instruction) {
                top.linkTo(
                    parent.top,
                    margin = 10.dp
                )
            }, verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally) {
            
            Text(text = Instruction,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSecondary)
            
            Divider(modifier = Modifier.height(2.dp),
                color = Color.Black)
            
        }
        
        //Advertisement
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .constrainAs(add) {
                    top.linkTo(
                        instruction.bottom,
                        margin = 30.dp
                    )
                },
            contentScale = ContentScale.Fit,
            painter = painterResource(id = R.drawable.advert),
            contentDescription = "advert")
        
    }
    
}

@Composable
fun audioCuttingControls(modifier: Modifier,navController: NavHostController){
    val currentRange = rememberSaveable {
        mutableStateOf(Pair("00:00","00:30"))
    }
    
    val currentTimeMs = rememberSaveable {
        mutableIntStateOf(120000)
    }
    
    val progressTime = rememberSaveable {
        mutableLongStateOf(120000)
    }

    val currentSong = rememberSaveable {
        mutableStateOf("")
    }

    var totalTimeMs = rememberSaveable {
        mutableStateOf(240000L)
    }

    var currentTime = rememberSaveable {
        mutableStateOf("")
    }

    var totalTime = rememberSaveable {
        mutableStateOf("")
    }

    ConstraintLayout(modifier = modifier) {
        val (times, progressbar,rangetext,title,button) = createRefs()

        LaunchedEffect(key1 = currentTimeMs.value) {

            //calculate total time
            val Ttotalsecs = totalTimeMs.value / 1000
            val TMinutes = Ttotalsecs / 60
            val Tsecs = Ttotalsecs % 60



            //formating
            var Tminutesformatted = ""
            var Tsecsformatted = ""

            //adding zeros if less than 9
            if (TMinutes <= 9) {
                Tminutesformatted = "0${TMinutes}"
            } else {
                Tminutesformatted = "${TMinutes}"
            }

            if (Tsecs <= 9) {
                Tsecsformatted = "0${Tsecs}"
            } else {
                Tsecsformatted = "${Tsecs}"
            }

            totalTime.value = "$Tminutesformatted : $Tsecsformatted"


            //calculate current time
            val Ctotalsecs = currentTimeMs.value / 1000
            val CMinutes = Ctotalsecs / 60
            val Csecs = Ctotalsecs % 60


            //formating current time
            var Cminutesformatted = ""
            var Csecsformatted = ""

            //adding zeros if less than 9
            if (CMinutes <= 9) {
                Cminutesformatted = "0${CMinutes}"
            } else {
                Cminutesformatted = "${CMinutes}"
            }

            if (Csecs <= 9) {
                Csecsformatted = "0${Csecs}"
            } else {
                Csecsformatted = "${Csecs}"
            }



            currentTime.value = "$Cminutesformatted : $Csecsformatted"

            //calculating total time after 30 seconds
            val CtotalsecsRange = (currentTimeMs.value / 1000) + 30
            val CMinutesRange = CtotalsecsRange / 60
            val CsecsRange = CtotalsecsRange % 60

            //formatting time after 30 secs
            //formating current time
            var CminutesRangeformatted = ""
            var CsecsRangeformatted = ""

            //adding zeros if less than 9
            if (CMinutesRange <= 9) {
                CminutesRangeformatted = "0${CMinutesRange}"
            } else {
                CminutesRangeformatted = "${CMinutesRange}"
            }

            if (CsecsRange <= 9) {
                CsecsRangeformatted = "0${CsecsRange}"
            } else {
                CsecsRangeformatted = "${CsecsRange}"
            }

            //detect if cut area is out of range
            var currentTimein30 = ""
            if (CtotalsecsRange > Ttotalsecs) {
                currentRange.value = Pair(OutofRange, OutofRange)
            }else{
                currentTimein30 = "$CminutesRangeformatted : $CsecsRangeformatted"
                currentRange.value = Pair(currentTime.value, currentTimein30)
            }
        }

        //title
        Text(
            modifier = Modifier.constrainAs(title){
                top.linkTo(parent.top,
                    margin = 5.dp)
                centerHorizontallyTo(parent)
            },
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                )){
                    append("Tyla - " )
                }

                withStyle(style = SpanStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Light,
                    fontSize = 16.sp,
                )){
                    append("Water")
                }
            },
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSecondary)

        //current range
        val range = if(!currentRange.value.first.equals(OutofRange)) {"Current - ${currentRange.value.first} to ${currentRange.value.second} (30secs)"}
        else {buildAnnotatedString {
            withStyle(
                style = SpanStyle(
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 16.sp,
                )
            ) {
                append(OutofRange)
            }
        }}
        Text(
            modifier = Modifier.constrainAs(rangetext){
                top.linkTo(title.bottom,
                    margin = 50.dp)
                bottom.linkTo(times.top,
                    margin = 10.dp)
                centerHorizontallyTo(parent)
            },
            text = range.toString(),
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.labelMedium,
            color = if(currentRange.value.first.equals(OutofRange)) Color.Red else MaterialTheme.colorScheme.onSecondary)


        //times
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(times) {
                    bottom.linkTo(
                        progressbar.top,
                        margin = 10.dp
                    )
                },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = currentTime.value,
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = totalTime.value,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        //progress bar
        Slider(
            value = (currentTimeMs.value.toFloat() / totalTimeMs.value.toFloat()),
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.onSecondary,
                activeTrackColor = MaterialTheme.colorScheme.onSecondary,
                inactiveTrackColor = Color.DarkGray
            ),
            onValueChangeFinished = {

            },
            onValueChange = { percentage ->
                currentTimeMs.value = (totalTimeMs.value * percentage).toInt()
            },
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(progressbar) {
                    bottom.linkTo(button.top,
                        margin = 10.dp)
                }
        )

        //done button
        customButton(text = "Done", modifier = Modifier.constrainAs(button){
            bottom.linkTo(parent.bottom,
                margin = 10.dp)
            centerHorizontallyTo(parent)
        }) {
            navController.navigate("Home"){
                popUpTo(0){
                    inclusive = true
                }
            }
        }
    }
}