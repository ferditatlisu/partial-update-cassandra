package com.partial.update.controllers;

import com.partial.update.data.Ticket;
import com.partial.update.dto.TicketFlowNameRequest;
import com.partial.update.dto.TicketPriorityRequest;
import com.partial.update.services.TicketService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/tickets")
@AllArgsConstructor
public class TicketController {
    private final TicketService ticketService;

    @GetMapping("{id}")
    public ResponseEntity<Ticket> get(@PathVariable UUID id) {
        var ticket =  ticketService.get(id);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("{id}/priority")
    public ResponseEntity<Ticket> get(@PathVariable UUID id, @RequestBody TicketPriorityRequest request) {
        request.setId(id);
        var ticket =  ticketService.updatePriority(request);
        return ResponseEntity.ok(ticket);
    }

    @PutMapping("{id}/flow-name")
    public ResponseEntity<Ticket> get(@PathVariable UUID id, @RequestBody TicketFlowNameRequest request) {
        request.setId(id);
        var ticket =  ticketService.updateFlowName(request);
        return ResponseEntity.ok(ticket);
    }
}
