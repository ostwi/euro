package com.wodkamocni.euro2021.repositories

import com.wodkamocni.euro2021.model.Game
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface GameRepository : CrudRepository <Game, Long>
