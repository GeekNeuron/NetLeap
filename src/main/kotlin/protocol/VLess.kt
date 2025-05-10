package GeekNeuron.client.protocol

import GeekNeuron.client.config.ServerConfig
import GeekNeuron.client.config.VLessConfig
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream

/**
 * Implementation of the VLess protocol.
 */
object VLess : Protocol {
    private val logger = LoggerFactory.getLogger(VLess::class.java)

    /**
     * Creates a VLess request.
     * @param config The VLess configuration.
     * @param payload The payload to send.
     * @return The created request as a byte array, or null if an error occurred.
     */
    override fun createRequest(config: ServerConfig, payload: ByteArray?): ByteArray? {
        if (config is VLessConfig) {
            return try {
                val version = 0x02.toByte()
                val uuidBytes = config.uuid.toString().replace("-", "").toByteArray()
                val command = 0x01.toByte() // TCP Command
                val portBytes = java.nio.ByteBuffer.allocate(2).putShort(config.port.toShort()).array() // port as short

                val requestHeader = ByteArrayOutputStream()

                requestHeader.write(version.toInt())
                requestHeader.write(uuidBytes)
                requestHeader.write(command.toInt())
                requestHeader.write(0x00) // option
                requestHeader.write(portBytes)
                requestHeader.write(0x00) // address type
                requestHeader.write(config.address.toByteArray())

                val result = ByteArrayOutputStream()
                result.write(requestHeader.toByteArray())
                if (payload != null) {
                    result.write(payload)
                }

                result.toByteArray()
            } catch (e: Exception) {
                logger.error("Error creating VLess request: ${e.message}", e)
                return null
            }
        } else {
            logger.error("Invalid config for VLess protocol")
            return null
        }
    }
}
