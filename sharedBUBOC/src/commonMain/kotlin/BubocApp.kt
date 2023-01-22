import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
internal fun BubocApp() {
    val coroutineScope = rememberCoroutineScope()
    val store = remember { coroutineScope.createStore() }
    val state by store.stateFlow.collectAsState()

    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // add button and by requests
                    SearchField(SearchType.All) { }
                    Box(Modifier.weight(1f)) {
                        SearchResults(state.messages)
                    }
                }
            }
        }
    }
}
