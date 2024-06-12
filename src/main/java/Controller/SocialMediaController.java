package Controller;

import Service.*;
import Model.*;

import io.javalin.Javalin;
import io.javalin.http.Context;


/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;
    
    //constructor
    public SocialMediaController() {
        accountService = new AccountService();
        messageService = new MessageService();
    }
    
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("register", this::registerNewUser);
        app.post("login", this::login);
        app.post("messages", this::processNewMessage);
        app.get("messages", this::getMessages);
        app.get("messages/{message_id}", this::getMessage);
        app.delete("messages/{message_id}", this::deleteMessage);
        app.patch("messages/{message_id}", this::updateMessage);
        app.get("accounts/{account_id}/messages", this::getMessagesFromUser);

        return app;
    }

    /**
     * endpoint /register will attempt to create new user account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void registerNewUser(Context context) 
    {
        //get account object from the request body
        Account account = context.bodyAsClass(Account.class);

        try{
            //attempt to register new user
            account = accountService.processUserRegistration(account.getUsername(), account.getPassword());
        }
        catch (AccountService.AccountException e) {
            //e.getMessage() contains the specific reason the user input was invalid
            //to pass the tests we will return no reasons to user
            context.status(400).result();
            return;
        }

        //check for successful registration
        if (account != null)
        {
            //return newly registered account
            context.json(account);
        }
        else
        {
            //internal error prevented registration
            context.status(500).result("Server Error: Something unexpected occurred.");
        }
    }

    /**
     * endpoint /login will attempt to log user in to their account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     */
    private void login(Context context) {
        //get account from request body
        Account account = context.bodyAsClass(Account.class);

        try{
            //attempt user log in
            account = accountService.login(account.getUsername(), account.getPassword());
        }
        catch (AccountService.AccountException e) {
            //e.getMessage() contains the specific reason the user input was invalid
            //to pass the tests we will return no reasons to user
            context.status(401).result();
            return;
        }

        //check for successful login
        if (account != null)
        {
            //return account
            context.json(account);
        }
        else
        {
            //internal error prevented registration
            context.status(500).result("Server Error: Something unexpected occurred.");
        }
    }

    /**
     * endpoint post /messages will allow the user to post a new message from their account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * 
     * - The creation of the message will be successful if and only if the message_text is not blank, 
     * is not over 255 characters, and posted_by refers to a real, existing user. 
     * If successful, the response body should contain a JSON of the message, including its message_id. 
     * The response status should be 200, which is the default. The new message should be persisted to the database.
     * If the creation of the message is not successful, the response status should be 400. (Client error)
     */
    private void processNewMessage(Context context) {
        //get message from request body
        Message message= context.bodyAsClass(Message.class);

        try{
            //attempt posting message
            message = messageService.postMessage(message);
        }
        catch (MessageService.MessageException e) {
            //e.getMessage() contains the specific reason the user input was invalid
            //to pass the tests we will return no reasons to user
            context.status(400).result();
            return;
        }

        //check for successful login
        if (message != null)
        {
            //return account
            context.json(message);
        }
        else
        {
            //internal error prevented registration
            context.status(500).result("Server Error: Something unexpected occurred.");
        }
    }

    /**
     * endpoint get /messages will allow the user to post a new message from their account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

     * The response body should contain a JSON representation of a list containing all messages 
     * retrieved from the database. It is expected for the list to simply be empty if there are 
     * no messages. The response status should always be 200, which is the default.
     */
    private void getMessages(Context context) {
        //return all messages
        context.json(messageService.getAllMessages());
    }

    /**
     * endpoint get /messages/{message_id} will allow the user to post a new message from their account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/messages.

     * The response body should contain a JSON representation of a list containing all messages 
     * retrieved from the database. It is expected for the list to simply be empty if there are 
     * no messages. The response status should always be 200, which is the default.
     */
    private void getMessage(Context context)
    {
        Message message = messageService.getMessageById(Integer.parseInt(context.pathParam("message_id")));
        if (message != null)
        {
            context.json(message);
        }
        else
        {
            context.json("");
        }
    }

    private void deleteMessage(Context context)
    {
        Message message = messageService.deleteMessage(Integer.parseInt(context.pathParam("message_id")));
        if (message != null)
        {
            context.json(message);
        }
        else
        {
            context.json("");
        }
    }

    /**
     * As a user, I should be able to submit a PATCH request on the endpoint 
     * PATCH localhost:8080/messages/{message_id}. The request body should contain a 
     * new message_text values to replace the message identified by message_id. The request 
     * body can not be guaranteed to contain any other information.

     * The update of a message should be successful if and only if the message id already 
     * exists and the new message_text is not blank and is not over 255 characters. If the 
     * update is successful, the response body should contain the full updated message 
     * (including message_id, posted_by, message_text, and time_posted_epoch), and the 
     * response status should be 200, which is the default. The message existing on the 
     * database should have the updated message_text.
     * If the update of the message is not successful for any reason, the response status 
     * should be 400. (Client error)
     */
    private void updateMessage(Context context)
    {
        Message message= context.bodyAsClass(Message.class);
        System.out.println(messageService.getAllMessages());
        System.out.println(message);

       try{
           //attempt posting message
           message = messageService.updateMessage(message.getMessage_id(), message.getMessage_text());
       }
       catch (MessageService.MessageException e) {
           //e.getMessage() contains the specific reason the user input was invalid
           //to pass the tests we will return no reasons to user
           context.status(400).result();
           System.out.println(e.getMessage());
           return;
       }

       //check for success
       if (message != null)
       {
           //return account
           context.json(message);
       }
       else
       {
           //internal error prevented registration
           context.status(500).result("Server Error: Something unexpected occurred.");
       }
    }

    /**
     * endpoint get /messages/{message_id} will allow the user to post a new message from their account
     * @param context The Javalin Context object manages information about both the HTTP request and response.
     * 
     * As a user, I should be able to submit a GET request on the endpoint GET localhost:8080/accounts/{account_id}/messages.

     * The response body should contain a JSON representation of a list containing all messages posted by a particular user, 
     * which is retrieved from the database. It is expected for the list to simply be empty if there are no messages. 
     * The response status should always be 200, which is the default.
     */
     private void getMessagesFromUser(Context context)
     {
        int accountId = Integer.parseInt(context.pathParam("account_id"));

        context.json(messageService.getMessagesFromUser(accountId));
     }
}