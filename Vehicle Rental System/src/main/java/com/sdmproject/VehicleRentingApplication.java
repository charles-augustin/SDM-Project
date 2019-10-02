package com.sdmproject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

import com.sdmproject.exceptions.DuplicateEntryException;
import com.sdmproject.model.ClientRecord;
import com.sdmproject.model.Reservation;
import com.sdmproject.model.Role;
import com.sdmproject.model.User;
import com.sdmproject.model.Vehicle;
import com.sdmproject.repository.ClientRecordRepository;
import com.sdmproject.repository.ReservationRepository;
import com.sdmproject.repository.RoleRepository;
import com.sdmproject.service.UserService;
import com.sdmproject.service.VehicleService;

@SpringBootApplication
public class VehicleRentingApplication extends SpringBootServletInitializer implements CommandLineRunner {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private ClientRecordRepository clientRecordRepository;

	@Autowired
	private VehicleService vehicleService;
	
	@Autowired
	private ReservationRepository reservationService;
	
	public static void main(String[] args) {
		SpringApplication.run(VehicleRentingApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(VehicleRentingApplication.class);
	}

	@Override
	public void run(String... args) throws ParseException, DuplicateEntryException {
		
		Role roleClerk = new Role(1, "CLERK");
		roleRepository.save(roleClerk);
		Role roleAdmin = new Role(2, "ADMIN");
		roleRepository.save(roleAdmin);
		
		boolean isAdminExist = userService.isUserExist("admin@niravjdn.xyz");
		if (!isAdminExist) {
			User admin = new User();
			admin.setEmail("admin@niravjdn.xyz");
			admin.setPassword("admin");
			admin.setFirstName("admin");
			admin.setLastName("admin");

			Role userRole = roleRepository.findByRole("ADMIN");
			admin.setRoles(new HashSet<Role>(Arrays.asList(userRole)));

			userService.saveUser(admin);
		}

		boolean isUserExist = userService.isUserExist("user@niravjdn.xyz");
		if (!isUserExist) {
			User user = new User();
			user.setEmail("user@niravjdn.xyz");
			user.setPassword("user");
			user.setFirstName("user");
			user.setLastName("user");
			Role userRole = roleRepository.findByRole("CLERK");
			user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
			userService.saveUser(user);
		}

		addVehicleRecords();
		addClientRecords();
		addReservationRecords();
	}

	private void addVehicleRecords() throws DuplicateEntryException {

		Vehicle vehicle = new Vehicle(1, "SUV", "Audi", "Q3", 2015, "Black", "ABC YHZ");
		vehicle = new Vehicle(1, "SEADEN", "Tesla", "Model S", 2016, "White", "ABD ISZ");
		vehicleService.saveVehicle(vehicle);
		vehicle = new Vehicle(2, "TRUCK", "BMW", "S400", 2015, "Pink", "ABE UEZ");
		vehicleService.saveVehicle(vehicle);
		vehicle = new Vehicle(3, "SUV", "Audi", "Q3", 2013, "White", "EBC KGZ");
		vehicleService.saveVehicle(vehicle);
		vehicle = new Vehicle(4, "SUV", "Dodge", "Tiburon", 2012, "Black", "TSC JSZ");
		vehicleService.saveVehicle(vehicle);
		vehicle = new Vehicle(5, "SEADEN", "Subaru", "Mazda", 2011, "Green", "JHS YHZ");
		vehicleService.saveVehicle(vehicle);

	}

	private void addClientRecords() throws ParseException, DuplicateEntryException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		ClientRecord record = new ClientRecord(1, "Nirav", "Patel", "123", "3434412344", sdf.parse("31-08-2020"));
		clientRecordRepository.save(record);
		record = new ClientRecord(1, "Savan", "Patel", "1234", "6734412344", sdf.parse("30-11-2020"));
		clientRecordRepository.save(record);
		record = new ClientRecord(1, "Jaivik", "Jadav", "1235", "1834412344", sdf.parse("30-10-2020"));
		clientRecordRepository.save(record);
		record = new ClientRecord(1, "Avinash", "Damodaran", "5123", "9634412344", sdf.parse("30-08-2021"));
		clientRecordRepository.save(record);
		record = new ClientRecord(1, "Jemish", "Paghdar", "8123", "4534412344", sdf.parse("30-05-2022"));
		clientRecordRepository.save(record);
	}
	
	private void addReservationRecords() throws ParseException, DuplicateEntryException {

		SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
		Reservation r ;
		r= new Reservation(1,vehicleService.findByID(1),clientRecordRepository.findById(1),sdf.parse("01-10-2019"),sdf.parse("30-11-2019"), sdf.parse("30-11-2020"));
		reservationService.save(r);
		r = new Reservation(2,vehicleService.findByID(2),clientRecordRepository.findById(2),sdf.parse("25-09-2019"),sdf.parse("10-10-2019"),sdf.parse("30-11-2020"));
		reservationService.save(r);
		r = new Reservation(3,vehicleService.findByID(3),clientRecordRepository.findById(3),sdf.parse("30-11-2020"),sdf.parse("30-11-2020"),sdf.parse("30-11-2020"));
		reservationService.save(r);
		r = new Reservation(4,vehicleService.findByID(4),clientRecordRepository.findById(4),sdf.parse("30-11-2020"),sdf.parse("30-11-2020"),sdf.parse("30-11-2020"));
		reservationService.save(r);
}
}
