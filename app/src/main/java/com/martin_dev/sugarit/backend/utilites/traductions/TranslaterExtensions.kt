import com.martin_dev.sugarit.backend.utilites.traductions.TranslaterEnToSp


import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

suspend fun TranslaterEnToSp.translateSuspend(message: String): String =
    suspendCancellableCoroutine { cont ->
        this.translate(message) { result ->
            cont.resume(result ?: message)
        }
    }
