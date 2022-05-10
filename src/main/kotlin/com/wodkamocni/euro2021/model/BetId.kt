package com.wodkamocni.euro2021.model

import java.io.Serializable
import javax.persistence.Embeddable

@Embeddable
data class BetId(
    val gameId: Long,
    val userId: Long,
) : Serializable {
    companion object {
        const val serialVersionUID = 1L
    }
}
