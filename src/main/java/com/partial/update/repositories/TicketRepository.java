package com.partial.update.repositories;


import com.datastax.oss.driver.api.core.DefaultConsistencyLevel;
import com.partial.update.data.Ticket;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Consistency;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface TicketRepository extends CassandraRepository<Ticket, UUID> {
    @Override
    @Consistency(DefaultConsistencyLevel.QUORUM)
    Optional<Ticket> findById(UUID uuid);
}