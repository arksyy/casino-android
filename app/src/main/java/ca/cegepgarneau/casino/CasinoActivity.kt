package ca.cegepgarneau.casino

import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ca.cegepgarneau.casino.ui.theme.CasinoTheme
import kotlin.random.Random

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

    override fun onResume() {
        super.onResume()
        sp = getSharedPreferences("PREFS", MODE_PRIVATE)
    }
}

@Composable
fun navigationCasino(
    sp: SharedPreferences,
    onDeconnexion: () -> Unit,
) {
    var ecranActuel by rememberSaveable { mutableStateOf("Accueil") }

    when (ecranActuel) {
        "Banque" -> {
            ecranBanque(
                sp = sp,
                onRetour = { ecranActuel = "Accueil" },
                onDeconnexion = onDeconnexion,
            )
        }

        "Salle" -> {
            ecranSalle(
                sp = sp,
                onRetour = { ecranActuel = "Accueil" },
                onDeconnexion = onDeconnexion,
            )
        }

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

            Text(
                text = "${stringResource(R.string.bienvenue)} $username!",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "${stringResource(R.string.solde)}: $solde $",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { if (solde == 0) onNaviguerBanque() else onNaviguerSalle() },
                modifier = Modifier.fillMaxWidth().padding(horizontal = paddingBouton),
            ) {
                Text(stringResource(R.string.btn_salle), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(onClick = onNaviguerBanque, modifier = Modifier.fillMaxWidth().padding(horizontal = paddingBouton)) {
                Text(stringResource(R.string.btn_banque), fontSize = 18.sp, fontWeight = FontWeight.Bold)
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
    var montant by rememberSaveable { mutableStateOf("") }
    val configuration = LocalConfiguration.current
    val paddingBouton = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 370.dp else 120.dp

    Scaffold(
        containerColor = Color(0xFFA7B0BC),
        topBar = {
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

            TextField(
                value = montant,
                onValueChange = { montant = it },
                label = { Text(stringResource(R.string.nb_jetons), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val username = sp.getString("SESSION", "") ?: ""
                    val soldeActuel = sp.getInt(username, 0)
                    val achat = montant.toIntOrNull() ?: 0
                    sp.edit().putInt(username, soldeActuel + achat).apply()
                    onRetour()
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = paddingBouton)
            ) {
                Text(stringResource(R.string.btn_acheter), fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ecranSalle(
    sp: SharedPreferences,
    onRetour: () -> Unit,
    onDeconnexion: () -> Unit,
) {
    val modes = listOf(stringResource(R.string.label_pair), stringResource(R.string.label_impair))
    var modeSelectionne by rememberSaveable { mutableStateOf("") }
    var mise by rememberSaveable { mutableStateOf("") }
    var numero by rememberSaveable { mutableStateOf("") }
    val username = sp.getString("SESSION", "") ?: ""
    var solde by rememberSaveable { mutableStateOf(sp.getInt(username, 0)) }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val paddingBouton = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 370.dp else 120.dp

    val msgSoldeInsuffisant = stringResource(R.string.msg_solde_insuffisant)
    val msgGagne = stringResource(R.string.msg_gagne)
    val msgPerdu = stringResource(R.string.msg_perdu)
    val msgPariNul = stringResource(R.string.msg_pari_nul)

    Scaffold(
        containerColor = Color(0xFFA7B0BC),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.titre_salle), fontSize = 20.sp, fontWeight = FontWeight.Bold) },
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

        val onJouer = {
            val miseLong = mise.toLongOrNull() ?: 0L
            val miseInt = if (miseLong < 0L) 0 else miseLong.toInt()

            if (miseLong > solde) {
                Toast.makeText(context, msgSoldeInsuffisant, Toast.LENGTH_SHORT).show()
                onRetour()
            } else {
                val tirage = Random.nextInt(0, 37)
                var gain = 0

                if (modeSelectionne.isNotEmpty()) {
                    val estPair = tirage % 2 == 0
                    val jouePair = modeSelectionne == modes[0]
                    if (estPair == jouePair) gain = miseInt * 2
                } else if (numero.isNotEmpty()) {
                    if (numero.toIntOrNull() == tirage) gain = miseInt * 35
                }

                val nouveauSolde = solde - miseInt + gain
                sp.edit().putInt(username, nouveauSolde).apply()
                solde = nouveauSolde
                val netChange = gain - miseInt
                val messageResultat = when {
                    netChange > 0 -> "$msgGagne +$netChange $"
                    netChange < 0 -> "$msgPerdu $netChange $"
                    else -> msgPariNul
                }
                Toast.makeText(context, messageResultat, Toast.LENGTH_SHORT).show()

                mise = ""
                numero = ""
                modeSelectionne = ""
            }
            Unit
        }

        when (configuration.orientation) {
            Configuration.ORIENTATION_LANDSCAPE -> {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color(0xFFB9E369))
                        .padding(top = 16.dp)
                        .background(Color(0xFFC0855A)),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.weight(1f).padding(16.dp),
                    ) {
                        Text(text = "${stringResource(R.string.solde)}: $solde $", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                        TextField(
                            value = mise,
                            onValueChange = { mise = it },
                            label = { Text(stringResource(R.string.mise), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.weight(1f).padding(start = 12.dp, end = 12.dp, top = 4.dp),
                    ) {
                        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                            modes.forEach { mode ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    RadioButton(
                                        selected = mode == modeSelectionne,
                                        onClick = { modeSelectionne = mode; numero = "" },
                                        enabled = numero.isEmpty(),
                                    )
                                    Text(text = mode, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                        TextField(
                            value = numero,
                            onValueChange = { numero = it; modeSelectionne = "" },
                            label = { Text(stringResource(R.string.numero), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            enabled = modeSelectionne.isEmpty(),
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onJouer,
                            modifier = Modifier.fillMaxWidth().padding(horizontal = 135.dp),
                        ) {
                            Text(stringResource(R.string.btn_jouer), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .background(Color(0xFFB9E369))
                        .padding(top = 16.dp)
                        .background(Color(0xFFC0855A)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = "${stringResource(R.string.solde)}: $solde $", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = mise,
                        onValueChange = { mise = it },
                        label = { Text(stringResource(R.string.mise), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
                        modes.forEach { mode ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = mode == modeSelectionne,
                                    onClick = { modeSelectionne = mode; numero = "" },
                                    enabled = numero.isEmpty(),
                                )
                                Text(text = mode, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TextField(
                        value = numero,
                        onValueChange = { numero = it; modeSelectionne = "" },
                        label = { Text(stringResource(R.string.numero), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        enabled = modeSelectionne.isEmpty(),
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = onJouer,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = paddingBouton),
                    ) {
                        Text(stringResource(R.string.btn_jouer), fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
