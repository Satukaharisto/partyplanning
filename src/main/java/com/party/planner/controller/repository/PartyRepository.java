package com.party.planner.controller.repository;

import com.party.planner.controller.domain.Budget;
import com.party.planner.controller.domain.ToDo;
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

    @Override
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

public boolean userAlreadyExists(String username){
    try (Connection conn = dataSource.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT  [UserName] FROM [dbo].[Users] WHERE [UserName] = ? " )) {
        ps.setString(1, username);
try (ResultSet rs = ps.executeQuery()) {
    if (rs.next())
        return false;
}
    }
    catch (SQLException e) {
        throw new RepositoryExceptions("tokigt");
    }
    return true;
}
    @Override
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
@Override
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

    @Override                   //BUDGET   ------------- här kopplar vi ihop databasen med HTML filen
    public int addBudgetItem (String item, int price, int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Budget]([Item],[Price], [User_ID]) " +
                     "VALUES (?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item);
            ps.setInt(2, price);

            ps.setInt(3, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i addbudget- partyrepo", e);
        }
    }
    @Override
    public List<Budget> getBudgetList(int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [ID], [Item], [Price] from [dbo].[Budget]\n " +
                     "WHERE User_ID = (?) ")) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            List<Budget> budgetList = new ArrayList<>();
            while (rs.next()) {
                budgetList.add(new Budget(rs.getInt("ID"),
                        rs.getString ("item"),
                        rs.getInt("price")));
            System.out.println("fungerar detta i budgetlist ");
            }
            return budgetList;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i checklogin - PartyRepo", e);
        }
    }
    @Override
    public int addToDo(Date date, String toDo, boolean done, int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Checklist]([Date],[Todo],[Done], [User_ID]) " +
                     "VALUES (?,?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, date);
            ps.setString(2, toDo);
            ps.setBoolean(3, done);
            ps.setInt(4, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int id = -1;
            while (rs.next()) {
                id = rs.getInt(1);
            }
            return id;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i addtodo - partyrepo", e);
        }
    }
    @Override
    public List<ToDo> getChecklist(int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [ID], [Date], [Todo], [Done] from [dbo].[Checklist]\n " +
                     "WHERE User_ID = (?) ")) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            List<ToDo> checklist = new ArrayList<>();
            while (rs.next()) {
                checklist.add(new ToDo(rs.getInt(""),
                        rs.getDate("Date"),
                        rs.getString("Todo"),
                        rs.getBoolean("Done")));

            }
            return checklist;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Nu blev det supertokigt i checklist - PartyRepo", e);
        }
    }

}
