package GeekNeuron.client.protocol

import GeekNeuron.client.config.ServerConfig

/**
 * An interface for defining different protocols.
 */
interface Protocol {
    /**
     * Creates a request based on the given configuration and payload.
     * @param config The server configuration.
     * @param payload The payload to be sent.
     * @return The created request as a byte array, or null if an error occurred.
     */
    fun createRequest(config: ServerConfig, payload: ByteArray?): ByteArray?
}
