package edu.ilstu.bdecisive.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "followers")
public class Follower extends User {

    public String addComment(String comment) {
        return "Comment added: " + comment;
    }
}
