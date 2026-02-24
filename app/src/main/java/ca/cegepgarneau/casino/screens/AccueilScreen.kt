package ca.cegepgarneau.casino.screens

import android.content.SharedPreferences
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.cegepgarneau.casino.R

// composable qui affiche l'écran d'accueil du casino avec le message de bienvenue, le solde du joueur, et les boutons pour naviguer vers la salle de jeu ou la banque
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ecranAccueil(
    sp: SharedPreferences,
    onNaviguerSalle: () -> Unit,
    onNaviguerBanque: () -> Unit,
    onDeconnexion: () -> Unit,
) {
    val username = sp.getString("SESSION", "") ?: ""
    val solde = sp.getInt(username, 0)
    val configuration = LocalConfiguration.current
    val paddingBouton = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 370.dp else 120.dp

    Scaffold(
        containerColor = Color(0xFFA7B0BC),
        topBar = {
            // barre de titre avec la déconnexion
            TopAppBar(
                title = { Text(stringResource(R.string.titre_accueil), fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFA4D3ED),
                    ),
                navigationIcon = {
                    IconButton(onClick = onDeconnexion) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Up")
                    }
                },
                actions = {
                    IconButton(onClick = onDeconnexion) {
                        Icon(Icons.Default.Menu, contentDescription = "Déconnexion")
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .background(Color(0xFFB9E369))
                    .padding(top = 16.dp)
                    .background(Color(0xFFC0855A)),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // message de bienvenue avec le nom du joueur
            Text(
                text = "${stringResource(R.string.bienvenue)} $username!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // affichage du solde du joueur
            Text(
                text = "${stringResource(R.string.solde)}: $solde $",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            // bouton pour naviguer vers la salle de jeu
            Button(
                onClick = { if (solde == 0) onNaviguerBanque() else onNaviguerSalle() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = paddingBouton),
            ) {
                Text(stringResource(R.string.btn_salle), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // bouton pour naviguer vers la banque
            Button(onClick = onNaviguerBanque, modifier = Modifier.fillMaxWidth().padding(horizontal = paddingBouton)) {
                Text(stringResource(R.string.btn_banque), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
