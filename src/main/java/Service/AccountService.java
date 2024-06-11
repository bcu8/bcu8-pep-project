package Service;

import DAO.AccountDAO;
import Model.Account;

public class AccountService {
    private AccountDAO accountDAO;

    //Constructor
    public AccountService() 
    {
        //initialize our DAO
        accountDAO = new AccountDAO();
    }

    //operation for creating new user account
    public boolean processRegistration(String username, String password)
    {
        //insert account using dao
        return accountDAO.insertAccount(username, password);
    }

    //operation for a user attempted sign in
    public boolean logIn(String username, String password)
    {
        //check if provided credentials are correct using dao
        return accountDAO.checkForAccount(username, password);
    }
}