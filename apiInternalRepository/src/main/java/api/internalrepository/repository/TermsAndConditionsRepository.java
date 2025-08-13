package api.internalrepository.repository;

import api.internalrepository.entity.TermsAndConditionsEntity;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TermsAndConditionsRepository extends JpaRepository<TermsAndConditionsEntity, Long> {

    @Query("select t from TermsAndConditionsEntity t " +
            "where t.fileName like :termsOrPolicies and t.status is true " +
            "order by t.registerDate desc")
    List<TermsAndConditionsEntity> findAllOrderByRegisterDateDesc(
            @Param("termsOrPolicies") final String termsOrPolicies);

    @Modifying
    @Transactional
    @Query("update TermsAndConditionsEntity t set t.status = false " +
            "where t.idTermsAndConditions = :idTermsAndConditions")
    void updateStatusToFalse(@Param("idTermsAndConditions") Long idTermsAndConditions);

    @Query(value = "select tc from TermsAndConditionsEntity as tc " +
            "where tc.status is true order by tc.registerDate desc")
    List<TermsAndConditionsEntity> getLastTermsAndConditions();

}
