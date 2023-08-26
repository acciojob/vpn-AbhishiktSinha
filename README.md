![img.png](img.png)

# VPN

A VPN (Virtual Private Network) service provider company wants to build a web application to manage their VPN service. The application should provide functionality for administrators to register themselves, add service providers and countries to the service providers.

The application should also provide functionality for users to register themselves, subscribe to a service provider, and connect to a VPN service in a specific country. Additionally, the application should allow users to communicate with each other, where the communication is possible only if both users are in the same country or connected to a VPN in the same country.


The design of models can be found [here](https://miro.com/app/board/uXjVPyE_1S0=/).

The following are the high-level requirements for the VPN web application:

## Administrators: 
1. Register themselves using the **registerAdmin** endpoint. 
2. Add service providers using the **addServiceProvider** endpoint. 
3. Add countries to the service providers using the **addCountry** endpoint. Each time you add a country, you should create a new Country object based on the given country name and add it to the country list of the service provider. Note that the user attribute of the country in this case would be null.

## Users: 
1. Register themselves using the **registerUser** endpoint. 
2. Each time you register a user, you should create a new Country object based on the given country name and assign it as the original country of the user. Note that the service provider attribute of the country in this case would be null. Subscribe to a service provider using the subscribe endpoint

## Connection: 
1. Connect to a VPN service in a specific country using the connect endpoint Disconnect from the VPN using the disconnect endpoint Establish communication between users using the communicate endpoint

## Error handling: 
Handle exceptions in case of invalid input or missing information.

The application should use the RESTful API approach, and the response should be in the form of HTTP status codes. 
The controllers and their respective endpoints have been described in the code snippet provided. 
The application should use the provided AdminController, UserController, and ConnectionController and their endpoints as specified.

### Note:

The driver code for implementation of interfaces has been provided. You need to write the interfaces yourself.
Only findById should be used wherever necessary.
No other JPA methods like findAll, findByUsername should be used.
save method in the JPA repository can return the corresponding object wherever necessary. Do not accept the returned value from the save method unless required absolutely.
Do not change the name of any existing function/class/attribute.
Avoid using the lombok library. Define getters and setters manually.