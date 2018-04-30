package com.party.planner.controller.repository;

import com.party.planner.controller.domain.Budget;
import com.party.planner.controller.domain.Food;
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

    // ADD
    @Override
    public int addUser(String userName, String password, String email) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Users]([UserName],[Password], [Email]) " +
                     "VALUES (?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, userName);
            ps.setString(2, password);
            ps.setString(3, email);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int userId = -1;
            while (rs.next()) {
                userId = rs.getInt(1);
            }
            return userId;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in adduser - PartyRepository", e);
        }
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
            throw new RepositoryExceptions("something went wrong in addguest - PartyRepository", e);
        }
    }

    @Override
    public int addFoodPreference(int guestId, String allergy, String foodPreference, String alcohol) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT FoodPreferences\n" +
                     "(Guest_ID, FoodPreference, Alcohol, Allergie)\n" +
                     "VALUES\n" +
                     "(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, guestId);
            ps.setString(2, foodPreference);
            ps.setString(3, alcohol);
            ps.setString(4, allergy);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int foodId = -1;
            while (rs.next()) {
                foodId = rs.getInt(1);
            }
            return foodId;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in addFoodPref.", e);
        }
    }

    @Override
    public int addBudgetItem(String item, int price, int userId) {
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
            throw new RepositoryExceptions("something went wrong in addbudget - PartyRepository", e);
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
            throw new RepositoryExceptions("something went wrong in addTodo - PartyRepository", e);
        }
    }

// CHECK DUPLICATE

    public boolean userAlreadyExists(String username) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT  [UserName] FROM [dbo].[Users] WHERE [UserName] = ? ")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return false;
            }
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in useralreadyexists - PartyRepository");
        }
        return true;
    }

    public boolean budgetItemAlreadyExists(String item, int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT  [Item] FROM [dbo].[Budget] WHERE [item] = ? AND User_Id = ?")) {
            ps.setString(1, item);
            ps.setInt(2, userId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in budgetItemAlready exists - PartyRepository");
        }
    }


// LOGIN

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
            throw new RepositoryExceptions("something went wrong in checklogin - PartyRepository", e);
        }
    }

    @Override
    public void updateGuest(int id, int userId, String firstname, String lastname, String gender) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Guests " +
                             "SET User_ID = (?), FirstName = (?), LastName = (?), Gender = (?) " +
                             "WHERE GuestID = (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, userId);
            ps.setString(2, firstname);
            ps.setString(3, lastname);
            ps.setString(4, gender);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in updateGuest - PartyRepository", e);
        }
    }

    @Override
    public void updateFoodPreference(int id, int guestId, String allergy, String foodPreference, String alcohol) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE FoodPreferences " +
                             "SET Guest_ID = (?), FoodPreference = (?), Alcohol = (?), Allergie = (?) " +
                             "WHERE id = (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, guestId);
            ps.setString(2, foodPreference);
            ps.setString(3, alcohol);
            ps.setString(4, allergy);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in updateGuest - PartyRepository", e);
        }
    }

    // LISTS
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
            throw new RepositoryExceptions("something went wrong in guestlist - PartyRepository", e);
        }
    }

    @Override
    public Food getFoodPreference(int guestId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id, Guest_ID, FoodPreference, Alcohol, Allergie" +
                     " FROM FoodPreferences WHERE Guest_ID = (?)")) {
            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();
            Food food = null;
            if (rs.next()) {
                food = new Food(rs.getInt("id"),
                        rs.getString("FoodPreference"),
                        rs.getString("Alcohol"),
                        rs.getString("Allergie"));
            }
            return food;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in foodlist - partyrepo", e);
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
                        rs.getString("item"),
                        rs.getInt("price")));
            }
            return budgetList;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in budgetlist - PartyRepository", e);
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
                checklist.add(new ToDo(rs.getInt("ID"),
                        rs.getDate("Date"),
                        rs.getString("Todo"),
                        rs.getBoolean("Done")));

            }
            return checklist;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in checklist - PartyRepository", e);
        }
    }

// BUDGET CALCULATOR

    public int budgetSum(int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [Price] from [dbo].[Budget]\n " +
                     "WHERE User_ID = (?) ")) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            int total = 0;
            while (rs.next()) {
                total = total + rs.getInt("Price");
            }
            return total;


        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in budgetsum - PartyRepository", e);
        }
    }

    // CHANGE VALUE
    public void changeBudgetItemPrice(int userId, String item, int price) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("UPDATE [dbo].[Budget]\n " +
                     "SET [Price] = (?) \n " +
                     "WHERE Item = (?) AND User_ID = (?) ")) {
            ps.setInt(1, userId);
            ps.setString(2, item);
            ps.setInt(3, price);
            ps.executeQuery();
//            int id = -1;
//            while (rs.next()) {
//                id = rs.getInt(id);
//            }
//            return id;
        } catch (SQLException e) {
            throw new RepositoryExceptions("Couldn't update");
        }
    }
}

