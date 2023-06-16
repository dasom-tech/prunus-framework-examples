package persistence.jpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import persistence.jpa.entity.Laptop;

import java.util.List;

public interface JpaRepository extends org.springframework.data.jpa.repository.JpaRepository<Laptop, Long> {

    List<Laptop> findAllByVendorAndDeletedIsFalse(String vendor);

    Page<Laptop> findAllByVendorAndDeletedIsFalse(String vendor, Pageable pageable);
}
