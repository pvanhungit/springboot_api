package com.awesome.domain;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "post")
@TypeDefs({
        @TypeDef(
                name = "string-array",
                typeClass = StringArrayType.class
        )
})
public class Post implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(name = "author_id")
    private Long authorId;

    @Length(max = 255)
    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @Type(
            type = "string-array"
    )
    @Column(name = "img",
            columnDefinition = "text[]"
    )
    private String[] img;

    @Type(
            type = "string-array"
    )
    @Column(name = "tags",
            columnDefinition = "text[]"
    )
    private String[] tags;

    @Column( name = "category_id" )
    private Long categoryId;

    @Column( name = "up_vote" )
    private int upVote;

    @Column( name = "down_vote" )
    private int downVote;

    @Column( name = "updated_by" )
    private Integer updatedBy;

    @Column(name = "created_at" , updatable = false, insertable = false)
    private Timestamp createdAt;

    @Column(name = "created_at" ,insertable = false)
    private Timestamp updated_at;

}