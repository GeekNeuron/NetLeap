package GeekNeuron.client.transport

import GeekNeuron.client.config.ServerConfig
import GeekNeuron.client.config.VMessConfig
import GeekNeuron.client.config.VLessConfig
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory
import java.io.ByteArrayInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory

/**
 * Implementation of the WebSocket transport with TLS.
 */
object WebSocket {
    private val logger = LoggerFactory.getLogger(WebSocket::class.java)

    /**
     * Sends a request via WebSocket.
     * @param config The server configuration.
     * @param request The request to send.
     * @return The response as a byte array, or null if an error occurred.
     */
    fun sendRequest(config: ServerConfig, request: ByteArray?): ByteArray? {
        if (request == null) {
            logger.warn("Request is null, cannot send via WebSocket")
            return null
        }

        if (config is VMessConfig || config is VLessConfig) { // Ensure it's Vmess or Vless config
            val client = HttpClient(CIO) {
                install(WebSockets) {
                     val certificate = """
                        -----BEGIN CERTIFICATE-----
                        MIIGbzCCBWOgAwIBAgIUUUW+9kK9jZIjJRo=
                        -----END CERTIFICATE-----
                    """.trimIndent()

                    val certificateFactory = CertificateFactory.getInstance("X.509")
                    val certificateInputStream = ByteArrayInputStream(certificate.toByteArray())
                    val serverCertificate = certificateFactory.generateCertificate(certificateInputStream) as X509Certificate

                    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
                    keyStore.load(null)
                    keyStore.setCertificateEntry("server", serverCertificate)

                    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
                    trustManagerFactory.init(keyStore)

                    val sslContext = SSLContext.getInstance("TLS")
                    sslContext.init(null, trustManagerFactory.trustManagers, null)

                    https {
                        sslContext(sslContext)
                    }
                }
            }

            return try {
                runBlocking {
                    var responseBytes: ByteArray? = null
                    // Use "wss" for secure WebSocket
                    val scheme = "wss"
                    client.webSocket(method = HttpMethod.Get, scheme = scheme, host = config.address, port = config.port, path = "/ws") { //replace /ws with your path
                        if (request != null) {
                            outgoing.send(Frame.Binary(true, request))
                        }
                        val incoming = incoming.receive()
                        if (incoming is Frame.Binary) {
                            responseBytes = incoming.data
                            println("Response from server (WebSocket): ${String(responseBytes!!)}")
                        }
                    }
                    client.close()
                    responseBytes
                }

            } catch (e: Exception) {
                logger.error("Error during WebSocket communication: ${e.message}", e)
                client.close()
                return null
            }
        } else {
            logger.error("Invalid config for WebSocket transport")
            return null
        }
    }
}
