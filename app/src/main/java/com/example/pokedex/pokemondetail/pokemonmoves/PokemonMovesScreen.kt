package com.example.pokedex.pokemondetail.pokemonmoves

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pokedex.data.remote.responses.Move
import com.example.pokedex.pokemondetail.dominantColorDS
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Composable
fun PokemonMovesScreen(
    pokemonMoves: List<Move>,
    navController: NavController,
) {
    Column(modifier = Modifier.padding(top = 20.dp, bottom = 100.dp)) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            items(pokemonMoves.size) {
                MoveCard(
                    navController=navController,
                    pokemonMove = pokemonMoves[it],
                    modifier = Modifier
                        .aspectRatio(1f)
                        .padding(8.dp)
                )
            }

        }
    }
}


@Composable
fun MoveCard(
    navController: NavController,
    pokemonMove: Move,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        dominantColorDS,
                        MaterialTheme.colors.surface
                    )
                )
            )
            .clickable {
                val url= URLEncoder.encode(pokemonMove.move.url, StandardCharsets.UTF_8.toString())
                navController.navigate(
                    "move_info_screen/${url}"
                )
            }
    ) {
        Text(
            text = pokemonMove.move.name,
            color = MaterialTheme.colors.onSurface,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}
