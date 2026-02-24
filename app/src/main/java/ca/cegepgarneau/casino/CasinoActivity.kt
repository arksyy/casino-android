package ca.cegepgarneau.casino

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import ca.cegepgarneau.casino.screens.ecranAccueil
import ca.cegepgarneau.casino.screens.ecranBanque
import ca.cegepgarneau.casino.screens.ecranSalle
import ca.cegepgarneau.casino.ui.theme.CasinoTheme

// activité du casino qui affiche l'écran d'accueil, et qui gère la navigation entre les écrans du casino
class CasinoActivity : ComponentActivity() {
    lateinit var sp: SharedPreferences

    // initialisation des shared preferences
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sp = getSharedPreferences("PREFS", MODE_PRIVATE)
        enableEdgeToEdge()
        setContent {
            CasinoTheme {
                navigationCasino(sp = sp, onDeconnexion = {
                    sp.edit().remove("SESSION").apply()
                    finish()
                })
            }
        }
    }

    // réinitialisation des shared preferences à chaque fois qu'on reprends
    override fun onResume() {
        super.onResume()
        sp = getSharedPreferences("PREFS", MODE_PRIVATE)
    }
}

// composable qui gère la navigation entre les écrans du casino
@Composable
fun navigationCasino(
    sp: SharedPreferences,
    onDeconnexion: () -> Unit,
) {
    var ecranActuel by rememberSaveable { mutableStateOf("Accueil") }

    when (ecranActuel) {
        // navigation vers l'écran de la banque
        "Banque" -> {
            ecranBanque(
                sp = sp,
                onRetour = { ecranActuel = "Accueil" },
                onDeconnexion = onDeconnexion,
            )
        }

        // navigation vers l'écran de la salle de jeu
        "Salle" -> {
            ecranSalle(
                sp = sp,
                onRetour = { ecranActuel = "Accueil" },
                onDeconnexion = onDeconnexion,
            )
        }

        // navigation vers l'écran d'accueil 
        else -> {
            ecranAccueil(
                sp = sp,
                onNaviguerSalle = { ecranActuel = "Salle" },
                onNaviguerBanque = { ecranActuel = "Banque" },
                onDeconnexion = onDeconnexion,
            )
        }
    }
}
