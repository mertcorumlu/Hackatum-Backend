package de.tum.hack.Bloomberg.Challenge.api

import de.tum.hack.Bloomberg.Challenge.models.MasterOrder

class BuySellOrdersResponse(
    val buy: MutableList<MasterOrder> = mutableListOf(),
    val sell: MutableList<MasterOrder> = mutableListOf()
)
