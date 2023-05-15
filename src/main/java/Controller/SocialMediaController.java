package Controller;

import com.fasterxml.jackson.core.type.TypeReference;
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

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
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

    private void loginHandler(Context ctx) throws JsonProcessingException{
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

        System.out.println("Yoo we are eherere3r");
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

    private void createAccountHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.readValue(ctx.body(), Account.class);

System.out.println("Yoo we are eherere3r");
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
                    System.out.println("hello");
                }
            }
        }

    }

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

    private void updateMessageByIdHandler(Context ctx) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        int message_id = parseInt(ctx.pathParam("id"));
        Message message = mapper.readValue(ctx.body(), Message.class);
        String updatedtext = message.message_text;
        System.out.println(updatedtext.length());
        if(updatedtext.length()>=255||updatedtext.length()==0){
            ctx.json("");
            System.out.println("hey way too long!");
            ctx.status(400);
        } else {


            Message foundMessage = messageService.getMessage(message_id);

            System.out.println(message);
            if (foundMessage != null) {
                Message updatedMessage = messageService.updateMessage(message_id, updatedtext);
                System.out.println(foundMessage.time_posted_epoch);
                if (updatedMessage != null) {
                    System.out.println("Message Deleted");
                    ctx.json(mapper.writeValueAsString(updatedMessage));
                    System.out.println();
                    System.out.println("We were here");

                } else {
                    ctx.status(400);
                    System.out.println("hello");
                }

            } else {
                ctx.json("");
                ctx.status(400);

            }
        }
    }

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

    private void getAllMessagesHandler(Context ctx) throws JsonProcessingException{

        ObjectMapper mapper = new ObjectMapper();
       // List<Message> messages = objectMapper.readValue(response.body().toString(), new TypeReference<List<Message>>(){});
        List<Message> foundMessages = messageService.getAllMessages();

        if(foundMessages!=null){
            System.out.println(foundMessages.size());
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
                System.out.println(mapper.writeValueAsString(addedMessage));
                System.out.println("We were here");

            } else {
                ctx.status(400);
                System.out.println("hello");
            }
        }
        }
    private void deleteMessageHandler(Context ctx) throws JsonProcessingException {
       int message_id = parseInt(ctx.pathParam("id"));
        ObjectMapper mapper = new ObjectMapper();
        Message foundMessage = messageService.getMessage(message_id);

            if(foundMessage!=null){
                Message deletedMessage = messageService.deleteMessage(message_id);
                System.out.println(foundMessage.time_posted_epoch);
                if(deletedMessage!=null){
                    System.out.println("Message Deleted");
                    ctx.json(mapper.writeValueAsString(foundMessage));
                    System.out.println();
                    System.out.println("We were here");

                }else{
                    ctx.status(400);
                    System.out.println("hello");
                }

            } else {
                ctx.json("");
            }

    }


}