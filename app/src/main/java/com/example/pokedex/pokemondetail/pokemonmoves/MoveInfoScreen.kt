package com.example.pokedex.pokemondetail.pokemonmoves

import android.widget.TableRow
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.pokedex.data.remote.moveinforesponses.MoveInfo
import com.example.pokedex.ui.theme.*
import com.example.pokedex.util.Resource
import com.example.pokedex.util.parseMoveTypeToColor

var typeColor = Color.Blue

@Composable
fun MoveInfoScreen(
    navController: NavController,
    moveUrl: String,
    viewModel: MoveInfoViewModel = hiltViewModel()
) {
    val moveInfo = produceState<Resource<MoveInfo>>(initialValue = Resource.Loading()) {
        value = viewModel.getMoveInfo(moveUrl)
    }.value

    typeColor = if (moveInfo.data?.type != null)
        parseMoveTypeToColor(moveInfo.data.type)
    else MaterialTheme.colors.background
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        typeColor
                    )
                )
            )
            .padding(bottom = 16.dp)
    ) {
        TopSection(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.2f)
                .align(Alignment.TopCenter)
        )
        StateWrapper(
            navController = navController,
            moveInfo = moveInfo,
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 100.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 16.dp
                )
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colors.surface)
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            loadingModifier = Modifier
                .size(20.dp)
                .align(Alignment.Center)
                .padding(16.dp)
        )
    }
}

@Composable
fun TopSection(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.TopStart,
        modifier = modifier
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.Black,
                        Color.Transparent
                    )
                )
            )
    ) {
        Icon(
            imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable { navController.popBackStack() }
        )
    }
}

@Composable
fun StateWrapper(
    navController: NavController,
    moveInfo: Resource<MoveInfo>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (moveInfo) {
        is Resource.Success -> {
            MoveInfoSection(
                moveInfo = moveInfo.data!!,
                modifier = modifier.offset(y = (-20).dp),
                navController = navController
            )
        }
        is Resource.Error -> {
            Text(
                text = moveInfo.message!!,
                color = Color.Red,
                modifier = modifier
            )
        }
        is Resource.Loading -> {
            CircularProgressIndicator(
                color = MaterialTheme.colors.primary,
                modifier = loadingModifier
            )
        }
    }
}

@Composable
fun MoveInfoSection(
    moveInfo: MoveInfo,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 50.dp)
    ) {
        Text(
            text = moveInfo.name.uppercase(),
            modifier = Modifier.fillMaxWidth(),
            color = parseMoveTypeToColor(moveInfo.type),
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)

        )

        //TableRow(tHead = "Accuracy:", tValue = moveInfo.accuracy.toString())
        PercentsRow(
            tHead = "Accuracy:",
            statValue = moveInfo.accuracy,
            statMaxValue = 100,
            statColor = TypeGrass
        )
        TableRow(tHead = "Power points:", tValue = moveInfo.pp.toString())
        TableRow(tHead = "Power:", tValue = moveInfo.power.toString())
        TableRow(tHead = "Damage:", tValue = moveInfo.damage_class.name)
        TableRow(tHead = "Type:", tValue = moveInfo.type.name)
    }
}

@Composable
fun TableRow(
    modifier: Modifier = Modifier,
    tHead: String,
    tValue: String
) {
    Column() {

        Row(modifier = modifier) {
            Text(
                text = tHead,
                modifier = Modifier.weight(0.4f),
                color = typeColor,
                fontWeight = FontWeight.Bold,
            )

            Text(
                text = tValue,
                modifier = Modifier.weight(0.6f),
                color = MaterialTheme.colors.onSurface,
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun PercentsRow(
    tHead: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    animDuration: Int = 1000,
    animDelay: Int = 0,
    modifier: Modifier = Modifier
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            durationMillis = animDuration,
            delayMillis = animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Column() {

        Row(modifier = modifier) {
            Text(
                text = tHead,
                modifier = Modifier.weight(0.4f),
                color = typeColor,
                fontWeight = FontWeight.Bold,
            )
            Box(
                modifier = Modifier
                    .height(30.dp)
                    .weight(0.6f)
                    .clip(CircleShape)
                    .background(
                        if (isSystemInDarkTheme()) {
                            Color(0xFF7C7C7C)
                        } else {
                            Color.LightGray
                        }
                    )
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth(curPercent.value)
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(statColor)
                        .padding(horizontal = 8.dp)
                ) {}

                Text(
                    text = (curPercent.value * statMaxValue).toInt().toString() + "%",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .padding(end = 8.dp)
                )
            }

        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}