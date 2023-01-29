import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Application
import platform.UIKit.UIScreen
import platform.UIKit.UIViewController
import platform.UIKit.UIWindow
import platform.UIKit.safeAreaInsets

fun MainViewController(): UIViewController =
    Application("BUBOC") {
        val window = UIWindow(frame = UIScreen.mainScreen.bounds)
        Box(modifier = Modifier.padding(top = window.safeAreaInsets.size.dp, bottom = window.safeAreaInsets.size.dp)) {
            BubocApp()
        }
    }