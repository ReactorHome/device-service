package com.myreactorhome.deviceservice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.myreactorhome.deviceservice.feign_clients.NestClient;
import com.myreactorhome.deviceservice.models.GenericDevice;
import com.myreactorhome.deviceservice.models.Hub;
import com.myreactorhome.deviceservice.models.Outlet;
import com.myreactorhome.deviceservice.models.Thermostat;
import com.myreactorhome.deviceservice.repositories.HubRepository;
import com.myreactorhome.deviceservice.repositories.OutletRepository;
//import com.myreactorhome.deviceservice.services.MessageService;
//import org.eclipse.paho.client.mqttv3.MqttClient;
//import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
//import org.eclipse.paho.client.mqttv3.MqttException;
//import org.eclipse.paho.client.mqttv3.internal.security.SSLSocketFactoryFactory;
import com.myreactorhome.deviceservice.repositories.ThermostatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.ArrayList;

@EnableFeignClients
@SpringBootApplication
public class DeviceServiceApplication implements CommandLineRunner {

	@Autowired
	OutletRepository outletRepository;

	@Autowired
	HubRepository hubRepository;

	@Autowired
	ThermostatRepository thermostatRepository;

	@Autowired
	NestClient nestClient;

	public static void main(String[] args) {
		SpringApplication.run(DeviceServiceApplication.class, args);
	}

	@Profile("dev")
	@Override
	public void run(String... strings) throws Exception {
		String jsonString = "{\n" +
				"    \"YbIGm1CP-4f4V8_1-oj2-iHa7Kq3pSDf\": {\n" +
				"        \"humidity\": 50,\n" +
				"        \"locale\": \"en-US\",\n" +
				"        \"temperature_scale\": \"F\",\n" +
				"        \"is_using_emergency_heat\": false,\n" +
				"        \"has_fan\": true,\n" +
				"        \"software_version\": \"5.6.1\",\n" +
				"        \"has_leaf\": true,\n" +
				"        \"where_id\": \"N_cvO3hLt-y9xyzELmxcHJprvbrEPyaf82H3e6_47-olru6WS1ny6g\",\n" +
				"        \"device_id\": \"YbIGm1CP-4f4V8_1-oj2-iHa7Kq3pSDf\",\n" +
				"        \"name\": \"Guest House (80D0)\",\n" +
				"        \"can_heat\": true,\n" +
				"        \"can_cool\": true,\n" +
				"        \"target_temperature_c\": 20,\n" +
				"        \"target_temperature_f\": 68,\n" +
				"        \"target_temperature_high_c\": 26,\n" +
				"        \"target_temperature_high_f\": 79,\n" +
				"        \"target_temperature_low_c\": 19,\n" +
				"        \"target_temperature_low_f\": 66,\n" +
				"        \"ambient_temperature_c\": 21,\n" +
				"        \"ambient_temperature_f\": 70,\n" +
				"        \"away_temperature_high_c\": 24,\n" +
				"        \"away_temperature_high_f\": 76,\n" +
				"        \"away_temperature_low_c\": 12.5,\n" +
				"        \"away_temperature_low_f\": 55,\n" +
				"        \"eco_temperature_high_c\": 24,\n" +
				"        \"eco_temperature_high_f\": 76,\n" +
				"        \"eco_temperature_low_c\": 12.5,\n" +
				"        \"eco_temperature_low_f\": 55,\n" +
				"        \"is_locked\": false,\n" +
				"        \"locked_temp_min_c\": 20,\n" +
				"        \"locked_temp_min_f\": 68,\n" +
				"        \"locked_temp_max_c\": 22,\n" +
				"        \"locked_temp_max_f\": 72,\n" +
				"        \"sunlight_correction_active\": false,\n" +
				"        \"sunlight_correction_enabled\": true,\n" +
				"        \"structure_id\": \"sLWdEHSsMtj9vBytxX97BVNpO9A9w5fQqDccRz7_ZBeuKMdz7lyTSA\",\n" +
				"        \"fan_timer_active\": false,\n" +
				"        \"fan_timer_timeout\": \"1970-01-01T00:00:00.000Z\",\n" +
				"        \"fan_timer_duration\": 15,\n" +
				"        \"previous_hvac_mode\": \"\",\n" +
				"        \"hvac_mode\": \"heat\",\n" +
				"        \"time_to_target\": \"~0\",\n" +
				"        \"time_to_target_training\": \"ready\",\n" +
				"        \"where_name\": \"Guest House\",\n" +
				"        \"label\": \"80D0\",\n" +
				"        \"name_long\": \"Guest House Thermostat (80D0)\",\n" +
				"        \"is_online\": true,\n" +
				"        \"hvac_state\": \"off\"\n" +
				"    }\n" +
				"}";

		outletRepository.deleteAll();
		hubRepository.deleteAll();
		thermostatRepository.deleteAll();
//		String jsonS = nestClient.getThermostats("Bearer c.9Q02hC8JBUr0MfhgKRWFYIkEOqp1vLxjBDtA9WjY1b2GueZjC6ze0jtKhAOTHcfhk9HSu1FRGXHMUmxHbiPHIk1ea1jZQXx6gsHStiDdafLJOPWJV1ET5q5MMJqhj01X0EDHH9nS1yq8xJy5");
		Hub hub = new Hub();
		hub.setHardwareId("23:45:67");
		hub.setGroupId(1);
//		Outlet outlet = new Outlet();
//		outlet.setHardwareId("12:34:56");
//		outlet.setConnectionAddress("123456");
//		outlet.setOn(false);
//		outlet.setManufacturer("TP-Link");
//		outletRepository.save(outlet);
//		hub.setDevices(new ArrayList<>());
//		hub.getDevices().add(outlet);
		hubRepository.save(hub);
		System.out.println(hub.getId());
//
//
//		Outlet outlet1 = outletRepository.findByHardwareIdIs("12:34:56");
//		System.out.println(outlet1.getType());
////
//		final ObjectMapper mapper = new ObjectMapper();
//		final JsonNode json = mapper.readTree(jsonS);
//
//// Alt 2, convert to a Pojo
//		for (final JsonNode thermostat : json) {
//			final Thermostat a = mapper.treeToValue(thermostat, Thermostat.class);
//			System.out.println(a.getEcoTemperatureHighF());
//		}
	}

//	@Bean
//	public MessageService messageService(){
//		return new MessageService();
//	}

}
