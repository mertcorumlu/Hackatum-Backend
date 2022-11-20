package de.tum.hack.Bloomberg.Challenge.api

data class PlotResponse(
    val xs: List<Int>,
    val ys: List<Double>,
    val vals: List<Double>
)