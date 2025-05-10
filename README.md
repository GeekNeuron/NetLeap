# NetLeap

A modern and lightweight V2Ray client implementation in Kotlin, designed for flexibility, security, and ease of use.

## Features

-   Supports VMess and VLess protocols
-   Simple routing based on domain name
-   TCP and WebSocket transports with TLS support
-   Modern encryption using Argon2 key derivation
-   Clean and modular codebase for easy extension

## Installation

1.  **Prerequisites:**
    -   Java Development Kit (JDK) 17 or higher
    -   Gradle 7.0 or higher

2.  **Clone the repository:**
    ```
    git clone https://github.com/GeekNeuron/NetLeap.git
    ```

3.  **Build the project:**
    ```
    cd NetLeap
    ./gradlew build
    ```

## Usage

1.  **Configure the client:**
    -   Edit the `src/main/resources/config.json` file (if you have one) to configure the server address, port, UUID, and other settings.  If the file doesn't exist, create it.  The application will use the default settings if the file is missing or incomplete.

2.  **Run the client:**
    ```
    ./gradlew run
    ```

3.  **Routing rules:**
    -   You can define routing rules in the `src/main/resources/routing.conf` file (if you have one) to route traffic based on domain name. If the file doesn't exist, create it. The application will use the default proxy if the file is missing.

## Contributing

We welcome contributions from the community! To contribute to this project:

1.  Fork the repository.
2.  Create a new branch for your feature or bug fix.
3.  Make your changes and commit them with clear and descriptive messages.
4.  Submit a pull request.

Please follow these guidelines:

-   Write clean and well-documented code.
-   Follow the existing code style.
-   Include unit tests for your changes.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

-   This project uses the [Ktor](https://ktor.io/) framework for building asynchronous clients and servers.
-   We would like to thank the developers of [Argon2](https://www.argon2.crtpto.net/) for providing a modern and secure key derivation function.

## Contact

[GeekNeuron](https://github.com/GeekNeuron)
