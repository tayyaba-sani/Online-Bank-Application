package com.bank.gatewayserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;

import java.time.LocalDate;

@SpringBootApplication
@EnableEurekaClient
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/bank/accounts/**")
						.filters(f -> f.rewritePath("/bank/account/(?<segment>.*)","/${segment}")
								.addResponseHeader("X-Response-Time", LocalDate.now().toString()))
						.uri("lb://ACCOUNTS")).
						route(p -> p
								.path("/bank/loans/**")
								.filters(f -> f.rewritePath("/bank/loans/(?<segment>.*)","/${segment}")
										.addResponseHeader("X-Response-Time",LocalDate.now().toString()))
								.uri("lb://LOANS")).
						route(p -> p
								.path("/bank/cards/**")
								.filters(f -> f.rewritePath("/bank/cards/(?<segment>.*)","/${segment}")
										.addResponseHeader("X-Response-Time",LocalDate.now().toString()))
								.uri("lb://CARDS")).build();
	}
}
