package com.party.planner.controller;

import com.party.planner.controller.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import javax.sql.*;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class PartyRepository implements Repository {


    @Autowired
    private DataSource dataSource;

// TODO: QUERYS FROM SQL here

    @Override
    public void addGuest (String firstname, String lastname, String gender){
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Guests]([FirstName],[LastName],[Gender])\n" +
                 "VALUES (?,?,?)")){
        ps.setString(1, firstname);
        ps.setString(2, lastname);
        ps.setString(3, gender);
        ps.executeUpdate();
    }
    catch (SQLException e){
        throw new RepositoryExceptions("Nu blev det supertokigt i PartyRepo", e);
    }
}


}
