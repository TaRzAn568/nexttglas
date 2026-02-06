package com.nexttglas.nexttglas.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nexttglas.nexttglas.R

@Composable
fun HomeScreen(onLogout: () -> Unit = {}) {

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {


        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(12.dp, 80.dp),
            contentAlignment = Alignment.TopCenter,
        ) {
            Text(
                "Welcome to the Confess It App! You can express anything anonymously here..",
                modifier = Modifier.fillMaxWidth(),
                fontSize = 24.sp,
                fontFamily = FontFamily.Cursive,
                fontWeight = FontWeight.Bold
            )


            Image(
                painter = painterResource(R.drawable.nextglas_logo),
                contentDescription = "Confess It Logo",
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
            )
        }

        Button(
            onClick = {},
            modifier = Modifier.padding(bottom = 12.dp)
        ) {
            Text("Get Started")
        }

        Button(
            onClick = onLogout,
            modifier = Modifier.padding(bottom = 80.dp)
        ) {
            Text("Logout")
        }

    }
}

@Composable
@Preview(showBackground = true)
fun HomeScreenPreview() {
    HomeScreen()
}