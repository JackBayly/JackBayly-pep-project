package DAO;
import Util.ConnectionUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import Model.Message;

public class MessageDAO {
    public Message insertMessage(Message message) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            //Set the SQL parameters
            preparedStatement.setInt(1,message.posted_by);
            preparedStatement.setString(2,message.message_text);
            preparedStatement.setLong(3,message.time_posted_epoch);
            //Execute the SQL
            preparedStatement.executeUpdate();
            ResultSet pkeyResultSet = preparedStatement.getGeneratedKeys();
            //Return the results
            if(pkeyResultSet.next()){
                int generated_message_id = (int) pkeyResultSet.getInt(1);
                return new Message(generated_message_id, message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
            }

        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message deleteMessage(int id) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Set the SQL parameters
            preparedStatement.setInt(1,id);
            //Execute the SQL
            int rows = preparedStatement.executeUpdate();
            //Return the results
            if(rows>0){
                return new Message(id, 0,null,0);
            }
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }
    public Message getMessage(int id){
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "SELECT posted_by,message_text, time_posted_epoch FROM Message WHERE message_id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Set the SQL parameters
            preparedStatement.setInt(1,id);
            //Execute the SQL
            ResultSet pkeyResultSet =  preparedStatement.executeQuery();
            //Return the results
            if(pkeyResultSet.next()) {
                int posted_by = pkeyResultSet.getInt(1);
                String message_text = pkeyResultSet.getString(2);
                long time_posted_epoch = pkeyResultSet.getLong(3);
                return new Message(id, posted_by, message_text, time_posted_epoch);
            }
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessages() {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "SELECT * FROM Message";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Execute the SQL
            ResultSet pkeyResultSet =  preparedStatement.executeQuery();
            //Return the results
            ArrayList<Message> messageList= new ArrayList<Message>();
            while(pkeyResultSet.next()) {
                int message_id = pkeyResultSet.getInt(1);
                int posted_by = pkeyResultSet.getInt(2);
                String message_text = pkeyResultSet.getString(3);
                long time_posted_epoch = pkeyResultSet.getLong(4);
                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messageList.add(message);
            }
            return messageList;
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
            return null;
    }

    public Message updateMessage(int messageId, String updatedtext) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "UPDATE Message SET message_text=? WHERE message_id=?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
            //Set the SQL parameters
            preparedStatement.setString(1,updatedtext);
            preparedStatement.setInt(2,messageId);
            //Execute the SQL
            preparedStatement.executeUpdate();
            //Return the results
            ResultSet rs =  preparedStatement.getGeneratedKeys();
            if(rs.next()){
                Message message = getMessage(messageId);
                return message;
            }
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public List<Message> getAllMessagesByUserId(int accountId) {
        Connection connection = ConnectionUtil.getConnection();
        try {
            //create the SQL statement
            String sql = "SELECT * FROM Message WHERE posted_by= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            //Set the SQL parameters
            preparedStatement.setInt(1,accountId);
            //Execute the SQL
            ResultSet pkeyResultSet =  preparedStatement.executeQuery();
            //Return the results
            ArrayList<Message> messageList= new ArrayList<Message>();
            while(pkeyResultSet.next()) {
                int message_id = pkeyResultSet.getInt(1);
                int posted_by = pkeyResultSet.getInt(2);
                String message_text = pkeyResultSet.getString(3);
                long time_posted_epoch = pkeyResultSet.getLong(4);
                Message message = new Message(message_id, posted_by, message_text, time_posted_epoch);
                messageList.add(message);
            }
            return messageList;
        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }
}
