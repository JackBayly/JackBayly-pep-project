package Controller;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import static java.lang.Integer.parseInt;

public class SocialMediaController {
    MessageService messageService;
    AccountService accountService;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public SocialMediaController(){
        this.messageService = new MessageService();
        this.accountService = new AccountService();
    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.get("/messages", this::getAllMessagesHandler);
        app.get("/messages/<id>", this::getMessageByIdHandler);
        app.patch("/messages/<id>", this::updateMessageByIdHandler);
        app.post("/messages", this::postMessageHandler);
        app.delete("/messages/<id>", this::deleteMessageHandler);
        app.get("/accounts/<id>/messages", this::getAllMessagesFromAccountHandler);
        app.post("/register", this::createAccountHandler);
        app.post("/login", this::loginHandler);
        return app;
    }
    /**
     * Handles logins. We expect a username and password.
     * Will return a 400 status if either username or password are empty.
     * Will return a 401 status if the user is not authenticated.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        if(account.username.length() ==0||account.password.length()<=4){
            ctx.json("");
            ctx.status(400);
        } else {
            Account foundAccount = accountService.login(account);
            if(foundAccount!=null){
                ctx.json(mapper.writeValueAsString(foundAccount));
            } else {
                ctx.json("");
                ctx.status(401);
            }
        }
    }

    /**
     * Handles user registration. We expect a username and password.
     * Will return a 400 status if either username or password are empty.
     * Will return a 400 status if the user already exists.
     * Will return an account object if the account is successfully created.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void createAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);
        if(account.username.length() ==0||account.password.length()<=4){
            ctx.json("");
            ctx.status(400);
        } else {
            Account foundAccount = accountService.getAccountByUsername(account.username);
            if(foundAccount!=null){
                ctx.json("");
                ctx.status(400);
            } else {
                Account addedAccount = accountService.addAccount(account);
                if (addedAccount != null) {
                    ctx.json(mapper.writeValueAsString(addedAccount));
                } else {
                    ctx.status(400);
                }
            }
        }

    }

    /**
     * This handler gets all messages for a specific account.
     * We expect an account id as input.
     * Returns a list of all messages found.
     * Will return an empty list if there are no messages found.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesFromAccountHandler(Context ctx) throws JsonProcessingException {
        int userId = parseInt(ctx.pathParam("id"));
        ObjectMapper mapper = new ObjectMapper();
        Account foundAccount = accountService.getAccount(userId);
        List<Message> foundMessages= new ArrayList<Message>();
        if(foundAccount!=null){
            foundMessages = messageService.getAllMessagesByUserId(foundAccount.account_id);
            ctx.json(mapper.writeValueAsString(foundMessages));
        } else {
            ctx.json(mapper.writeValueAsString(foundMessages));
        }
    }

    /**
     * Handles updating a message by its id.
     * We expect a message id and an updated message text.
     * Will return a 400 status if the message is empty or has a length of 255 characters or more.
     * Will return a 400 status if the message is not found.
     * Will return the updated message object if it was successfully updated.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = parseInt(ctx.pathParam("id"));
        Message message = mapper.readValue(ctx.body(), Message.class);
        String updatedtext = message.message_text;
        if(updatedtext.length()>=255||updatedtext.length()==0){
            ctx.json("");
            ctx.status(400);
        } else {
            Message foundMessage = messageService.getMessage(message_id);
            if (foundMessage != null) {
                Message updatedMessage = messageService.updateMessage(message_id, updatedtext);
                if (updatedMessage != null) {
                    ctx.json(mapper.writeValueAsString(updatedMessage));
                } else {
                    ctx.status(400);
                }
            } else {
                ctx.json("");
                ctx.status(400);
            }
        }
    }

    /**
     * Handles retrieving a specific message given an id.
     * We expect a message id.
     * Will return an empty message object if the message is not found.
     * Will return a message object if it is found.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getMessageByIdHandler(Context ctx) throws JsonProcessingException {
        int message_id = parseInt(ctx.pathParam("id"));
        ObjectMapper mapper = new ObjectMapper();
        Message foundMessage = messageService.getMessage(message_id);
        if(foundMessage!=null){
            ctx.json(mapper.writeValueAsString(foundMessage));
        } else {
            ctx.json("");
        }
    }

    /**
     * Handles getting all messages.
     * Returns a list of all messages.
     * If there are no messages it will return an empty string.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        List<Message> foundMessages = messageService.getAllMessages();
        if(foundMessages!=null){
            ctx.json(mapper.writeValueAsString(foundMessages));
        } else {
            ctx.json("");
        }
    }

    /**
     * Handler to post a new message.
     * The Jackson ObjectMapper will automatically convert the JSON of the POST request into an message object.
     * If messageService returns a null message (meaning posting an message was unsuccessful), the API will return a 400
     * message (client error). There is no need to change anything in this method.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     *            be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void postMessageHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Message message = mapper.readValue(ctx.body(), Message.class);
        if(message.message_text.length()>254||message.message_text.length()==0){
            ctx.json("");
            ctx.status(400);
        } else {
            Message addedMessage = messageService.addMessage(message);
            if (addedMessage != null) {
                ctx.json(mapper.writeValueAsString(addedMessage));
            } else {
                ctx.status(400);
            }
        }
        }

    /**
     * Handles deleting a message given an id.
     * We expect a message id.
     * Will return a message object corresponding to the deleted message
     * If the message was not found we will return an empty object.
     * If the message wasn't deleted we will return a 400 status.
     * @param ctx the context object handles information HTTP requests and generates responses within Javalin. It will
     * be available to this method automatically thanks to the app.post method.
     * @throws JsonProcessingException will be thrown if there is an issue converting JSON into an object.
     */
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
        int message_id = parseInt(ctx.pathParam("id"));
        ObjectMapper mapper = new ObjectMapper();
        Message foundMessage = messageService.getMessage(message_id);
            if(foundMessage!=null){
                Message deletedMessage = messageService.deleteMessage(message_id);
                if(deletedMessage!=null){
                    ctx.json(mapper.writeValueAsString(foundMessage));
                }else{
                    ctx.status(400);
                }
            } else {
                ctx.json("");
            }
    }
}