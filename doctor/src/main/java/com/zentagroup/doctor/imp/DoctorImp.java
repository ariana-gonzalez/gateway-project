package com.zentagroup.doctor.imp;

import com.zentagroup.doctor.database.DBConnection;
import com.zentagroup.doctor.model.Doctor;
import com.zentagroup.doctor.service.IDoctorService;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.management.Query;

import static com.zentagroup.doctor.util.Constant.*;

public class DoctorImp implements IDoctorService {

    private Doctor doctor;

    public DoctorImp(Doctor doctor) {
        this.doctor = doctor;
    }

    /**
     * Saves a new doctor to the database with the data of the local attribute
     * Doctor.
     * 
     * @return Doctor - object with the data of the newly created doctor, including
     *         its id.
     */
    @Override
    public Doctor create() {
        Doctor doctor = null;
        String query = "INSERT INTO doctor(name, specialization) VALUES(?, ?)";
        try (Connection conn = DBConnection.connectionToDatabase();
                PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, this.doctor.getName());
            st.setString(2, this.doctor.getSpecialization());
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                rs.next();
                doctor = new Doctor(rs.getInt(1), this.doctor);
            } catch (Exception e) {
                System.out.print(SAVING_ERROR + e.getMessage());
            }
        } catch (SQLException e) {
            System.out.print(SQL_EXCEPTION + e.getMessage());
        }
        return doctor;
    }

    /**
     * Retrieves all the data (name and specialization) of a doctor based on the
     * given id, which is unique. Returns null if it isn't found.
     * 
     * @return Doctor - retrieved doctor object.
     */
    @Override
    public Doctor retrieve() {
        Doctor doctor = null;
        String query = "SELECT * FROM doctor WHERE id=? OR name=?";
        try (Connection conn = DBConnection.connectionToDatabase();
                PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, this.doctor.getId());
            st.setString(2, this.doctor.getName());
            try (ResultSet rs = st.executeQuery()) {
                if (rs.next()) {
                    doctor = new Doctor(rs.getInt("id"), rs.getString("name"), rs.getString("specialization"));
                }
            } catch (Exception e) {
                System.out.print(FINDING_ERROR);
            }
        } catch (SQLException e) {
            System.out.print(SQL_EXCEPTION);
        }
        return doctor;
    }

    /**
     * Updates the data from a doctor in the database and returns the updated
     * doctor. The local doctor object must have been modified already with the
     * information to update.
     * 
     * @return Doctor - object with the updated data.
     */
    @Override
    public Doctor update() {
        Doctor doctor = null;
        String query = "UPDATE doctor SET name=?, specialization=? WHERE id=?";
        try (Connection conn = DBConnection.connectionToDatabase();
                PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setString(1, this.doctor.getName());
            st.setString(2, this.doctor.getSpecialization());
            st.setInt(3, this.doctor.getId());
            st.executeUpdate();
            doctor = new Doctor(this.doctor);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public Doctor delete() {
        Doctor doctor = this.retrieve();
        if (doctor == null) {
            return null;
        }
        String query = "DELETE FROM doctor WHERE id=?";
        try (Connection conn = DBConnection.connectionToDatabase();
                PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, this.doctor.getId());
            st.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return doctor;
    }

    @Override
    public List<Integer> findPatientsIds(){
        List<Integer> list = new ArrayList<Integer>();
        String query = "SELECT DISTINCT id_patient FROM appointment WHERE id_doctor = ?";
        try (Connection conn = DBConnection.connectionToDatabase();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, this.doctor.getId());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    list.add(rs.getInt(1));
                }
            } catch (Exception ex){
                throw new Exception(ex.getMessage());
            }
        } catch (SQLException ex){
            System.out.println(SQL_EXCEPTION + ex.getMessage());
        } catch (Exception ex){
            System.out.println(LISTING_ERROR + ex.getMessage());
        }
        return list;
    }

    @Override
    public Map<String, Integer> findAppointments() {
        Map<String, Integer> map = new HashMap<>();
        String query = "SELECT id_patient, date FROM appointment WHERE id_doctor = ?";
        try (Connection conn = DBConnection.connectionToDatabase();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, this.doctor.getId());
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    String date = dateFormat.format(rs.getTimestamp(2));
                    map.put(date, rs.getInt(1));
                }
            } catch (Exception ex){
                throw new Exception(ex.getMessage());
            }
        } catch (SQLException ex){
            System.out.println(SQL_EXCEPTION + ex.getMessage());
        } catch (Exception ex){
            System.out.println(LISTING_ERROR + ex.getMessage());
        }
        return map;
    }

    //revisar
    @Override
    public List<Doctor> findDoctorsByPatient(int patientId) {
        List<Doctor> list = new ArrayList<Doctor>();
        String query = "SELECT DISTINCT id, name, specialization FROM appointment a JOIN doctor d ON a.id_doctor = d.id WHERE id_patient = ?";
        try (Connection conn = DBConnection.connectionToDatabase();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, patientId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Doctor doctor = new Doctor(rs.getInt(1), rs.getString(2), rs.getString(3));
                    list.add(doctor);
                }
            } catch (Exception ex){
                throw new Exception(ex.getMessage());
            }
        } catch (SQLException ex){
            System.out.println(SQL_EXCEPTION + ex.getMessage());
        } catch (Exception ex){
            System.out.println(LISTING_ERROR + ex.getMessage());
        }
        return list;
    }

    //"2013-09-04 13:30:00"
    @Override
    public Map<String, Doctor> findAppointmentsByPatiend(int patientId) {
        Map<String, Doctor> map = new HashMap<>();
        String query = "SELECT date, id_doctor, name, specialization " +
                       "FROM appointment a JOIN doctor d ON a.id_doctor = d.id " +
                       "WHERE a.id_patient = ?";
        try (Connection conn = DBConnection.connectionToDatabase();
             PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, patientId);
            try (ResultSet rs = st.executeQuery()) {
                while (rs.next()){
                    Doctor doctor = new Doctor(rs.getInt(2), rs.getString(3), rs.getString(4));
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
                    String date = dateFormat.format(rs.getTimestamp(1));
                    map.put(date, doctor);
                }
            } catch (Exception ex){
                throw new Exception(ex.getMessage());
            }
        } catch (SQLException ex){
            System.out.println(SQL_EXCEPTION + ex.getMessage());
        } catch (Exception ex){
            System.out.println(LISTING_ERROR + ex.getMessage());
        }
        return map;
    }

    // resivar, hacer gateway, controllers aqui y alla
    @Override
    public Map<String, Doctor> createAppointment(int patientId, String date) {
        Map<String, Doctor> response = new HashMap<>();
        String query = "INSERT INTO appointment (id_doctor, id_patient, date) VALUES (?, ?, ?)";
        try (Connection conn = DBConnection.connectionToDatabase();
                PreparedStatement st = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            st.setInt(1, this.doctor.getId());
            st.setInt(2, patientId);
            st.setTimestamp(3, Timestamp.valueOf(date));
            st.executeUpdate();
            try (ResultSet rs = st.getGeneratedKeys()) {
                rs.next();
                Doctor doctor = this.retrieve();
                response.put(date, doctor);
            } catch (Exception e) {
                System.out.print(SAVING_ERROR);
            }
        } catch (SQLException e) {
            System.out.print(SQL_EXCEPTION);
        }
        return response;
    }
}
