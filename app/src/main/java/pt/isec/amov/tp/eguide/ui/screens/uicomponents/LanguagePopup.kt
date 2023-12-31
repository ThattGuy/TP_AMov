package pt.isec.amov.tp.eguide.ui.screens.uicomponents

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.data.Language

@Composable
fun LanguageSelector(navController: NavHostController, onDismiss: () -> Unit) {
    var showDialog by remember { mutableStateOf(false) }

    // Sample data (replace with actual language data and flag resources)
    val languages = listOf(
        Language(R.string.english.toString(), R.drawable.english_flag),  // Replace with actual flag resource IDs
        Language(R.string.portuguese.toString(), R.drawable.pt_flag)
    )

    // Clickable Text
    Text(
        "Languages",
        Modifier.clickable { showDialog = true }
    )

    // Dialog for language selection
    if (showDialog) {
        LanguageDialog(languages) { language ->
            showDialog = false
            //setLocale(context, language.locale)
            // Optionally, restart the activity or update UI
            onDismiss()
        }
    }
}

@Composable
fun LanguageDialog(languages: List<Language>, onLanguageSelected: (Language) -> Unit) {
    Dialog(onDismissRequest = { /* TODO: Handle dismiss */ }) {
        Surface(
            shape = MaterialTheme.shapes.medium,
        ) {
            LazyColumn {
                items(languages) { language ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .clickable { onLanguageSelected(language) }
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Display the flag
                        Image(
                            painter = painterResource(id = language.flag),
                            contentDescription = "${language.name} flag"
                        )
                        Spacer(Modifier.width(8.dp))
                        // Display the language name
                        Text(text = language.name)
                    }
                }
            }
        }
    }
}