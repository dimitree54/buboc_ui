package card

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cuboc.ingredient.Ingredient


@Composable
internal fun IngredientCard(ingredient: Ingredient, state: MutableList<Any>) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(onClick = { state.removeLast() }) {
            Text("Back")
        }
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = Icons.Default.Star,
                contentDescription = "Result icon",
                tint = Color.Black
            )
            Text(
                text = "Ingredient", style = MaterialTheme.typography.h4
            )
        }
        Text(
            text = ingredient.name, style = MaterialTheme.typography.h5
        )
        Text(
            text = "Measured in ${ingredient.measureUnit.name}"
        )
        Button(onClick = { TODO() }) {
            Text("Request")
        }
        Button(onClick = { TODO() }) {
            Text("Search resources")
        }
        Button(onClick = { TODO() }) {
            Text("Search for recipes requiring")
        }
        Button(onClick = { TODO() }) {
            Text("Search for recipes producing")
        }
    }
}