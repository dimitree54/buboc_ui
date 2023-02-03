package search

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import logic.*
import preview.*


@Composable
internal fun SearchResultsList(
    results: List<SearchResult>,
    state: LazyListState = rememberLazyListState(),
    resultChosen: (SearchResult) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = state,
    ) {
        results.forEachIndexed { index, result ->
            item(key = index) {
                Button(
                    onClick = { resultChosen(result) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(25),
                ) {
                    when (result) {
                        is IngredientSearchResult -> PreviewIngredientCard(result.ingredient)
                        is RecipeInputSearchResult -> PreviewRecipeInputCard(result.recipeInput)
                        is RecipeSearchResult -> PreviewRecipeCard(result.recipe)
                        is RecipeOutputSearchResult -> PreviewRecipeOutputCard(result.recipeOutput)
                        is ResourceSearchResult -> PreviewResourceCard(result.resource)
                    }
                }
            }
        }
    }
}
