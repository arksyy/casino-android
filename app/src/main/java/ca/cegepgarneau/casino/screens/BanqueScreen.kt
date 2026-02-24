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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.cegepgarneau.casino.R

// composable qui affiche l'écran de la banque du casino
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ecranBanque(
    sp: SharedPreferences,
    onRetour: () -> Unit,
    onDeconnexion: () -> Unit,
) {
    var montant by rememberSaveable { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val paddingBouton = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 370.dp else 120.dp

    Scaffold(
        containerColor = Color(0xFFA7B0BC),
        topBar = {
            // barre de titre avec la déconnexion
            TopAppBar(
                title = { Text(stringResource(R.string.titre_banque), fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFFA4D3ED),
                    ),
                navigationIcon = {
                    IconButton(onClick = onRetour) {
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

            // champ de texte pour entrer le montant à acheter
            TextField(
                value = montant,
                onValueChange = { montant = it },
                label = { Text(stringResource(R.string.nb_jetons), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            // bouton pour acheter des jetons
            Button(
                onClick = {
                    val achat = montant.toIntOrNull() ?: 0
                    if (achat > 0) {
                        val username = sp.getString("SESSION", "") ?: ""
                        val soldeActuel = sp.getInt(username, 0)
                        sp.edit().putInt(username, soldeActuel + achat).apply()
                        onRetour()
                    }
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = paddingBouton)
            ) {
                Text(stringResource(R.string.btn_acheter), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
