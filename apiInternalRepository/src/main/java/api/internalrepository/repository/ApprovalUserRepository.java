package api.internalrepository.repository;

import api.internalrepository.entity.ApprovalUserEntity;
import api.internalrepository.to.ApprovalUserTo;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface ApprovalUserRepository extends JpaRepository<ApprovalUserEntity, Long> {

    @Query("SELECT new api.internalrepository.to.ApprovalUserTo(au.approvalStatus, tc.urlFile, tc.fileName) " +
            "FROM ApprovalUserEntity au " +
            "INNER JOIN au.termsAndConditionsEntity tc " +
            "INNER JOIN au.userEntity user " +
            "WHERE user.idUser = :idUser AND au.termsAndConditionsEntity.idTermsAndConditions IN (" +
            "SELECT tce.idTermsAndConditions FROM TermsAndConditionsEntity tce " +
            "WHERE tce.fileName IN (:termsAndPolicies) AND tce.status = true)")
    List<ApprovalUserTo> getApprovalUserEntityByIdUser(@Param("idUser") Long idUser,
                           @Param("termsAndPolicies") List<String> termsAndPolicies);

    @Modifying
    @Transactional
    @Query("update ApprovalUserEntity as au set au.approvalStatus = true, au.approvalDate = :approvedDate " +
            "where au.userEntity.idUser = :idUser and au.termsAndConditionsEntity.idTermsAndConditions = ( " +
            "select tce.idTermsAndConditions from TermsAndConditionsEntity as tce " +
            "where tce.fileName like :termsOrPolicies and tce.status is true)")
    void updateStatusApprovalUser(@Param("idUser") final Long idUser,
                                  @Param("termsOrPolicies") final String termsOrPolicies,
                                  @Param("approvedDate") final OffsetDateTime approvedDate);

}
