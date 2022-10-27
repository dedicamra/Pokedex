package com.example.pokedex.pokemondetail.pokemonmoves

import androidx.lifecycle.ViewModel
import com.example.pokedex.data.remote.moveinforesponses.MoveInfo
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MoveInfoViewModel @Inject constructor(
    private val repository: PokemonRepository
): ViewModel() {

    suspend fun getMoveInfo(url:String):Resource<MoveInfo>{
        return repository.getMoveInfo(url)
    }

}