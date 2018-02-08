package com.myreactorhome.deviceservice;

import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.models.Outlet;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.repositories.OutletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;

@SpringBootApplication
public class DeviceServiceApplication implements CommandLineRunner {

	@Autowired
	OutletRepository outletRepository;

	@Autowired
	HubRepository hubRepository;

	public static void main(String[] args) {
		SpringApplication.run(DeviceServiceApplication.class, args);
	}

	@Profile("dev")
	@Override
	public void run(String... strings) throws Exception {
		outletRepository.deleteAll();
		hubRepository.deleteAll();

		Hub hub = new Hub();
		hub.setHardwareId("23:45:67");
		Outlet outlet = new Outlet();
		outlet.setHardwareId("12:34:56");
		outlet.setConnectionAddress("123456");
		outlet.setOn(false);
		outlet.setManufacturer("TP-Link");
		outletRepository.save(outlet);
		hub.setDevices(new ArrayList<>());
		hub.getDevices().add(outlet);
		hubRepository.save(hub);


		Outlet outlet1 = outletRepository.findByHardwareIdIs("12:34:56");
		System.out.println(outlet1.getType());
	}
}
