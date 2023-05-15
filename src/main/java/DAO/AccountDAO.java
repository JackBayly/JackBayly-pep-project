package DAO;

import Model.Account;
import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    public Account getAccount(int userId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //Write SQL logic here
            String sql = "SELECT account_id, username, password FROM Account WHERE account_id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1,userId);

            ResultSet pkeyResultSet =  preparedStatement.executeQuery();

            if(pkeyResultSet.next()) {
                System.out.println("Helloooo Person");
                int account_id = pkeyResultSet.getInt(1);

                String username = pkeyResultSet.getString(2);

                String password = pkeyResultSet.getString(3);
                System.out.println(username);
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
            //Write SQL logic here
            String sql = "INSERT INTO Account(username, password) VALUES (?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.

            preparedStatement.setString(1,account.username);
            preparedStatement.setString(2,account.password);

            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            System.out.println("Looking for message id");
            if(pkeyResultSet==null){
                System.out.println("no keys returned");
            }
            //int generated_message_id2 = (int) pkeyResultSet.getInt(0);
            //System.out.println(generated_message_id2);

            if(pkeyResultSet.next()){
                System.out.println("HIt the next");
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
            //Write SQL logic here
            String sql = "SELECT account_id, username, password FROM Account WHERE username= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setString(1,username);

            ResultSet pkeyResultSet =  preparedStatement.executeQuery();

            if(pkeyResultSet.next()) {
                System.out.println("Helloooo Person");
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
            //Write SQL logic here
            String sql = "SELECT account_id, username, password FROM Account WHERE username= ? AND password= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setString(1,account.username);
            preparedStatement.setString(2,account.password);
            ResultSet pkeyResultSet =  preparedStatement.executeQuery();

            if(pkeyResultSet.next()) {
                System.out.println("Helloooo Person");
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
