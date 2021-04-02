package edu.towson.cosc457.CarDealership.model;

import com.sun.istack.NotNull;
import edu.towson.cosc457.CarDealership.model.dto.CommentDto;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "Comment")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id",
            updatable = false)
    private Long id;
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "ticket_id")
    private ServiceTicket serviceTicket;
    @NotNull
    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH})
    @JoinColumn(name = "mechanic_id")
    private Mechanic mechanic;
    @NotNull
    @Column(name = "date_created")
    private LocalDate dateCreated;
    @NotNull
    @Column(name = "content")
    private String content;

    public Comment() { }

    public Comment(ServiceTicket serviceTicket, Mechanic mechanic, LocalDate dateCreated, String content) {
        this.serviceTicket = serviceTicket;
        this.mechanic = mechanic;
        this.dateCreated = dateCreated;
        this.content = content;
    }

    public static Comment from (CommentDto commentDto) {
        Comment comment = new Comment();
        comment.setId(commentDto.getId());
        comment.setServiceTicket(commentDto.getServiceTicket());
        comment.setMechanic(commentDto.getMechanic());
        comment.setDateCreated(commentDto.getDateCreated());
        comment.setContent(commentDto.getContent());
        return comment;
    }
}