package com.party.planner.controller.repository;

import com.party.planner.controller.domain.Guest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
            throw new RepositoryExceptions("Nu blev det supertokigt i addUser - partyrepo", e);
        }
    }

    @Override                   //GÄSTER    ------------- här kopplar vi ihop databasen med HTML filen
    public int addGuest(String firstname, String lastname, String gender, int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Guests]([FirstName],[LastName],[Gender], [User_ID]) " +
                     "VALUES (?,?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, gender);
            ps.setInt(4, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int guestId = -1;
            while (rs.next()) {
                guestId = rs.getInt(1);
            }
            return guestId;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i addguest - partyrepo", e);
        }
    }

    public Integer checkLogin(String username, String password) {
        try (Connection conn = dataSource.getConnection();

             PreparedStatement ps = conn.prepareStatement("SELECT [UserID] FROM  [dbo].[Users] WHERE ([UserName] = (?) AND [Password] = (?)) ")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet results = ps.executeQuery();
            if (results.next()) {
                return results.getInt("UserId");
            }
            return null;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i checklogin - PartyRepo", e);
        }
    }

    @Override
    public List<Guest> getGuestList(int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT GuestId, FirstName, lastname, gender from Guests\n " +
                     "WHERE User_ID = (?) ")) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            List<Guest> guests = new ArrayList<>();
            while (rs.next()) {
                guests.add(new Guest(rs.getInt("GuestId"),
                        rs.getString("FirstName"),
                        rs.getString("lastname"),
                        rs.getString("gender")));

            }
            return guests;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i checklogin - PartyRepo", e);
        }
    }
}
