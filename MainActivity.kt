package ca.cegepgarneau.casino

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.content.res.Configuration
import ca.cegepgarneau.casino.ui.theme.CasinoTheme

@OptIn(ExperimentalMaterial3Api::class)
class MainActivity : ComponentActivity() {
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        sp = getSharedPreferences("PREFS", MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CasinoTheme {
                Scaffold(
                    containerColor = Color(0xFFA7B0BC),
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text(stringResource(R.string.app_name), fontSize = 20.sp, fontWeight = FontWeight.Bold) },
                            colors =
                                TopAppBarDefaults.topAppBarColors(
                                    containerColor = Color(0xFFA4D3ED),
                                ),
                        )
                    },
                ) { innerPadding ->
                    connexion(modifier = Modifier.padding(innerPadding), sp)
                }
            }
        }
    }
}

//écran de connexion qui permets à quelqu'un de se connecter avec leur nom
@Composable
fun connexion(
    modifier: Modifier = Modifier,
    sp: SharedPreferences,
) {
    var username by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current
    val configuration = LocalConfiguration.current
    val paddingBouton = if (configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) 370.dp else 120.dp

    Column(
        modifier =
            modifier
                .fillMaxSize()
                .background(Color(0xFFB9E369))
                .padding(top = 16.dp)
                .background(Color(0xFFC0855A)),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.username), fontSize = 16.sp, fontWeight = FontWeight.Bold) },
            singleLine = true,
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val editor = sp.edit()
                if (!sp.contains(username)) {
                    editor.putInt(username, 15)
                }
                editor.putString("SESSION", username)
                editor.apply()
                val intent = Intent(context, CasinoActivity::class.java)
                context.startActivity(intent)
            },
            enabled = true,
            modifier = Modifier.fillMaxWidth().padding(horizontal = paddingBouton)
        ) {
            Text(stringResource(R.string.titre_connexion), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
