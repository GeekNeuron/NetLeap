package GeekNeuron.client.security

import de.mkammerer.argon2.Argon2Factory
import org.slf4j.LoggerFactory

/**
 * Utility class for key derivation using Argon2.
 */
object KeyDerivation {
    private val logger = LoggerFactory.getLogger(KeyDerivation::class.java)

    /**
     * Derives a key from the given password and salt using Argon2.
     * @param password The password to use for key derivation.
     * @param salt The salt to use for key derivation.
     * @return The derived key as a byte array, or null if an error occurred.
     */
    fun deriveKey(password: String, salt: ByteArray): ByteArray? {
        val argon2 = Argon2Factory.create()
        return try {
            val iterations = 3 // You can adjust
            val memory = 65536 // 64MB
            val parallelism = 1 // number of threads

            val hash = argon2.hash(iterations, memory, parallelism, password.toCharArray(), Charsets.UTF_8, salt)
            hash.toByteArray()

        } catch (e: Exception) {
            logger.error("Error deriving key with Argon2: ${e.message}", e)
            null
        } finally {
            argon2.close()
        }
    }

    /**
     * Verifies a password against a hash using Argon2.
     * @param hash The hash to verify against.
     * @param password The password to verify.
     * @param salt The salt used to generate the hash.
     * @return true if the password matches the hash, false otherwise.
     */
    fun verify(hash: String, password: CharArray, salt: ByteArray): Boolean {
        val argon2 = Argon2Factory.create()
        return try {
            argon2.verify(hash, password, Charsets.UTF_8, salt)
        } catch (e: Exception) {
            logger.error("Error verifying password with Argon2: ${e.message}", e)
            false
        } finally {
            argon2.close()
        }
    }
}
