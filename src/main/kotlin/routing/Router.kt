package GeekNeuron.client.routing

import GeekNeuron.client.config.ServerConfig
import org.slf4j.LoggerFactory

/**
 * The router class for routing traffic based on domain name.
 */
object Router {
    private val logger = LoggerFactory.getLogger(Router::class.java)
    val rules = mutableListOf<RoutingRule>() // List of routing rules
    var defaultProxy: ServerConfig? = null // Default proxy configuration

    /**
     * Adds a routing rule.
     * @param rule The routing rule to add.
     */
    fun addRule(rule: RoutingRule) {
        rules.add(rule)
    }

    /**
     * Sets the default proxy.
     * @param proxy The default proxy configuration.
     */
    fun setDefaultProxy(proxy: ServerConfig) {
        defaultProxy = proxy
    }

    /**
     * Routes a domain to a server configuration.
     * @param domain The domain to route.
     * @return The server configuration for the domain, or the default proxy if no rule is found.
     */
    fun route(domain: String): ServerConfig? {
        for (rule in rules) {
            if (domain == rule.domain) {
                logger.debug("Routing $domain to ${rule.proxyConfig.address}")
                return rule.proxyConfig
            }
        }
        logger.debug("No rule found for $domain, using default proxy")
        return defaultProxy
    }
}
