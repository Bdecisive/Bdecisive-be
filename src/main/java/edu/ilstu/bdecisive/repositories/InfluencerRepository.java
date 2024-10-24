package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.Influencer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfluencerRepository extends JpaRepository<Influencer, Long> {
}