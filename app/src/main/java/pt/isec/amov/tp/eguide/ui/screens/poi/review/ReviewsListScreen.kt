package pt.isec.amov.tp.eguide.ui.screens.poi.review

import pt.isec.amov.tp.eguide.data.Review

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
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
fun ReviewItem(
    review: Review, navController: NavController,
    viewModel: LocationViewModel
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
    ) {

        Column(
            modifier = Modifier
                .padding(8.dp)
                .weight(1f)
        ) {
            Text(
                text = review.tittle ?: stringResource(id = R.string.no_name),
                fontWeight = FontWeight.Bold
            )
            Text(text = review.createdBy.toString())
            Text(text = review.review.toString())
            Text(text = review.rating.toString())
        }

    }
}

@Composable
fun ListReviews(
    modifier: Modifier = Modifier,
    viewModel: LocationViewModel,
    navController: NavController
) {
    val reviewsList = viewModel.reviewsList.observeAsState()
    Column(
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Button(onClick = {
            navController.navigate(Screens.ADD_REVIEW.route) }) {
            Text(text = stringResource(id = R.string.add_review))
        }
        LazyColumn(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            items(reviewsList.value ?: listOf()) { review ->
                ReviewItem(review = review, navController = navController, viewModel = viewModel)
            }
        }
    }
}
