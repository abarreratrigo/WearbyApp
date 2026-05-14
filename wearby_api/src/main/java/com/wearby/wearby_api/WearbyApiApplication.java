package com.wearby.wearby_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Para poder iniciar la aplicación, ejecutad el sql
 * en Workbench y modificad las application properties
 * con vuestra contraseña del root
 */
@SpringBootApplication
public class WearbyApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(WearbyApiApplication.class, args);
	}

}
