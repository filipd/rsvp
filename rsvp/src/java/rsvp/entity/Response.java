package rsvp.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import rsvp.util.ResponseEnum;

@NamedQuery(name="rsvp.entity.Response.findResponseByEventAndPerson",
            query="SELECT r " + 
            "FROM Response r " +
            "JOIN r.event e " +
            "JOIN r.person p " +
            "WHERE e.id = :eventId AND p.id = :personId")
@Entity
public class Response implements Serializable {
    private static final long serialVersionUID = -8188690304868834266L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Event event;
    @ManyToOne
    private Person person;
    @Enumerated(EnumType.STRING)
    private ResponseEnum response;

    public Response() {
        this.setResponse(ResponseEnum.NOT_RESPONDED);
    }

    public Response(Event event, Person person, ResponseEnum response) {
        this.setEvent(event);
        this.setPerson(person);
        this.setResponse(response);
    }

    public Response(Event event, Person person) {
        this.setEvent(event);
        this.setPerson(person);
        this.setResponse(ResponseEnum.NOT_RESPONDED);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Response)) {
            return false;
        }
        Response other = (Response) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "rsvp.entity.Response[id=" + id + "]";
    }

    /**
     * @return the event
     */
    public Event getEvent() {
        return event;
    }

    /**
     * @param event the event to set
     */
    public void setEvent(Event event) {
        this.event = event;
    }

    /**
     * @return the person
     */
    public Person getPerson() {
        return person;
    }

    /**
     * @param person the person to set
     */
    public void setPerson(Person person) {
        this.person = person;
    }

    /**
     * @return the response
     */
    public ResponseEnum getResponse() {
        return response;
    }

    /**
     * @param response the response to set
     */
    public void setResponse(ResponseEnum response) {
        this.response = response;
    }

    public String getResponseText() {
        String responseText;
        if (this.response == ResponseEnum.ATTENDING) {
            responseText = "Attending";
        } else if (this.response == ResponseEnum.MAYBE_ATTENDING) {
            responseText = "Maybe attending";
        } else if (this.response == ResponseEnum.NOT_ATTENDING) {
            responseText = "Not attending";
        } else if (this.response == ResponseEnum.NOT_RESPONDED) {
            responseText = "Hasn't responded yet";
        } else {
            responseText = "unknown";
        }
        return responseText;
    }
}
