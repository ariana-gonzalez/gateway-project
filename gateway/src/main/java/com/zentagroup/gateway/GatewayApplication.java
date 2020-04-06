package com.zentagroup.gateway;

import com.zentagroup.gateway.controller.DoctorController;
import com.zentagroup.gateway.controller.PatientController;
import com.zentagroup.gateway.dto.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GatewayApplication {

	public static void main(String[] args) {

		SpringApplication.run(GatewayApplication.class, args);

		/**
		DoctorController dc = new DoctorController();
		PatientController pc = new PatientController();

		Doctor doctor = dc.retrieveDoctor(1);
		Patient patient = pc.retrievePatient(1);

		System.out.println("--------------------------Creating Appointments--------------------------------");

		Appointment appointment2 = dc.createAppointment(2, 1, "2020-07-01 09:45:00");
		Appointment appointment3 = dc.createAppointment(2, 4, "2020-07-01 10:30:00");
		Appointment appointment4 = dc.createAppointment(1, 4, "2020-07-01 15:20:00");
		Appointment appointment5 = dc.createAppointment(1, 3, "2020-07-01 16:10:00");

		System.out.println(appointment2);
		System.out.println(appointment3);
		System.out.println(appointment4);
		System.out.println(appointment5);

		System.out.println("---------------------Retrieving Appointments (Doctor)-------------------------");

		AppointmentList<Doctor, Patient> appointmentList = dc.retrieveAppointments(1);
		AppointmentList<Doctor, Patient> appointmentList1 = dc.retrieveAppointments(2);

		System.out.println(appointmentList);
		System.out.println(appointmentList1);

		System.out.println("---------------------------Retrieving Patients--------------------------------");

		EntityList<Doctor, Patient> entityList = dc.retrievePatients(1);
		EntityList<Doctor, Patient> entityList1 = dc.retrievePatients(1);

		System.out.println(entityList);
		System.out.println(entityList1);

		System.out.println("----------------------Retrieving Appointments (Patient)------------------------");

		AppointmentList<Patient, Doctor> appointmentList2 = dc.retrieveAppointmentsByPatient(1);
		AppointmentList<Patient, Doctor> appointmentList3 = dc.retrieveAppointmentsByPatient(4);

		System.out.println(appointmentList2);
		System.out.println(appointmentList3);

		System.out.println("----------------------------Retrieving Doctors--------------------------------");

		EntityList<Patient, Doctor> entityList2 = dc.retrieveDoctorsByPatient(1);
		EntityList<Patient, Doctor> entityList3 = dc.retrieveDoctorsByPatient(4);

		System.out.println(entityList2);
		System.out.println(entityList3);

		 **/



	}
}
