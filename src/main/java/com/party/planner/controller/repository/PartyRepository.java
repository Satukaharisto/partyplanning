package com.party.planner.controller.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;

@Component
public class PartyRepository implements Repository {
    @Autowired
    private DataSource dataSource;

// TODO: QUERYS FROM SQL here

    @Override                   //USERS    ------------- här kopplar vi ihop databasen med HTML filen
    public int addUser(String userName, String password) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Users]([UserName],[Password]) " +
                     "VALUES (?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int userId = -1;
            while (rs.next()) {
                userId = rs.getInt(1);
            }
            return userId;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i när du skapar en user", e);
        }
    }
    @Override                   //GÄSTER    ------------- här kopplar vi ihop databasen med HTML filen
    public void addGuest(String firstname, String lastname, String gender) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Guests]([FirstName],[LastName],[Gender]) " +
                     "VALUES (?,?,?) ")) {
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, gender);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i PartyRepo", e);
        }


    }
}
