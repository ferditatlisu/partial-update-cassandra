package com.partial.update.data;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.Column;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

@Data
@Builder
@Table("tickets")
@AllArgsConstructor
@NoArgsConstructor
public class Ticket implements Serializable {
    @PrimaryKey()
    private UUID id;

    @Column()
    private String title;

    @Column
    private String priority;

    @Column
    private Flow flow;

    @Column
    private User modifiedBy;

    @Column
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Date modifiedDate;

    public void updateModified(User user){
        modifiedBy = user;
        modifiedDate = new Date();
    }
}
