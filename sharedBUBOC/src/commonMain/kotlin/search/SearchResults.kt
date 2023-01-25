package search

import InstancePreview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import entry.Instance

@Composable
internal fun SearchResultsList(results: List<Instance>, stack: MutableList<Any>) {
    val listState = rememberLazyListState()
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        state = listState,
    ) {
        results.forEach { result ->
            item(key = result.id) {
                InstancePreview(result){
                    stack.add(result.obj)
                }
            }
        }
    }
}

