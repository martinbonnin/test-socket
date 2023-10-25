import io.ktor.network.selector.*
import io.ktor.network.sockets.*
import kotlinx.coroutines.*
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.test.Test
import kotlin.time.Duration.Companion.seconds

class Main {
    @Test
    fun test() {
        val scope = CoroutineScope(Dispatchers.IO)

        val job = scope.launch {
            val port = 53333
            val selectorManager = SelectorManager(Dispatchers.IO)

            launch {
                println("listening")
                println("run 'nc  127.0.0.1 53333' in another terminal")
                val serverSocket = aSocket(selectorManager).tcp().bind("127.0.0.1", port)
                val socket = serverSocket.accept()

                println("server connected")
                launch {
                    delay(2.seconds)
                    println("closing socket")
                    socket.close()
                    println("closing serverSocket")
                    serverSocket.close()
                    println("cancelling scope")
                    scope.cancel()
                }

                println("reading client message")
                socket.openReadChannel().readInt()
                waitForever()
            }
        }

        runBlocking {
            job.join()
            println("the server is cancelled but the nc client in your terminal should still be connected.")
            println("the same thing on the JVM correctly terminates the TCP connection.")

            waitForever()
        }
    }
}

suspend fun waitForever() {
    while (true) {
        delay(1000)
    }
}