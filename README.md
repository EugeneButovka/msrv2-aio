# microservices-all-in-one-project-rev2
Technologies: Java/Spring Boot/Eureka/Hystrix/Sleuth+Zipkin

## How to run?

Run locally: launch each "-local" config from the .run directory
Run with config server: launch each config from the .run directory without "-local" postfix. Start launching from the 
config server.


eureka UI: http://localhost:8761/
zipkin UI: http://localhost:9411/zipkin
hystrix UI: http://localhost:8788/hystrix/monitor?stream=http%3A%2F%2Flocalhost%3A8383%2Factuator%2Fhystrix.stream&title=product-app
