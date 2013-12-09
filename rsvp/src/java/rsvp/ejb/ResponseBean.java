package rsvp.ejb;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import rsvp.entity.Event;
import rsvp.entity.Person;
import rsvp.entity.Response;
import rsvp.util.ResponseEnum;

@Stateless
@Path("/{eventId}/{inviteId}")
public class ResponseBean {

    @PersistenceContext
    private EntityManager em;
    
    @GET
    @Produces("text/html")
    public String getResponse(@PathParam("eventId") Long eventId,
            @PathParam("inviteId") Long personId) {
        StringBuilder output = new StringBuilder();
        Response response = (Response)
                em.createNamedQuery("rsvp.entity.Response.findResponseByEventAndPerson")
                .setParameter("eventId", eventId)
                .setParameter("personId", personId)
                .getSingleResult();
        Event event = response.getEvent();
        Person person = response.getPerson();
        output.append("<html lang=\"en\"><head><title>Current response for ")
                .append(person.getFirstName())
                .append(" ")
                .append(person.getLastName())
                .append(" at ")
                .append(event.getName())
                .append("</title>")
                .append("<link href=\"/rsvp/css/default.css\" rel=\"stylesheet\" type=\"text/css\" />")
                .append("</head>");
        output.append("<body><h1>")
                .append(event.getName())
                .append("</h1>");
        output.append("<p>Hello ")
                .append(person.getFirstName())
                .append("!</p>");
        output.append("<p>");
        output.append("Your current response is: ")
                .append(response.getResponseText());
        output.append("</p>");

        output.append("<form name=\"submitResponse\" action=\"/rsvp/resources/")
                .append(event.getId())
                .append("/")
                .append(person.getId())
                .append("\" method=\"POST\">");
        output.append("<p>Will you attend ")
                .append(event.getName())
                .append("?</p>");
        output.append("<select title=\"Response\" name=\"attendeeResponse\">");
        output.append("<option>Yes</option>");
        output.append("<option>Maybe</option>");
        output.append("<option>No</option>");
        output.append("</select>");
        output.append("<br/>");
        output.append("<input type=\"submit\" value=\"Submit response\" name=\"attendeeResponseButton\"/>");
        output.append("</form>");

        output.append("</body></html>");

        return output.toString();

    }

    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces("text/html")
    public String putResponse(@FormParam("attendeeResponse") String userResponse,
            @PathParam("eventId") Long eventId,
            @PathParam("inviteId") Long personId) {
        StringBuilder output = new StringBuilder();
        Response response = (Response)
                em.createNamedQuery("rsvp.entity.Response.findResponseByEventAndPerson")
                .setParameter("eventId", eventId)
                .setParameter("personId", personId)
                .getSingleResult();
        Event event = response.getEvent();
        Person person = response.getPerson();
        if (userResponse.equals("Yes") && !response.getResponse().equals(ResponseEnum.ATTENDING)) {
            response.setResponse(ResponseEnum.ATTENDING);
            em.merge(response);
        } else if (userResponse.equals("No") && !response.getResponse().equals(ResponseEnum.NOT_ATTENDING)) {
            response.setResponse(ResponseEnum.NOT_ATTENDING);
            em.merge(response);
        } else if (userResponse.equals("Maybe") && !response.getResponse().equals(ResponseEnum.MAYBE_ATTENDING)) {
            response.setResponse(ResponseEnum.MAYBE_ATTENDING);
            em.merge(response);
        }
        
        // create the response HTML
        output.append("<html lang=\"en\"><head><title>Current response for ")
                .append(person.getFirstName())
                .append(" ")
                .append(person.getLastName())
                .append(" at ")
                .append(event.getName())
                .append("</title>")
                .append("<link href=\"/rsvp/css/default.css\" rel=\"stylesheet\" type=\"text/css\" />")
                .append("</head>");
        output.append("<body><h1>")
                .append(event.getName())
                .append("</h1>");
        output.append("<p>Thanks for your RSVP, ")
                .append(person.getFirstName())
                .append(" ")
                .append(person.getLastName())
                .append("!</p>");
        output.append("<p>Your current response is: ")
                .append(response.getResponseText())
                .append("</p>");
        output.append("<p><a href=\"/rsvp/resources/status/")
                .append(event.getId())
                .append("\">Back to event page</a></p>");
        output.append("</body></html>");
        return output.toString();
    }
 
}
