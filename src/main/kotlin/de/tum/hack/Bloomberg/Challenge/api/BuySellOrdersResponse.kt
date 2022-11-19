package de.tum.hack.Bloomberg.Challenge.api

import de.tum.hack.Bloomberg.Challenge.models.SnapshotOrder

data class BuySellOrdersResponse (
    val buy: List<SnapshotOrder>,
    val sell: List<SnapshotOrder>
)
