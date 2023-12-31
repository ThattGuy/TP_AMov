package pt.isec.amov.tp.eguide.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

data class StudentCredit(
    val name: String,
    val discipline: String,
    val schoolYear: String,
    val course: String
)

@Composable
fun Credits() {
    val studentCredits = listOf(
        StudentCredit("Diogo Gomes", "Mobile Development", "2023/24", "Computer Science"),
        StudentCredit("Raul Pereira", "Mobile Development", "2023/24", "Computer Science (DA)"),
        StudentCredit("Tiago ", "Mobile Development", "2023/24", "Computer Science (DA)")
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("App Credits", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(25.dp))
        studentCredits.forEach { student ->
            StudentCreditView(student)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
fun StudentCreditView(student: StudentCredit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Name: ${student.name}", style = MaterialTheme.typography.titleMedium)
        Text("Discipline: ${student.discipline}", style = MaterialTheme.typography.bodyMedium)
        Text("School Year: ${student.schoolYear}", style = MaterialTheme.typography.bodyMedium)
        Text("Course: ${student.course}", style = MaterialTheme.typography.bodyMedium)
    }
}