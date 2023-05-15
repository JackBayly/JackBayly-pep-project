package DAO;
import Model.Account;
import Util.ConnectionUtil;
import java.sql.*;

public class AccountDAO {
    public Account getAccount(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "SELECT account_id, username, password FROM Account WHERE account_id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //Set the SQL parameters
            preparedStatement.setInt(1,userId);
            //Execute the SQL
            ResultSet pkeyResultSet =  preparedStatement.executeQuery();
            //Return the results
            if(pkeyResultSet.next()) {
                int account_id = pkeyResultSet.getInt(1);
                String username = pkeyResultSet.getString(2);
                String password = pkeyResultSet.getString(3);
                return new Account(account_id, username, password);
            }
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account insertAccount(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "INSERT INTO Account(username, password) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //Set the SQL parameters
            preparedStatement.setString(1,account.username);
            preparedStatement.setString(2,account.password);
            //Execute the SQL
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            //Return the results
            if(pkeyResultSet.next()){
                int generated_account_id = (int) pkeyResultSet.getInt(1);
                return new Account(generated_account_id, account.username,account.password);
            }
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account getAccountByUserName(String username) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "SELECT account_id, username, password FROM Account WHERE username= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Set the SQL parameters
            preparedStatement.setString(1,username);
            //Execute the SQL
            ResultSet pkeyResultSet =  preparedStatement.executeQuery();
            //Return the results
            if(pkeyResultSet.next()) {
                int account_id = pkeyResultSet.getInt(1);
                String user = pkeyResultSet.getString(2);
                String password = pkeyResultSet.getString(3);
                return new Account(account_id, user, password);
            }
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public Account login(Account account) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "SELECT account_id, username, password FROM Account WHERE username= ? AND password= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Set the SQL parameters
            preparedStatement.setString(1,account.username);
            preparedStatement.setString(2,account.password);
            //Execute the SQL
            ResultSet pkeyResultSet =  preparedStatement.executeQuery();
            //Return the results
            if(pkeyResultSet.next()) {
                int account_id = pkeyResultSet.getInt(1);
                String user = pkeyResultSet.getString(2);
                String password = pkeyResultSet.getString(3);
                return new Account(account_id, user, password);
            }
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }
}
