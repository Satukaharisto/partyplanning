package com.party.planner.controller.repository;


import com.party.planner.controller.domain.*;
import org.mindrot.jbcrypt.BCrypt;
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
    public Integer checkLogin(String username, String password) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [UserID] , [Password] FROM [dbo].[User3] WHERE ([UserName] = (?) ) ")) {
            ps.setString(1, username);
            ResultSet results = ps.executeQuery();

            if (results.next()) {
                String hashedPassword = results.getString("Password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    return results.getInt("UserId");
                }
            }
            return null;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in checklogin - PartyRepository", e);
        }
    }
    @Override
    public int addUser(String userName, String password, String email) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[User3]([UserName], [Password], [Email]) " +
                     "VALUES (?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, userName);
            ps.setString(2, BCrypt.hashpw(password, BCrypt.gensalt())); //krypterar lösenordet i databasen
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
    public int addGuest(int eventId, String firstname, String lastname, String email, String gender) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT [dbo].[Guest3]([FirstName],[LastName],[Email], [Gender], [Event_ID]) " +
                     "VALUES (?,?,?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, email);
            ps.setString(4, gender);
            ps.setInt(5, eventId);
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
             PreparedStatement ps = conn.prepareStatement("INSERT FoodPreference3\n" +
                     "(Guest_ID,FoodPreference, Alcohol, Allergie)\n" +
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
    public int addBudgetItem(String item, int price, int eventId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Budget3]([Item],[Price],[Event_ID]) " +
                     "VALUES (?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, item);
            ps.setInt(2, price);
            ps.setInt(3, eventId);
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
    public int addToDo(Date date, String toDo, boolean done, int eventId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Checklist3]([Date],[Todo],[Done],[Event_ID]) " +
                     "VALUES (?,?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setDate(1, date);
            ps.setString(2, toDo);
            ps.setBoolean(3, done);
            ps.setInt(4, eventId);
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
             PreparedStatement ps = conn.prepareStatement("SELECT  [UserName] FROM [dbo].[User3] WHERE [UserName] = ? ")) {
            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next())
                    return false;
            }
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in useralreadyexists - PartyRepository", e);
        }
        return true;
    }

    public boolean budgetItemAlreadyExists(String item, int eventId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [Item] FROM [dbo].[Budget3] WHERE [item] = ? AND Event_Id = ?")) {
            ps.setString(1, item);
            ps.setInt(2, eventId);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in budgetItemAlready exists - PartyRepository", e);
        }
    }

// LOGIN




    @Override
    public void updateGuest(int eventId, int id, String firstname, String lastname, String email, String gender) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Guest3 " +
                             "SET Event_ID = (?), FirstName = (?), LastName = (?), Email = (?), Gender = (?) " +
                             "WHERE GuestID = (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, eventId);
            ps.setString(2, firstname);
            ps.setString(3, lastname);
            ps.setString(4, email);
            ps.setString(5, gender);
            ps.setInt(6, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in updateGuest - PartyRepository", e);
        }
    }

    @Override
    public void updateFoodPreference(int id, int guestId, String allergy, String foodPreference, String alcohol) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE FoodPreference3 " +
                             "SET Guest_ID = (?), FoodPreference = (?), Alcohol = (?), Allergie = (?) " +
                             "WHERE RsvpID = (?)", Statement.RETURN_GENERATED_KEYS)) {
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

    @Override
    public void updateBudget(int eventId, int id, String item, int price) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Budget3 " +
                             "SET event_id = (?), item = (?), price = (?) " +
                             "WHERE BudgetID = (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, eventId);
            ps.setString(2, item);
            ps.setInt(3, price);
            ps.setInt(4, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in updateBudget - Partyrepo", e);
        }
    }

    @Override
    public void updateChecklist(int id, int eventId, Date date, String toDo, boolean done) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Checklist3 " +
                             "SET Event_ID = (?), date = (?), todo = (?), done = (?) " +
                             "WHERE ChecklistID = (?)", Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, eventId);
            ps.setDate(2, date);
            ps.setString(3, toDo);
            ps.setBoolean(4, done);
            ps.setInt(5, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in updateChecklist - partyrepo", e);
        }
    }

    @Override
    public void updateEvent(int eventId, String eventName, Date eventDate, int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Event3 " +
                             "SET EventName = (?), EventDate = (?), User_ID = (?) " +
                             "WHERE EventID = (?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, eventName);
            ps.setDate(2, eventDate);
            ps.setInt(3, userId);
            ps.setInt(4, eventId);
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in updateEvent - partyrepo", e);
        }
    }


    @Override
    public void deleteBudget(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM Budget3 WHERE BudgetID = (?)")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in deleteBudget - Partyrepo", e);
        }
    }

    @Override
    public void deleteChecklist(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM Checklist3 WHERE ChecklistID = (?) ")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in deleteChecklist - Partyrepo", e);
        }
    }

    @Override
    public void deleteFoodPreference(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM FoodPreference3 WHERE RsvpID = (?)")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in deleteFoodPreference - Partyrepo", e);
        }
    }

    @Override
    public void deleteFoodPreferenceByGuestId(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM FoodPreference3 WHERE Guest_ID = (?)")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in deleteFoodPreference - Partyrepo", e);
        }
    }

    @Override
    public void deleteGuest(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM Guest3 WHERE GuestID = (?)")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in deleteGuest - Partyrepo", e);
        }
    }

    @Override
    public void deleteGuestsByEventId(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM Guest3 WHERE Event_ID = (?) ")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in deleteGuestsByEventId - Partyrepo", e);
        }
    }

    @Override
    public void deleteEvent(int id) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM Event3 WHERE EventId = (?)")) {
            ps.setInt(1, id);
            ps.execute();
        } catch (SQLException e) {
            throw new RepositoryExceptions("Something went wrong in deleteEvent - Partyrepo", e);
        }
    }


    // LISTS
    @Override
    public List<Guest> getGuestList(int eventId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT GuestId, FirstName, lastname, email, gender from Guest3\n " +
                     "WHERE Event_ID = (?) ")) {
            ps.setInt(1, eventId);

            ResultSet rs = ps.executeQuery();
            List<Guest> guests = new ArrayList<>();
            while (rs.next()) {
                guests.add(new Guest(rs.getInt("GuestId"),
                        rs.getString("FirstName"),
                        rs.getString("lastname"),
                        rs.getString("email"),
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
             PreparedStatement ps = conn.prepareStatement("SELECT RsvpID, Guest_ID, FoodPreference, Alcohol, Allergie" +
                     " FROM FoodPreference3 WHERE Guest_ID = (?)")) {
            ps.setInt(1, guestId);
            ResultSet rs = ps.executeQuery();
            Food food = null;
            if (rs.next()) {
                food = new Food(rs.getInt("RsvpID"),
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
    public List<Budget> getBudgetList(int eventId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [BudgetID], [Item], [Price] from [dbo].[Budget3]\n " +
                     "WHERE Event_ID = (?) ")) {
            ps.setInt(1, eventId);

            ResultSet rs = ps.executeQuery();
            List<Budget> budgetList = new ArrayList<>();
            while (rs.next()) {
                budgetList.add(new Budget(rs.getInt("BudgetID"),
                        rs.getString("item"),
                        rs.getInt("price")));
            }
            return budgetList;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in budgetlist - PartyRepository", e);
        }
    }

    @Override
    public List<Checklist> getChecklist(int eventId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [ChecklistID], [Date], [Todo], [Done] from [dbo].[Checklist3]\n " +
                     "WHERE Event_ID = (?) ")) {
            ps.setInt(1, eventId);

            ResultSet rs = ps.executeQuery();
            List<Checklist> checklist = new ArrayList<>();
            while (rs.next()) {
                checklist.add(new Checklist(rs.getInt("ChecklistID"),
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

    public int budgetSum(int eventId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT [Price] from [dbo].[Budget3]\n " +
                     "WHERE Event_ID = (?) ")) {

            ps.setInt(1, eventId);
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

    @Override
    public int addEvent(String name, java.sql.Date date, int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO [dbo].[Event3]([EventName],[EventDate],[User_ID]) " +
                     "VALUES (?,?,?) ", Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setDate(2, date);
            ps.setInt(3, userId);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            int eventId = -1;
            while (rs.next()) {
                eventId = rs.getInt(1);
            }
            return eventId;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in addevent - PartyRepository", e);
        }
    }

    @Override
    public List<Event> getEventList(int userId) {
        try (Connection conn = dataSource.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT EventID, EventName, EventDate from Event3 " +
                     "WHERE User_ID = (?) ")) {
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();
            List<Event> eventList = new ArrayList<>();
            while (rs.next()) {
                eventList.add(new Event(rs.getInt("EventID"),
                        rs.getString("EventName"),
                        rs.getDate("EventDate")));
            }
            return eventList;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong in eventlist - PartyRepository", e);
        }
    }

    @Override
   public List<Inspiration> listInspiration() {
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT InspirationID, Category, InspirationName, InspirationText,InspirationLink, InspirationPicture FROM Inspiration3 " )) {
            List<Inspiration> inspirationItems = new ArrayList<>();
            while (rs.next()) inspirationItems.add(rsInspiration(rs));

            return inspirationItems;
        } catch (SQLException e) {
            throw new RepositoryExceptions("something went wrong with Inspiration - PartyRepository", e);
        }
    }
    private Inspiration rsInspiration(ResultSet rs) throws SQLException {
        return new Inspiration (rs.getInt("InspirationID"), rs.getString("Category"), rs.getString("InspirationName"), rs.getString("InspirationText"), rs.getString("InspirationLink"), rs.getString("InspirationPicture"));
    }
}

