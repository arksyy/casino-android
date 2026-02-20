package ca.cegepgarneau.casino

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import ca.cegepgarneau.casino.ui.theme.CasinoTheme

class MainActivity : ComponentActivity() {
    lateinit var sp: SharedPreferences
    override fun onCreate(savedInstanceState: Bundle?) {
        sp = getSharedPreferences("PREFS", MODE_PRIVATE)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CasinoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Connexion(modifier = Modifier.padding(innerPadding), sp)
                }
            }
        }
    }
}

@Composable
fun Connexion(modifier: Modifier = Modifier, sp: SharedPreferences) {
    var username by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center) {

        Text(
            text = "MineDrop",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(24.dp))

        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Nom d'utilisateur") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

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
            enabled = true
        ) {
            Text("Connnexion")
        }
    }
}
