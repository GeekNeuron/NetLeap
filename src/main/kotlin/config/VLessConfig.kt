package GeekNeuron.client.config

import GeekNeuron.client.exception.ConfigValidationException
import java.util.*

/**
 * The configuration for the VLess protocol.
 * @param address The server address.
 * @param port The server port.
 * @param uuid The user ID.
 * @param encryption The encryption settings.
 */
data class VLessConfig(
    override val address: String,
    override val port: Int,
    val uuid: UUID,
    val encryption: String = "none"
) : ServerConfig {
    /**
     * Validates the VLess configuration.
     * @return true if the configuration is valid.
     * @throws ConfigValidationException if the configuration is invalid.
     */
    override fun validate(): Boolean {
        if (port !in 1..65535) {
            throw ConfigValidationException("Invalid port number: $port")
        }
        if (address.isEmpty()) {
            throw ConfigValidationException("Address cannot be empty")
        }
        try {
            UUID.fromString(uuid.toString())
        } catch (e: IllegalArgumentException) {
            throw ConfigValidationException("Invalid UUID format: ${uuid.toString()}")
        }
        return true
    }
}
