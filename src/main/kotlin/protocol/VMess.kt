package GeekNeuron.client.protocol

import GeekNeuron.client.config.ServerConfig
import GeekNeuron.client.config.VMessConfig
import GeekNeuron.client.exception.VMessException
import GeekNeuron.client.security.KeyDerivation
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.security.NoSuchAlgorithmException
import java.security.spec.InvalidKeySpecException
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.random.Random

/**
 * Implementation of the VMess protocol.
 */
object VMess : Protocol {
    private val logger = LoggerFactory.getLogger(VMess::class.java)
    private const val TIME_HASH_ALLOWED_DELAY = 30 // seconds
    private const val ENCRYPT_MODE = Cipher.ENCRYPT_MODE

    /**
     * Creates a VMess request.
     * @param config The VMess configuration.
     * @param payload The payload to send.
     * @return The created request as a byte array, or null if an error occurred.
     */
    override fun createRequest(config: ServerConfig, payload: ByteArray?): ByteArray? {
        if (config is VMessConfig) {
            return try {
                val command = 0x01.toByte() // TCP Command
                val random = Random.Default
                val rand = ByteArray(16) // Reduced to 16 bytes for key derivation
                random.nextBytes(rand)

                val time = System.currentTimeMillis() / 1000
                val timeBytes = time.toString().toByteArray()

                val uuidBytes = config.uuid.toString().replace("-", "").toByteArray()

                val requestHeader = ByteArrayOutputStream()

                requestHeader.write(0x01) // Version
                requestHeader.write(uuidBytes)
                requestHeader.write(rand, 0, 16)  // Random bytes, only 16
                requestHeader.write(command.toInt())
                requestHeader.write(config.alterId) // Including AlterId
                requestHeader.write(timeBytes.size)
                requestHeader.write(timeBytes)

                // Use Argon2 for key derivation
                val password = config.uuid.toString() // Using UUID as password - NOT SECURE FOR PRODUCTION
                val salt = rand
                val derivedKey = KeyDerivation.deriveKey(password, salt) ?: throw VMessException("Key derivation failed")

                val iv = derivedKey.copyOfRange(0, 16)

                val cipher = Cipher.getInstance("AES/CFB8/NoPadding")
                val secretKeySpec = SecretKeySpec(derivedKey, "AES")
                val ivParameterSpec = IvParameterSpec(iv)
                cipher.init(ENCRYPT_MODE, secretKeySpec, ivParameterSpec)

                val headerBytes = requestHeader.toByteArray()
                val encryptedHeader = cipher.doFinal(headerBytes)

                val result = ByteArrayOutputStream()
                result.write(encryptedHeader)
                val encryptedPayload = cipher.doFinal(payload) ?: ByteArray(0) // Encrypting the payload
                result.write(encryptedPayload) // Writing the encrypted payload

                result.toByteArray()

            } catch (e: NoSuchAlgorithmException) {
                logger.error("Algorithm not found: ${e.message}", e)
                null
            } catch (e: InvalidKeySpecException) {
                logger.error("Invalid key spec: ${e.message}", e)
                null
            } catch (e: VMessException) {
                logger.error("VMess exception: ${e.message}", e)
                null
            } catch (e: Exception) {
                logger.error("Error creating VMess request: ${e.message}", e)
                null // Returning null in case of an error
            }
        } else {
            logger.error("Invalid config for VMess protocol")
            null
        }
    }

    /**
     * Checks if the given timestamp is valid.
     * @param timestamp The timestamp to check.
     * @return true if the timestamp is valid, false otherwise.
     */
    fun isValidTime(timestamp: Long): Boolean {
        val currentTime = System.currentTimeMillis() / 1000
        return (currentTime - timestamp) <= TIME_HASH_ALLOWED_DELAY
    }
}
