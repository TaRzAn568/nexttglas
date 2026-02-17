package com.nexttglas.nexttglas.ui.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.collections.forEach
import kotlin.let

enum class UserRole(val title: String, val description: String) {
    LEARNER(
        "The Learners",
        "For pre-primary, pupils, and students ready to excel"
    ),
    TUTOR(
        "The Tutors",
        "For qualified educators and professionals ready to monetize their expertise"
    ),
    INSTITUTION(
        "The Institutions",
        "For schools and universities. Partner with us to scale your growth and earn a recurring 40% commission on every completed session"
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistrationPreferenceScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedRole by remember { mutableStateOf<UserRole?>(null) }
    val primaryColor = MaterialTheme.colorScheme.primary
    val backgroundColor = MaterialTheme.colorScheme.background

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Nexttglas",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = primaryColor,
                            letterSpacing = 0.5.sp
                        )
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    TextButton(onClick = { /* No action */ }) {
                        Text("Help")
                    }
                }
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 8.dp
            ) {
                Button(
                    onClick = {
                        selectedRole?.let {
                            Log.d("RegistrationPreference", "Selected Role: ${it.name}")
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .height(56.dp),
                    enabled = selectedRole != null,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        "Continue",
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = primaryColor.copy(alpha = 0.08f),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, primaryColor.copy(alpha = 0.2f))
            ) {
                Text(
                    text = "Complete your setup by choosing your role",
                    modifier = Modifier.padding(16.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    color = primaryColor,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Role Selection Stack
            Column(
                modifier = Modifier.selectableGroup(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                UserRole.entries.forEach { role ->
                    RoleCard(
                        role = role,
                        isSelected = selectedRole == role,
                        onClick = { selectedRole = role },
                        primaryColor = primaryColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun RoleCard(
    role: UserRole,
    isSelected: Boolean,
    onClick: () -> Unit,
    primaryColor: Color
) {
    val elevation = if (isSelected) 6.dp else 1.dp
    val borderColor = if (isSelected) primaryColor else Color.LightGray.copy(alpha = 0.5f)
    val borderWidth = if (isSelected) 2.dp else 1.dp

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .selectable(
                selected = isSelected,
                onClick = onClick,
                role = Role.RadioButton
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = elevation),
        border = BorderStroke(borderWidth, borderColor)
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = role.title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                ),
                color = if (isSelected) primaryColor else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = role.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 20.sp
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegistrationPreferenceScreenPreview() {
    RegistrationPreferenceScreen()
}
