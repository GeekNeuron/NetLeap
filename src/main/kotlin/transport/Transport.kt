package GeekNeuron.client.transport

/**
 * An interface for defining different transport layers.
 */
interface Transport {
    /**
     * Sends a request to the server.
     * @param address The server address.
     * @param port The server port.
     * @param request The request to send.
     * @return The response from the server as a byte array, or null if an error occurred.
     */
    fun send(address: String, port: Int, request: ByteArray): ByteArray?
}
