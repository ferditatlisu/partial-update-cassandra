package com.partial.update.builders;

import com.datastax.oss.driver.api.core.ConsistencyLevel;
import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.querybuilder.update.OngoingAssignment;
import com.datastax.oss.driver.api.querybuilder.update.UpdateWithAssignments;
import com.datastax.oss.driver.internal.core.data.DefaultUdtValue;
import com.partial.update.data.Ticket;
import org.springframework.data.cassandra.core.CassandraTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.literal;
import static com.datastax.oss.driver.api.querybuilder.QueryBuilder.update;

public class TicketDbQuery {
    private final CassandraTemplate cassandraTemplate;
    private static final String tableName = "tickets";

    private OngoingAssignment currentQuery;
    private Map<CqlIdentifier, Object> udtObjects;
    private TicketDbQueryBuilder builder;
    private UUID ticketId;

    public TicketDbQuery(CassandraTemplate cassandraTemplate){
        this.cassandraTemplate = cassandraTemplate;
    }

    public TicketDbQueryBuilder createQuery(Ticket ticket){
        ticketId = ticket.getId();
        udtObjects = new LinkedHashMap<>();
        var persistentEntity = cassandraTemplate.getConverter().getMappingContext().getPersistentEntity(Ticket.class);
        cassandraTemplate.getConverter().write(ticket, udtObjects, Objects.requireNonNull(persistentEntity));
        currentQuery = update(tableName);

        builder = new TicketDbQueryBuilder();
        builder
                .modifiedBy()
                .modifiedDate();

        return builder;
    }

    public class TicketDbQueryBuilder {

        public TicketDbQueryBuilder priority() {
            final String columnName = "priority";
            addColumnToCurrentQuery(columnName);

            return this;
        }

        public TicketDbQueryBuilder flowName() {
            final String columnName = "flow";
            final String fieldName = "name";
            addFieldToCurrentQuery(columnName, fieldName);

            return this;
        }

        public boolean execute(ConsistencyLevel consistencyLevel, boolean queryToLog){
            var simpleStatement = whereById(consistencyLevel);
            if(queryToLog)
                System.out.print(simpleStatement.getQuery());

            var executed = cassandraTemplate.getCqlOperations().execute(simpleStatement);

            return executed;
        }

        private TicketDbQueryBuilder modifiedBy(){
            final String columnName = "modifiedby";
            addColumnToCurrentQuery(columnName);

            return this;
        }

        private TicketDbQueryBuilder modifiedDate(){
            final String columnName = "modifieddate";
            addColumnToCurrentQuery(columnName);

            return this;
        }

        private SimpleStatement whereById(ConsistencyLevel consistencyLevel){
            final String columnName = "id";
            return ((UpdateWithAssignments)currentQuery)
                    .whereColumn(columnName).isEqualTo(literal(ticketId))
                    .builder()
                    .setConsistencyLevel(consistencyLevel)
                    .build();
        }

        private void addColumnToCurrentQuery(String columnName){
            var value = udtObjects.get(CqlIdentifier.fromInternal(columnName));
            currentQuery = currentQuery.setColumn(columnName, literal(value));
        }

        private void addFieldToCurrentQuery(String columnName, String fieldName){
            var value = udtObjects.get(CqlIdentifier.fromInternal(columnName));
            var fieldValue = ((DefaultUdtValue) value).getObject(CqlIdentifier.fromInternal(fieldName));
            currentQuery = currentQuery.setField(CqlIdentifier.fromInternal(columnName), CqlIdentifier.fromInternal(fieldName), literal(fieldValue));
        }
    }
}