package Controller;

import io.javalin.Javalin;
import io.javalin.http.Context;

import Model.Account;
import Model.Message;
import Service.AccountService;
import Service.MessageService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static java.lang.Integer.parseInt;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
public class SocialMediaController {
    MessageService messageService;
    /**
     * In order for the test cases to work, you will need to write the endpoints in the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * @return a Javalin app object which defines the behavior of the Javalin controller.
     */
    public SocialMediaController(){
        this.messageService = new MessageService();

    }
    public Javalin startAPI() {
        Javalin app = Javalin.create();

//        app.get("/messages", this::getAllMessagesHandler);
        app.post("/messages", this::postMessageHandler);
        app.delete("/messages/<id>", this::deleteMessageHandler);
//        app.get("/accounts", this::getAllAccountsHandler);
//        app.post("/accounts", this::postAccountHandler);
//        app.get("/books/available", this::getAvailableBooksHandler);

        return app;
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


        Message addedMessage = messageService.addMessage(message);

        if(addedMessage!=null){
            ctx.json(mapper.writeValueAsString(addedMessage));
            System.out.println(mapper.writeValueAsString(addedMessage));
            System.out.println("We were here");

        }else{
            ctx.status(400);
            System.out.println("hello");
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