package GeekNeuron.client

import GeekNeuron.client.config.VMessConfig
import GeekNeuron.client.config.VLessConfig
import GeekNeuron.client.exception.ConfigValidationException
import GeekNeuron.client.protocol.VMess
import GeekNeuron.client.routing.Router
import GeekNeuron.client.routing.RoutingRule
import GeekNeuron.client.transport.TCP
import GeekNeuron.client.transport.WebSocket
import org.slf4j.LoggerFactory
import java.net.URI
import java.util.*

fun main() {
    // Configure Logging (You might want to configure this with a file appender)
    val logger = LoggerFactory.getLogger("Main")

    // Default Proxy Configuration
    val defaultProxy = VMessConfig(
        address = "default.example.com",
        port = 8080,
        uuid = UUID.randomUUID(),
        alterId = 5
    )

    try {
        defaultProxy.validate()
        Router.setDefaultProxy(defaultProxy)
    } catch (e: ConfigValidationException) {
        logger.error("Invalid default proxy configuration: ${e.message}")
        return
    }

    // Routing Rules
    val googleProxy = VMessConfig(
        address = "google.example.com",
        port = 8080,
        uuid = UUID.randomUUID(),
        alterId = 5
    )
    try {
        googleProxy.validate()
        val googleRule = RoutingRule(domain = "google.com", proxyConfig = googleProxy)
        Router.addRule(googleRule)
    } catch (e: ConfigValidationException) {
        logger.error("Invalid google proxy configuration: ${e.message}")
    }

    val exampleProxy = VLessConfig(
        address = "example.example.com",
        port = 8080,
        uuid = UUID.randomUUID()
    )

    try {
        exampleProxy.validate()
        val exampleRule = RoutingRule(domain = "example.com", proxyConfig = exampleProxy)
        Router.addRule(exampleRule)
    } catch (e: ConfigValidationException) {
        logger.error("Invalid example proxy configuration: ${e.message}")
    }


    // Destination
    val destinationUrl = "https://www.google.com" // Change URL here
    val uri = URI(destinationUrl)
    val domain = uri.host

    // Route the request
    val selectedProxy = Router.route(domain)

    if (selectedProxy != null) {
        logger.info("Routing $destinationUrl to: ${selectedProxy.address}")

        val payload = "Hello, Routed Request!".toByteArray()
        val request = when (selectedProxy) {
            is VMessConfig -> {
                VMess.createRequest(selectedProxy, payload)
            }
            is VLessConfig -> {
                GeekNeuron.client.protocol.VLess.createRequest(selectedProxy, payload)
            }
            else -> null
        }

        // Send Request - Choose TCP or WebSocket based on your preference
        if (selectedProxy is VMessConfig) {
            TCP.sendRequest(selectedProxy, request)
        } else if (selectedProxy is VLessConfig) {
            WebSocket.sendRequest(selectedProxy, request)
        }

    } else {
        logger.warn("No proxy found for $destinationUrl")
    }
}
