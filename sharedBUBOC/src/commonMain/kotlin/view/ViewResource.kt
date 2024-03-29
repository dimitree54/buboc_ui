package view

import DeleteButton
import RESOURCE_ICON
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cuboc.ingredient.PieceOfUserResource
import preview.PreviewIngredientCard


@Composable
internal fun ViewResource(
    resource: PieceOfUserResource,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = RESOURCE_ICON,
                contentDescription = "Result icon",
                tint = Color.Black
            )
            Text(
                text = resource.resource.ingredient.name.toString(), style = MaterialTheme.typography.h3
            )
        }
        Text("Ingredient: ", style = MaterialTheme.typography.h5)
        PreviewIngredientCard(resource.resource.ingredient)
        Text("Amount:", style = MaterialTheme.typography.h5)
        Text(resource.resource.amount.toString() + " " + resource.resource.ingredient.measureUnit)
        DeleteButton(false, onDelete)
    }
}