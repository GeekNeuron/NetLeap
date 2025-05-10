package GeekNeuron.client.transport

import GeekNeuron.client.config.ServerConfig
import org.slf4j.LoggerFactory
import java.io.IOException
import java.net.Socket

/**
 * Implementation of the TCP transport.
 */
object TCP {
    private val logger = LoggerFactory.getLogger(TCP::class.java)

    /**
     * Sends a request via TCP.
     * @param config The server configuration.
     * @param request The request to send.
     * @return The response as a byte array, or null if an error occurred.
     */
    fun sendRequest(config: ServerConfig, request: ByteArray?): ByteArray? {
        if (request == null) {
            logger.warn("Request is null, cannot send")
            return null
        }

        return try {
            val socket = Socket(config.address, config.port) // Create socket
            socket.outputStream.write(request) // Send the request

            val inputStream = socket.inputStream
            val response = inputStream.readBytes()  // Read the response (simplified)
            println("Response from server (TCP): ${String(response)}")

            socket.close() // Close the connection
            response

        } catch (e: IOException) {
            logger.error("Error during communication (TCP): ${e.message}", e)
            return null
        }
    }
}
