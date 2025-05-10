package GeekNeuron.client.config

/**
 * An interface for defining the base server configuration.
 */
interface ServerConfig {
    /**
     * The server address.
     */
    val address: String

    /**
     * The server port.
     */
    val port: Int

    /**
     * Validates the configuration.
     * @return true if the configuration is valid.
     * @throws ConfigValidationException if the configuration is invalid.
     */
    fun validate(): Boolean
}
