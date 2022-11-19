package de.tum.hack.Bloomberg.Challenge.api

import de.tum.hack.Bloomberg.Challenge.models.SnapshotOrder

class BuySellOrdersResponse(
    val buy: MutableList<SnapshotOrder> = mutableListOf(),
    val sell: MutableList<SnapshotOrder> = mutableListOf()
)
