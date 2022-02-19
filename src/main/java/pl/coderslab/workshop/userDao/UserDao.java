package pl.coderslab.workshop.userDao;


import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;
import java.util.Arrays;

public class UserDao {
    private static final String CREATE_DATABASE_QUERY = "CREATE DATABASE IF NOT EXISTS workshop\n" +
            "    CHARACTER SET utf8mb4\n" +
            "    COLLATE utf8mb4_unicode_ci;";
    private static final String CREATE_TABLE_QUERY ="CREATE TABLE IF NOT EXISTS users(\n" +
            "    id INT(11) NOT NULL AUTO_INCREMENT,\n" +
            "    email VARCHAR(255) NOT NULL UNIQUE,\n" +
            "    username VARCHAR(255) NOT NULL,\n" +
            "    password VARCHAR(60) NOT NULL,\n" +
            "    PRIMARY KEY (id)\n" +
            "    );";
    private static final String CREATE_USER_QUERY = "INSERT INTO users('email', 'username', password) VALUES(?,?,?)";
    private static final String SELECT_USER_QUERY = "SELECT * FROM users WHERE  id =?";
    private static final String SELECT_ALL_USERS_QUERY = "SELECT * FROM users";
    private static final String UPDATE_USER_PASSWORD_QUERY = "UPDATE users SET password = ?  WHERE id = ?";
    private static final String UPDATE_USER_USER_QUERY = "UPDATE users SET email = ?, username = ?  WHERE id = ?;";
    private static final String DELETE_USER_QUERY = "DELETE FROM users WHERE id = ?";
    private static final String DELETE_ALL_USERS_QUERY = "DELETE FROM users";


    public void createDatabase(){
        try (Connection connection = DBUtil.connect()){
            connection.createStatement().executeUpdate(CREATE_DATABASE_QUERY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas tworzenia Bazy Danych", throwables);
        }
    }
    public void createTable(){
        try (Connection connection = DBUtil.connect("workshop")){
            connection.createStatement().executeUpdate(CREATE_TABLE_QUERY);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas tworzenia Tabeli użytkowników", throwables);
        }
    }

    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
    public User createUser (User user){
        try(Connection conn = DBUtil.connect("workshop")){
            PreparedStatement statement = conn.prepareStatement(CREATE_USER_QUERY, Statement.RETURN_GENERATED_KEYS);
            statement.setString(1,user.getEmail());
            statement.setString(2,user.getUsername());
            statement.setString(3,hashPassword(user.getPassword()));
            statement.executeUpdate();
            ResultSet resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                user.setId(resultSet.getInt(1));
            }
            return user;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas tworzenia nowego użytkownika",throwables);
        }
    }
    public User readUser(int userID){
        try (Connection connection = DBUtil.connect("workshop")) {
            PreparedStatement statement = connection.prepareStatement(SELECT_USER_QUERY);
            statement.setInt(1,userID);
            ResultSet resultSet = statement.getResultSet();
            if(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                return user;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas pobierania użytkownika",throwables);
        }
        return null;
    }
    public User[] readAllUsers(){
        try (Connection connection = DBUtil.connect("workshop")) {
            User[] users = new User[0];
            PreparedStatement statement = connection.prepareStatement(SELECT_ALL_USERS_QUERY);
            ResultSet resultSet = statement.getResultSet();
            while(resultSet.next()){
                User user = new User();
                user.setId(resultSet.getInt("id"));
                user.setEmail(resultSet.getString("email"));
                user.setUsername(resultSet.getString("username"));
                user.setPassword(resultSet.getString("password"));
                users = Arrays.copyOf(users, users.length +1);
                users[users.length -1] = user;
            }
            return users;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas pobierania użytkowników",throwables);
        }
    }
    public void update (User user){
        try(Connection conn = DBUtil.connect("workshop")){
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_USER_QUERY);
            statement.setString(1,user.getEmail());
            statement.setString(2,user.getUsername());
            statement.setInt(3,user.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas edycji użytkownika",throwables);
        }

    }
    public void updatePassword (User user){
        try(Connection conn = DBUtil.connect("workshop")){
            PreparedStatement statement = conn.prepareStatement(UPDATE_USER_PASSWORD_QUERY);
            statement.setString(1,hashPassword(user.getPassword()));
            statement.setInt(2,user.getId());
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas edycji hasła",throwables);
        }
    }
    public void deleteUser(int userId){
        try (Connection connection = DBUtil.connect("workshop")){
            PreparedStatement statement = connection.prepareStatement(DELETE_USER_QUERY);
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas usuwania użytkownika",throwables);
        }
    }
    public void deleteAllUser(){
        try (Connection connection = DBUtil.connect("workshop")){
            PreparedStatement statement = connection.prepareStatement(DELETE_ALL_USERS_QUERY);
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw new DaoException("Błąd podczas usuwania użytkowników",throwables);
        }
    }


}
