package com.golfcaddieai.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.golfcaddieai.data.Club
import com.golfcaddieai.data.Course
import com.golfcaddieai.data.Hole
import com.golfcaddieai.data.PlayerScore
import com.golfcaddieai.data.RoundFormat
import com.golfcaddieai.data.UserProfile
import com.golfcaddieai.data.createSampleClubs
import com.golfcaddieai.data.createSampleCourses
import com.golfcaddieai.data.createSampleWeather

class GolfViewModel : ViewModel() {
    var user by mutableStateOf(UserProfile("Golfer", "Guest"))
        private set

    var clubs by mutableStateOf(createSampleClubs())
        private set

    var courses by mutableStateOf(createSampleCourses())
        private set

    var selectedCourse by mutableStateOf(courses.first())
        private set

    var weather by mutableStateOf(createSampleWeather(39.5309, -119.8141))
        private set

    var selectedRoundFormat by mutableStateOf(RoundFormat.FULL_18)
        private set

    var scorecard by mutableStateOf(
        listOf(
            PlayerScore("Alex", 79),
            PlayerScore("Sam", 84)
        )
    )
        private set

    var holes by mutableStateOf(
        listOf(
            Hole(1, 4, "White", 420),
            Hole(2, 5, "Blue", 480),
            Hole(3, 3, "Red", 175),
            Hole(4, 4, "White", 360)
        )
    )
        private set

    val gpsLocationLabel: String
        get() = "Reno, NV"

    fun getCurrentHour(): Int = java.time.LocalTime.now().hour

    fun loginWithName(name: String) {
        user = UserProfile(name, "Name")
    }

    fun loginWithGoogle() {
        user = UserProfile("Google Golfer", "Google")
    }

    fun addClub(brand: String, model: String, clubType: String, baselineDistance: Int, isPutter: Boolean) {
        clubs = clubs + Club(
            id = "club-${clubs.size + 1}",
            brand = brand,
            model = model,
            clubType = clubType,
            baselineDistanceYards = baselineDistance,
            isPutter = isPutter
        )
    }

    fun startRound(course: Course) {
        selectedCourse = course
        weather = createSampleWeather(course.latitude, course.longitude)
    }

    fun setRoundFormat(format: RoundFormat) {
        selectedRoundFormat = format
    }

    fun aiClubRecommendation(): String {
        val recommendation = clubs.filter { !it.isPutter }.maxByOrNull { it.baselineDistanceYards }
        val clubName = recommendation?.clubType ?: "Driver"
        return "AI suggests a ${clubName} for this hole based on your average distance and current wind conditions."
    }

    fun addPlayer(playerName: String) {
        if (playerName.isNotBlank()) {
            scorecard = scorecard + PlayerScore(playerName, 0)
        }
    }
}
