package com.partial.update.dto;

import com.partial.update.data.User;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.UUID;

@Getter
@Setter

public class TicketPriorityRequest {
    @Schema(hidden = true)
    private UUID id;
    private User modifiedBy;
    private String priority;
}
