package HospitalManagementSystem;

import java.sql.*;
import java.util.Scanner;

public class HospitalManagementSystem {
    private static final String url="jdbc:mysql://localhost:3306/hospital";
    private static final String username="root";
    private static final String password="123456789";

    public static void main(String[] args){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
        }catch(ClassNotFoundException e){
            e.printStackTrace();
        }

        Scanner sc=new Scanner(System.in);

        try{
            Connection con= DriverManager.getConnection(url,username,password);
            Patient patient=new Patient(con,sc);
            Doctor doctor=new Doctor(con);

            while(true){
                System.out.println("HOSPITAL MANAGEMENT SYSTEM");
                System.out.println("1.Add Patient");
                System.out.println("2.View Patient");
                System.out.println("3.View Doctors");
                System.out.println("4.Book Appointment");
                System.out.println("5.Exit");
                System.out.println("Enter your choice:-");
                int choice=sc.nextInt();

                switch(choice){
                    case 1:
                        //Add patient
                        patient.addPatient();
                        System.out.println();
                        break;
                    case 2:
                        //view patient
                        patient.viewPatients();
                        System.out.println();
                        break;
                    case 3:
                        //view Doctor
                        doctor.viewDoctors();
                        System.out.println();
                        break;

                    case 4:
                        //book appointment
                        bookAppointment(patient,doctor,con,sc);
                        System.out.println();
                        break;

                    case 5:
                        System.out.println("THANK YOU for using HOSPITAL MANAGEMENT SYSTEM !!");
                        return;
                    default:
                        System.out.println("enter valid choice ");
                        break;


                }
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public static void bookAppointment(Patient patient,Doctor doctor,Connection connection,Scanner scanner){
        System.out.print("Enter Patient id:-");
        int patientId=scanner.nextInt();
        System.out.print("Enter Doctor Id:-");
        int doctorId=scanner.nextInt();
        System.out.print("Enter appointment date(YYYY-MM-DD:-");
        String appointmentDate=scanner.next();

        if(patient.getPatientById(patientId) && doctor.getDoctorById(doctorId)){
            if(checkDoctorAvailability(doctorId,appointmentDate,connection)){
                String appointmentQuery="Insert into appointments(patient_id,doctor_id,appointment_date) values(?,?,?)";
                try{
                    PreparedStatement ps=connection.prepareStatement(appointmentQuery);
                    ps.setInt(1,patientId);
                    ps.setInt(2,doctorId);
                    ps.setString(3,appointmentDate);
                    int rowsAffected=ps.executeUpdate();
                    if(rowsAffected>0){
                        System.out.println("appointment booked");

                    }else{
                        System.out.println("failed to book appointment");
                    }

                }catch(SQLException e){
                    e.printStackTrace();
                }
            }else{
                System.out.println("Doctor not available on this date!!");
            }
        }else{
            System.out.println("either doctor or patient doesn't exist");
        }
    }

    public static boolean checkDoctorAvailability(int doctorId,String appointmentDate, Connection connection){
        String query="select count(*) from appointments where doctor_id=? and appointment_date=?";
        try{
            PreparedStatement ps=connection.prepareStatement(query);
            ps.setInt(1,doctorId);
            ps.setString(2,appointmentDate);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                int count=rs.getInt(1);
                if(count==0){
                    return true;
                }
                else{
                    return false;
                }
            }

        }catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }


}
