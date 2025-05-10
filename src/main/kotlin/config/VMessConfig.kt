package GeekNeuron.client.config

import GeekNeuron.client.exception.ConfigValidationException
import java.util.*

/**
 * The configuration for the VMess protocol.
 * @param address The server address.
 * @param port The server port.
 * @param uuid The user ID.
 * @param alterId The alter ID.
 * @param security The security settings.
 */
data class VMessConfig(
    override val address: String,
    override val port: Int,
    val uuid: UUID,
    val alterId: Int = 0,
    val security: String = "aes-128-cfb"
) : ServerConfig {
    /**
     * Validates the VMess configuration.
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
