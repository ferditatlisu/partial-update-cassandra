package com.partial.update.services;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.partial.update.builders.TicketDbQuery;
import com.partial.update.data.Ticket;
import com.partial.update.dto.TicketFlowNameRequest;
import com.partial.update.dto.TicketPriorityRequest;
import com.partial.update.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {
    private final CassandraTemplate cassandraTemplate;
    private final TicketRepository ticketRepository;

    public Ticket get(UUID id)  {
        return ticketRepository.findById(id).orElse(null);
    }

    public Ticket updatePriority(TicketPriorityRequest request){
        var ticket = ticketRepository.findById(request.getId()).orElse(null);
        ticket.setPriority(request.getPriority());
        ticket.updateModified(request.getModifiedBy());

        TicketDbQuery ticketDbQuery = new TicketDbQuery(cassandraTemplate);
        ticketDbQuery
                .createQuery(ticket)
                .priority()
                .execute(ConsistencyLevel.QUORUM, true);

        return ticket;
    }

    public Ticket updateFlowName(TicketFlowNameRequest request){
        var ticket = ticketRepository.findById(request.getId()).orElse(null);
        ticket.getFlow().setName(request.getFlowName());
        ticket.updateModified(request.getModifiedBy());

        TicketDbQuery ticketDbQuery = new TicketDbQuery(cassandraTemplate);
        ticketDbQuery
                .createQuery(ticket)
                .flowName()
                .execute(ConsistencyLevel.QUORUM, true);

        return ticket;
    }
}
