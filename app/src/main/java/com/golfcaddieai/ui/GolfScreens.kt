package com.golfcaddieai.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.golfcaddieai.data.Club
import com.golfcaddieai.data.Course
import com.golfcaddieai.data.UserProfile
import com.golfcaddieai.ui.theme.GolfGreen
import com.golfcaddieai.ui.theme.GolfGold
import com.golfcaddieai.ui.theme.GolfSand
import com.golfcaddieai.viewmodel.GolfViewModel

@Composable
fun LoginScreen(navController: NavController, viewModel: GolfViewModel) {
    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        GolfSand,
                        Color(0xFFE8F5E9),
                        Color(0xFFECFDF5)
                    )
                )
            )
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Golf Caddie AI",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = GolfGreen
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Log in and get AI-powered golf guidance.",
            style = MaterialTheme.typography.bodyLarge,
            color = Color.DarkGray
        )
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Your name") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (name.isNotBlank()) {
                    viewModel.loginWithName(name.trim())
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Continue with name")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                viewModel.loginWithGoogle()
                navController.navigate("home") {
                    popUpTo("login") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Filled.Person, contentDescription = null)
            Spacer(modifier = Modifier.size(8.dp))
            Text("Continue with Google")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: GolfViewModel) {
    val greeting = "Good ${if (viewModel.getCurrentHour() < 12) "morning" else "afternoon"}, ${viewModel.user.name}!"
    val weather = viewModel.weather

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Home") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = GolfGreen.copy(alpha = 0.1f))
                ) {
                    Column(Modifier.padding(20.dp)) {
                        Text(text = greeting, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Filled.LocationOn, contentDescription = null, tint = GolfGreen)
                            Text(text = "${viewModel.gpsLocationLabel} • ${weather.temperatureF}°F")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = weather.condition, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
                        Text(text = weather.golfingImpact)
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFDBEAFE))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Weather impact", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Wind: ${weather.windSpeedMph} mph ${weather.windDirection}")
                        Text("Conditions: ${weather.condition}")
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Nearby golf courses", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Button(onClick = { navController.navigate("clubs") }) {
                        Text("Club bag")
                    }
                }
            }

            items(viewModel.courses) { course ->
                CourseRecommendationCard(course = course, onStartRound = {
                    viewModel.startRound(course)
                    navController.navigate("round")
                })
            }
        }
    }
}

@Composable
private fun CourseRecommendationCard(course: Course, onStartRound: () -> Unit) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(course.name, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    Text("${course.city} • ${course.distanceMiles} mi away")
                }
                Text("Par ${course.par}", fontWeight = FontWeight.SemiBold)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text("Course profile: scenic fairways, rolling greens, and strong wind exposure.")
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(onClick = onStartRound, modifier = Modifier.height(38.dp)) {
                    Text("Start round")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubBagScreen(navController: NavController, viewModel: GolfViewModel) {
    var brand by remember { mutableStateOf("") }
    var model by remember { mutableStateOf("") }
    var type by remember { mutableStateOf("Driver") }
    var distance by remember { mutableStateOf("250") }
    var isPutter by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Golf Bag") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                . fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = GolfGold.copy(alpha = 0.12f))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Add club", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(value = brand, onValueChange = { brand = it }, label = { Text("Brand") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("Model") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = type, onValueChange = { type = it }, label = { Text("Club") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(value = distance, onValueChange = { distance = it }, label = { Text("Baseline distance (yards)") }, modifier = Modifier.fillMaxWidth())
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                if (brand.isNotBlank() && model.isNotBlank()) {
                                    viewModel.addClub(
                                        brand = brand.trim(),
                                        model = model.trim(),
                                        clubType = type.trim(),
                                        baselineDistance = distance.toIntOrNull() ?: 0,
                                        isPutter = isPutter
                                    )
                                    brand = ""
                                    model = ""
                                    type = "Driver"
                                    distance = "250"
                                    isPutter = false
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = null)
                            Spacer(modifier = Modifier.size(8.dp))
                            Text("Add to bag")
                        }
                    }
                }
            }

            item {
                Text("Club bag", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            items(viewModel.clubs) { club ->
                ClubBagItem(club = club)
            }

            item {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Back to home")
                }
            }
        }
    }
}

@Composable
private fun ClubBagItem(club: Club) {
    Card(
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(club.clubType, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                if (club.isPutter) {
                    Text("Putter")
                }
            }
            Text("${club.brand} ${club.model}")
            Text("Baseline: ${club.baselineDistanceYards} yards")
        }
    }
}

@Composable
fun RoundScreen(navController: NavController, viewModel: GolfViewModel) {
    val weather = viewModel.weather
    val selected = viewModel.selectedRoundFormat

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Round") })
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE0F2FE))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("Wind", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Text("${weather.windSpeedMph} mph ${weather.windDirection}")
                        Text("Impact: ${weather.golfingImpact}")
                    }
                }
            }

            item {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    listOf("18 holes", "Front 9", "Back 9").forEach { label ->
                        val format = when (label) {
                            "18 holes" -> com.golfcaddieai.data.RoundFormat.FULL_18
                            "Front 9" -> com.golfcaddieai.data.RoundFormat.FRONT_9
                            else -> com.golfcaddieai.data.RoundFormat.BACK_9
                        }
                        OutlinedButton(
                            onClick = { viewModel.setRoundFormat(format) },
                            modifier = Modifier
                        ) {
                            Text(label)
                        }
                    }
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("${viewModel.selectedCourse.name} • ${selected.name}", fontWeight = FontWeight.Bold)
                            Button(onClick = {}) {
                                Text("Recenter")
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        MapPlaceholder(
                            shots = listOf(
                                androidx.compose.ui.geometry.Offset(0.3f, 0.45f),
                                androidx.compose.ui.geometry.Offset(0.6f, 0.62f)
                            )
                        )
                    }
                }
            }

            item {
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF0FDF4))
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text("AI recommendation", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(viewModel.aiClubRecommendation())
                    }
                }
            }

            item {
                Text("Hole stats", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            }

            items(viewModel.holes) { hole ->
                HoleStatCard(hole)
            }

            item {
                Button(
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Finish round")
                }
            }
        }
    }
}

@Composable
private fun MapPlaceholder(shots: List<androidx.compose.ui.geometry.Offset>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp)
            .background(Color(0xFFDCFCE7), RoundedCornerShape(18.dp))
            .border(2.dp, Color(0xFF86EFAC), RoundedCornerShape(18.dp))
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .size(180.dp)
                .background(Color(0xFFBBF7D0), RoundedCornerShape(24.dp))
        )
        shots.forEach { offset ->
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .background(Color(0xFF166534), RoundedCornerShape(50))
                    .align(Alignment.TopStart)
                    .padding(
                        start = (offset.x * 180).dp,
                        top = (offset.y * 180).dp
                    )
            )
        }
    }
}

@Composable
private fun HoleStatCard(hole: com.golfcaddieai.data.Hole) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Hole ${hole.number}", fontWeight = FontWeight.Bold)
                Text("Par ${hole.par}")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text("Tee color: ${hole.teeColor}")
            Text("Distance to green: ${hole.distanceToGreenYards} yards")
            Text("Shots tracked: ${hole.shots.size}")
        }
    }
}

@Composable
fun ScorecardScreen(navController: NavController, viewModel: GolfViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Scorecard") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(text = "${viewModel.user.name}'s scorecard", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(16.dp))
            viewModel.scorecard.forEach { player ->
                Card(
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(player.name)
                        Text("${player.strokes} strokes")
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { navController.navigate("home") },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Return home")
            }
        }
    }
}
