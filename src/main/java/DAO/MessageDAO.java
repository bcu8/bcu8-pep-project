package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//Class for managing operations related to the Message table of our database
public class MessageDAO {

    //support method to make object from result
    private Message resultToMessage(ResultSet result) throws SQLException
    {
        return new Message(
        result.getInt("message_id"),
        result.getInt("posted_by"),
        result.getString("message_text"),
        result.getLong("time_posted_epoch"));
    }
    
    //returns contents of message table
    public List<Message> getAllMessages() 
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        //declare empty list for returning messages
        List<Message> messages = new ArrayList<>();

        try 
        {
            //create and execute prepared statement
            String sql = "SELECT * FROM Message;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            //loop through results
            while(rs.next())
            {
                messages.add(resultToMessage(rs));
            }
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        //return results (empty if fail)
        return messages;
    }

    //get message by id, return message obj or null if fail
    public Message getMessageById(int id)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        try 
        {
            //create and execute prepared statement
            String sql = "SELECT * FROM Message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            //return result if found
            if(rs.next()) return resultToMessage(rs);
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        
        //return null if exception or id wasnt found
        return null;
    }

    //insert new message into Message table. Returns the message inserted into db
    public Message insertMessage(Message message) 
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create "insert" prepared statement
            String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?,?,?);";
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            //configure prepared statement parameters
            ps.setInt(1, message.getPosted_by());
            ps.setString(2, message.getMessage_text());
            ps.setLong(3, message.getTime_posted_epoch());

            //update database
            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) throw new SQLException("Couldnt insert message " + message +" into table.");
            
            try {
                //get the row we just inserted
                ResultSet rs = ps.getGeneratedKeys();
            
                //get id of message and set it in the message object to be returned
                if (rs.next()) {
                int generatedId = rs.getInt(1);
                message.setMessage_id(generatedId);
                return message;
                }
                else {
                    throw new SQLException("Could not get id from newly created message");
                }
            }
            catch(SQLException e) {
                System.out.println(e.getMessage());
            }
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //return result
        return null;
    }

    //deletes message with given id from messages table
    public Message deleteMessage(int id)
    {
        Message deletedMessage = null;
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create and execute prepared statement to select the message before deletion
            String selectSql = "SELECT * FROM Message WHERE message_id = ?";
            PreparedStatement selectPs = connection.prepareStatement(selectSql);
            selectPs.setInt(1, id);
            ResultSet rs = selectPs.executeQuery();

            if (rs.next()) {
                deletedMessage = resultToMessage(rs);
            }
            else
            {
                return null;
            }
            
            //create and execute prepared statement
            String sql = "DELETE FROM Message WHERE message_id = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1,id);
            ps.executeUpdate();
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //return result
        return deletedMessage;
    }

    public Message updateMessage(int id, String updateText) {
        // Get connection
        Connection connection = ConnectionUtil.getConnection();
    
        try {
            // Create and execute prepared statement to update the message text
            String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setString(1, updateText);
            ps.setInt(2, id);
            ps.executeUpdate();
    
            return getMessageById(id);
        } catch (SQLException e) {
            // Handle exception if it occurs
            System.out.println(e.getMessage());
        }
    
        // Return null if update fails or message not found
        return null;
    }

    //returns list of messages written by specified user
    public List<Message> getMessagesFromUser(int account_id)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();

        //declare empty list for returning messages
        List<Message> messages = new ArrayList<>();

        try 
        {
            //create and execute prepared statement
            String sql = "SELECT * FROM Message WHERE posted_by = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);
            ps.setInt(1, account_id);
            ResultSet rs = ps.executeQuery();

            //loop through results
            while(rs.next())
            {
                messages.add(resultToMessage(rs));
            }
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
            
        //return results (empty if fail)
        return messages;
    }
}
