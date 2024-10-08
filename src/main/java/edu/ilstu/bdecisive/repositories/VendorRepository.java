package edu.ilstu.bdecisive.repositories;

import edu.ilstu.bdecisive.models.User;
import edu.ilstu.bdecisive.models.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository  extends JpaRepository<Vendor, Long> {
}
