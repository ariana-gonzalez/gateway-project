package com.zentagroup.patient.imp;

import com.zentagroup.patient.database.DBConnection;
import com.zentagroup.patient.model.Patient;
import com.zentagroup.patient.service.IPatientService;
import java.sql.*;

import static com.zentagroup.patient.util.Constant.*;

public class PatientImp implements IPatientService {

    private Patient patient;

    public PatientImp(Patient patient) {
        this.patient = patient;
    }

    /**
     * Saves a new patient to the database with the data of the local attribute Patient.
     * @return Patient - object with the data of the newly created patient, including its id.
     */
    @Override
    public Patient create() {
        Patient patient = null;
        String query = "INSERT INTO patient(name, phone, address) VALUES(?, ?, ?)";
        try (Connection conn = DBConnection.connectionToDatabase();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, this.patient.getName());
            st.setString(2, this.patient.getPhone());
            st.setString(3, this.patient.getAddress());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                rs.next();
                patient = new Patient(rs.getInt(1), this.patient);
            } catch (Exception e) {
                System.out.println(SAVING_ERROR + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(SQL_EXCEPTION + e.getMessage());
        }
        return patient;
    }

    /**
     * Retrieves all the data (name, phone number and address) of a patient
     * based on the given id, which is unique. Returns null if it isn't found.
     * @return Patient - retrieved patient object.
     */
    @Override
    public Patient retrieve() {
        Patient patient = null;
        String query = "SELECT * FROM patient WHERE id=? OR name=?";
        try (Connection conn = DBConnection.connectionToDatabase();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, this.patient.getId());
            st.setString(2, this.patient.getName());
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    patient = new Patient(rs.getInt("id"), rs.getString("name"),
                            rs.getString("phone"), rs.getString("address"));
                }
            } catch (Exception e) {
                System.out.println(FINDING_ERROR + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.println(SQL_EXCEPTION + e.getMessage());
        }
        return patient;
    }

    /**
     * Updates the data from a patient in the database and returns the updated patient.
     * The local patient object must have been modified already with the information to update.
     * @return Patient - object with the updated data.
     */
    @Override
    public Patient update() {
        Patient patient = null;
        String query = "UPDATE patient SET name=?, phone=?, address=? WHERE id=?";
        try (Connection conn = DBConnection.connectionToDatabase();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, this.patient.getName());
            st.setString(2, this.patient.getPhone());
            st.setString(3, this.patient.getAddress());
            st.setInt(4, this.patient.getId());
            st.executeUpdate();
            patient = new Patient(this.patient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }

    /**
     * Deletes a patient from the database based on its id, which is unique.
     * @return Patient - deleted patient object.
     */
    @Override
    public Patient delete() {
        Patient patient = this.retrieve();
        if (patient == null) {
            return null;
        }
        String query = "DELETE FROM patient WHERE id=?";
        try (Connection conn = DBConnection.connectionToDatabase();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, this.patient.getId());
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patient;
    }
}
