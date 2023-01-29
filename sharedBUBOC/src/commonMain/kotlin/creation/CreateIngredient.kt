package creation

import INGREDIENT_ICON
import SaveButton
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cuboc.ingredient.Ingredient
import utility.MeasureUnit

@Composable
internal fun CreateIngredient(
    onCreation: (Ingredient) -> Unit
) {
    val ingredientName = remember { mutableStateOf("") }
    val measureUnitName = remember { mutableStateOf("") }
    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()), verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row {
            Icon(
                modifier = Modifier.size(50.dp),
                imageVector = INGREDIENT_ICON,
                contentDescription = null,
                tint = Color.Black
            )
            Text(
                text = "Create ingredient", style = MaterialTheme.typography.h3
            )
        }
        Text(text = "Name: ", style = MaterialTheme.typography.h5)
        TextField(
            ingredientName.value,
            singleLine = true,
            shape = RoundedCornerShape(25),
            onValueChange = { ingredientName.value = it }
        )
        Text(
            text = "Measure unit: ",
            style = MaterialTheme.typography.h5
        )
        TextField(
            measureUnitName.value,
            singleLine = true,
            shape = RoundedCornerShape(25),
            onValueChange = { measureUnitName.value = it }
        )

        val readyToSave = ingredientName.value.isNotBlank() && measureUnitName.value.isNotBlank()
        SaveButton(readyToSave) {
            onCreation(Ingredient(ingredientName.value, MeasureUnit(measureUnitName.value)))
        }
    }
}
