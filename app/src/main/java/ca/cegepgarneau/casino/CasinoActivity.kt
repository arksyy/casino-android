package ca.cegepgarneau.casino

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.cegepgarneau.casino.ui.theme.CasinoTheme

sealed class Ecran {
    data object Accueil : Ecran()

    data object Banque : Ecran()

    data object Salle : Ecran()
}

class CasinoActivity : ComponentActivity() {
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = getSharedPreferences("PREFS", MODE_PRIVATE)
        enableEdgeToEdge()
        setContent {
            CasinoTheme {
                navigationCasino(sp = sp, onDeconnexion = { finish() })
            }
        }
    }
}

@Composable
fun navigationCasino(
    modifier: Modifier = Modifier,
    sp: SharedPreferences,
    onDeconnexion: () -> Unit,
) {
    var ecranActuel by remember {
        mutableStateOf<Ecran>(Ecran.Accueil)
    }

    when (ecranActuel) {
        is Ecran.Accueil -> {
            ecranAccueil(
                sp = sp,
                onNaviguerSalle = { ecranActuel = Ecran.Salle },
                onNaviguerBanque = { ecranActuel = Ecran.Banque },
                onDeconnexion = onDeconnexion,
            )
        }

        is Ecran.Banque -> {
            ecranBanque(
                sp = sp,
                onRetour = { ecranActuel = Ecran.Accueil },
                onDeconnexion = onDeconnexion,
            )
        }

        is Ecran.Salle -> { }
    }
}

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titre_accueil)) },
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
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "${stringResource(R.string.bienvenue)}, $username!")
            Text(text = "Solde: $solde")

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                if (solde == 0) onNaviguerBanque() else onNaviguerSalle()
            }) {
                Text(stringResource(R.string.btn_salle))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNaviguerBanque) {
                Text(stringResource(R.string.btn_banque))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ecranBanque(
    sp: SharedPreferences,
    onRetour: () -> Unit,
    onDeconnexion: () -> Unit,
) {
    var montant by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titre_banque)) },
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
            modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            OutlinedTextField(
                value = montant,
                onValueChange = { montant = it },
                label = { Text(stringResource(R.string.nb_jetons)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(onClick = {
                val username = sp.getString("SESSION", "") ?: ""
                val soldeActuel = sp.getInt(username, 0)
                val achat = montant.toIntOrNull() ?: 0
                sp.edit().putInt(username, soldeActuel + achat).apply()
                onRetour()
            }) {
                Text(stringResource(R.string.btn_acheter))
            }
        }
    }
}
