package DAO;

import Model.Account;
import Util.ConnectionUtil;

import java.sql.*;

public class AccountDAO {
    public boolean insertAccount(String username, String password)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create "insert" prepared statement
            String sql = "INSERT INTO Message (username, password) VALUES (?,?);";
            PreparedStatement ps = connection.prepareStatement(sql);

            //configure prepared statement parameters
            ps.setString(1, username);
            ps.setString(2, password);

            //update database
            ps.executeUpdate();

            //return success
            return true;
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //return fail
        return false;
    }

    //checks if an account exists with given username and password
    public boolean checkForAccount(String username, String password)
    {
        //get connection
        Connection connection = ConnectionUtil.getConnection();
        try 
        {
            //create prepared statement
            String sql = "SELECT * FROM Account WHERE username = ? AND password = ?;";
            PreparedStatement ps = connection.prepareStatement(sql);

            //configure prepared statement parameters
            ps.setString(1, username);
            ps.setString(2, password);

            //get results
            ResultSet rs = ps.executeQuery();

            //return true if account found, otherwise false
            return rs.next();
        }
        //handle exception if it occurs
        catch(SQLException e)
        {
            System.out.println(e.getMessage());
        }
        //return fail
        return false;
    }
}
