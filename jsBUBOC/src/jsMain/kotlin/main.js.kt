import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.FirebaseOptions
import dev.gitlive.firebase.initialize
import org.jetbrains.skiko.wasm.onWasmReady

fun main() {
    Firebase.initialize(
        options = FirebaseOptions(
            applicationId = "1:1023684655079:web:705b60ec8dcc82bdd8ef42",
            apiKey = "AIzaSyCp1sA8eQJf4Hl48S-Lfp5sPoJhCsIUZmQ",
            gaTrackingId = "G-D4YGKSTCJ0",
            storageBucket = "buboc-f5b3b.appspot.com",
            projectId = "buboc-f5b3b",
            gcmSenderId = "1023684655079",
            authDomain = "buboc-f5b3b.firebaseapp.com",
        )
    )
    onWasmReady {
        Window("BUBOC") {
            Column(modifier = Modifier.fillMaxSize()) {
                MainView()
            }
        }
    }
}

