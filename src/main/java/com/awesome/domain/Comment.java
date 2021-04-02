package com.awesome.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Max;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "comment")
public class Comment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column( name = "comment_id" )
    private long id;

    @Column( name = "post_id" )
    private long postId;

    @Column( name = "content" )
    private String content;

    @Column( name = "user_id" )
    private long userId;

    @Column(name = "created_at" , updatable = false, insertable = false)
    private Timestamp createdAt;

    @Column(name = "updated_at" ,insertable = false)
    private Timestamp updatedAt;

}