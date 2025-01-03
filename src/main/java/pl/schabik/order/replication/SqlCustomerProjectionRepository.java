package pl.schabik.order.replication;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

interface SqlCustomerProjectionRepository extends CustomerProjectionRepository, JpaRepository<CustomerProjection, UUID> {
}