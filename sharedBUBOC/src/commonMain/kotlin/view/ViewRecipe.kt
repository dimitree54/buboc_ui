package view

import DeleteButton
import RECIPE_ICON
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
import cuboc.recipe.UserRecipe
import preview.PreviewRecipeInputCard
import preview.PreviewRecipeOutputCard


@Composable
internal fun ViewRecipe(
    userRecipe: UserRecipe,
    onDelete: () -> Unit
) {
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = RECIPE_ICON,
                contentDescription = null,
                tint = Color.Black
            )
            Text(
                text = userRecipe.recipe.name.toString(), style = MaterialTheme.typography.h3
            )
        }
        Text(text = "Inputs:", style = MaterialTheme.typography.h5)
        userRecipe.inputs.forEach {
            PreviewRecipeInputCard(it)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Instructions:", style = MaterialTheme.typography.h5)
        Text(text = userRecipe.recipe.instruction.text.toString())
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Duration:", style = MaterialTheme.typography.h5)
        Text(text = userRecipe.recipe.instruction.durationMinutes.toString() + " minutes")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Outputs:", style = MaterialTheme.typography.h5)
        userRecipe.outputs.forEach {
            PreviewRecipeOutputCard(it)
        }
        DeleteButton(false, onDelete)
    }
}
