package GeekNeuron.client.routing

import GeekNeuron.client.config.ServerConfig

/**
 * A routing rule that maps a domain to a proxy configuration.
 * @param domain The domain to match.
 * @param proxyConfig The proxy configuration to use for the domain.
 */
data class RoutingRule(val domain: String, val proxyConfig: ServerConfig)
