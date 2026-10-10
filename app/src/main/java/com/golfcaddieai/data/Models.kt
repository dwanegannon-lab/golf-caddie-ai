package com.golfcaddieai.data

import kotlin.math.roundToInt

data class UserProfile(
    val name: String,
    val loginMethod: String
)

data class Club(
    val id: String,
    val brand: String,
    val model: String,
    val clubType: String,
    val isPutter: Boolean = false,
    val baselineDistanceYards: Int
)

data class Course(
    val id: String,
    val name: String,
    val city: String,
    val par: Int,
    val distanceMiles: Double,
    val latitude: Double,
    val longitude: Double,
    val holeCount: Int = 18
)

data class WeatherStatus(
    val temperatureF: Int,
    val condition: String,
    val windSpeedMph: Int,
    val windDirection: String,
    val golfingImpact: String
)

data class Shot(
    val id: String,
    val clubType: String,
    val distanceYards: Int,
    val x: Float,
    val y: Float
)

data class Hole(
    val number: Int,
    val par: Int,
    val teeColor: String,
    val distanceToGreenYards: Int,
    val shots: List<Shot> = emptyList()
)

data class PlayerScore(
    val name: String,
    val strokes: Int
)

enum class RoundFormat {
    FULL_18,
    FRONT_9,
    BACK_9
}

fun createSampleClubs(): List<Club> = listOf(
    Club("driver", "TaylorMade", "Stealth 2", "Driver", baselineDistanceYards = 280),
    Club("3wood", "Callaway", "Paradym", "3 Wood", baselineDistanceYards = 245),
    Club("5i", "Titleist", "T100", "5 Iron", baselineDistanceYards = 170),
    Club("pw", "Cleveland", "RTX ZipCore", "Pitching Wedge", baselineDistanceYards = 120),
    Club("putter", "Odyssey", "White Hot", "Putter", isPutter = true, baselineDistanceYards = 32)
)

fun createSampleCourses(): List<Course> = listOf(
    Course("sierra", "Sierra Ridge", "Reno", 72, 11.4, 39.5309, -119.8141),
    Course("pine", "Pine Valley", "Las Vegas", 71, 9.8, 36.1699, -115.1398),
    Course("lagoon", "Lagoon Links", "Scottsdale", 70, 13.6, 33.4942, -111.9261)
)

fun createSampleWeather(latitude: Double, longitude: Double): WeatherStatus {
    val temp = ((latitude * 4.0) + 60).roundToInt()
    val wind = ((longitude * -2.0) + 18).roundToInt().coerceIn(4, 28)
    val direction = listOf("NW", "W", "SW", "S", "SE", "E", "NE", "N").random()
    val conditions = listOf("Clear", "Windy", "Partly Cloudy", "Humid", "Mild").random()
    val impact = when {
        wind > 18 -> "Wind will challenge your tee shots and approach clubs."
        temp > 82 -> "Hot conditions may reduce carry distance on the course."
        else -> "Playable conditions with a slight breeze and solid club selection."
    }
    return WeatherStatus(temp, conditions, wind, direction, impact)
}
