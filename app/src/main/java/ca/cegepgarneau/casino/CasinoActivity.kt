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
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
                Scaffold(modifier = Modifier.fillMaxSize()) {
                        innerPadding ->
                    NavigationCasino(modifier =
                        Modifier.padding(innerPadding), sp)
                }
            }
        }
    }
}

@Composable
fun NavigationCasino(modifier: Modifier = Modifier, sp:
SharedPreferences) {
    var ecranActuel by remember {
        mutableStateOf<Ecran>(Ecran.Accueil) }

    when (ecranActuel) {
        is Ecran.Accueil -> EcranAccueil(
            sp = sp,
            onNaviguerSalle = { ecranActuel = Ecran.Salle },
            onNaviguerBanque = { ecranActuel = Ecran.Banque }
        )
        is Ecran.Banque -> { /* EcranBanque à venir */ }
        is Ecran.Salle -> { /* EcranSalle à venir */ }
    }
}

@Composable
fun EcranAccueil(
    sp: SharedPreferences,
    onNaviguerSalle: () -> Unit,
    onNaviguerBanque: () -> Unit
) {
    val username = sp.getString("SESSION", "") ?: ""
    val solde = sp.getInt(username, 0)

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "${stringResource(R.string.bienvenue)}, $username! Solde: $solde")

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            if (solde == 0) onNaviguerBanque() else
                onNaviguerSalle()
        }) {
            Text(stringResource(R.string.btn_salle))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = onNaviguerBanque) {
            Text(stringResource(R.string.btn_banque))
        }
    }
}