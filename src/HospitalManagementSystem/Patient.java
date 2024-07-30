package HospitalManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Patient {
    private Connection connection;
    private Scanner scanner;

    public Patient(Connection connection,Scanner scanner){
        this.connection=connection;
        this.scanner=scanner;
    }

    public void addPatient(){
        System.out.print("enter Patient Name:-");
        String name=scanner.next();
        System.out.print("enter Patient age :-");
        int age=scanner.nextInt();
        System.out.print("enter patient gender:-");
        String gender=scanner.next();

        try{
            String Query="INSERT into patients(name,age,gender) values(?,?,?)";
            PreparedStatement ps=connection.prepareStatement(Query);
            ps.setString(1,name);
            ps.setInt(2,age);
            ps.setString(3,gender);
            int affectedRows=ps.executeUpdate();

            if(affectedRows>0){
                System.out.println("Patient added successfully!!");
            }
            else{
                System.out.println("Failed to add patient!!");
            }
        }catch(SQLException e){
            e.printStackTrace();
        }
    }

    public void viewPatients(){
        String query="select * from patients";
        try{
            PreparedStatement ps=connection.prepareStatement(query);
            ResultSet rs=ps.executeQuery();
            System.out.println("Patients:-");
            System.out.println("+-----------+-----------------+------------+---------------+");
            System.out.println("| Patient Id| Name            | Age        | Gender        |");
            System.out.println("+-----------+-----------------+------------+---------------+");
            while(rs.next()){
                int id=rs.getInt("id");
                String name=rs.getString("name");
                int age=rs.getInt("age");
                String gender=rs.getString("gender");
                System.out.printf("| %-9s | %-15s | %-10s | %-13s |\n",id,name,age,gender);

            }
            System.out.println("+-----------+-----------------+------------+---------------+");

        }catch(SQLException e){
            e.printStackTrace();
        }

    }

    public boolean getPatientById(int id){
        String query="select * from patients where id=?";
        try{
            PreparedStatement ps=connection.prepareStatement(query);
            ps.setInt(1,id);
            ResultSet rs=ps.executeQuery();
            if(rs.next()){
                return true;
            }else{
                return false;
            }

        }
        catch(SQLException e){
            e.printStackTrace();
        }
        return false;
    }

}
