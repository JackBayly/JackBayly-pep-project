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
            //Write SQL logic here
            String sql = "INSERT INTO Message(posted_by, message_text, time_posted_epoch) VALUES (?,?,?)";
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1,message.posted_by);
            preparedStatement.setString(2,message.message_text);
            preparedStatement.setLong(3,message.time_posted_epoch);

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
                int generated_message_id = (int) pkeyResultSet.getInt(1);
               System.out.println(generated_message_id);

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
            //Write SQL logic here
            String sql = "DELETE FROM Message WHERE message_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1,id);

           int rows = preparedStatement.executeUpdate();
             System.out.println("Looking for message id");


            if(rows>0){
                System.out.println("Deleted the rows");



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
            //Write SQL logic here
            String sql = "SELECT posted_by,message_text, time_posted_epoch FROM Message WHERE message_id= ?";
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            //write preparedStatement's setString and setInt methods here.
            preparedStatement.setInt(1,id);

            ResultSet pkeyResultSet =  preparedStatement.executeQuery();

            if(pkeyResultSet.next()) {
                System.out.println("Helloooo Person");
                int posted_by = pkeyResultSet.getInt(1);

                String message_text = pkeyResultSet.getString(2);

                long time_posted_epoch = pkeyResultSet.getLong(3);
                System.out.println(message_text);
                return new Message(id, posted_by, message_text, time_posted_epoch);
            }
//            pkeyResultSet.close();
//            preparedStatement.close();


            //int generated_message_id2 = (int) pkeyResultSet.getInt(0);
            //System.out.println(generated_message_id2);

//            if(pkeyResultSet.next()){
//                System.out.println("HIt the next");
//                int generated_message_id = (int) pkeyResultSet.getInt(1);
//                System.out.println(generated_message_id);
//
//                return new Message(generated_message_id, message.getPosted_by(),message.getMessage_text(),message.getTime_posted_epoch());
//            }

        }catch(SQLException e){
            System.out.println("We got a sql error");
            System.out.println(e.getMessage());
        }
        return null;
    }
}
