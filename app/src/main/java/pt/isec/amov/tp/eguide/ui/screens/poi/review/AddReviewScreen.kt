package pt.isec.amov.tp.eguide.ui.screens.poi.review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import pt.isec.amov.tp.eguide.R
import pt.isec.amov.tp.eguide.ui.screens.Screens
import pt.isec.amov.tp.eguide.ui.viewmodels.LocationViewModel
import pt.isec.amov.tp.eguide.utils.firebase.FAuthUtil

@Composable
fun AddReview(navController: NavController, viewModel: LocationViewModel) {
    // State for form inputs
    val (reviewTitle, setReviewTitle) = remember { mutableStateOf("") }
    val (reviewText, setReviewText) = remember { mutableStateOf("") }
    val (rating, setRating) = remember { mutableStateOf(0L) }
    val userId = FAuthUtil.currentUser?.uid.orEmpty()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.add_review_title),
            fontWeight = FontWeight.Bold
        )

        OutlinedTextField(
            value = reviewTitle,
            onValueChange = setReviewTitle,
            label = { Text(stringResource(id = R.string.add_review_title)) },
            modifier = Modifier.fillMaxWidth()
        )

        OutlinedTextField(
            value = reviewText,
            onValueChange = setReviewText,
            label = { Text(stringResource(id = R.string.add_review)) },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        OutlinedTextField(
            value = rating.toString(),
            onValueChange = { newRating -> setRating(newRating.toLongOrNull() ?: 0L) },
            label = { Text(stringResource(id = R.string.rating)) },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = {
                viewModel.addReview(
                    userId = userId,
                    reviewTitle = reviewTitle,
                    reviewText = reviewText,
                    rating = rating
                )
                navController.navigate(Screens.LIST_REVIEWS.route)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.submit_review))
        }
    }
}