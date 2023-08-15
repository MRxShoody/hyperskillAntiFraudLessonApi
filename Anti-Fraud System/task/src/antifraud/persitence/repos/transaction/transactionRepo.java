package antifraud.persitence.repos.transaction;

import antifraud.models.enums.regions;
import antifraud.persitence.models.transaction.transaction;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface transactionRepo extends CrudRepository<transaction,Long>,
                                         PagingAndSortingRepository<transaction,Long> {

    //region
    int countDistinctByRegionIsNotAndNumberAndTransactionDateBetween(regions r, String number, LocalDateTime t1, LocalDateTime t2);

    //ip
    @Query("SELECT count(distinct(t.ip)) FROM transaction t WHERE t.number = :number AND t.transactionDate BETWEEN :t1 AND :t2")
    int checkIP(@Param("t1") LocalDateTime t1, @Param("t2") LocalDateTime t2, @Param("number") String number);

    List<transaction> findAllByNumber(String number);


    //@Query("SELECT count(distinct(t.ip)) FROM transaction t WHERE t.user.id = ?3 AND t.transactionDate BETWEEN ?1 AND ?2")
    //int checkIPtest1(LocalDateTime t1, LocalDateTime t2, long id);

    //@Query(value = "SELECT count(distinct(t.ip)) FROM TRANSACTION t WHERE t.USER_ID = ?3 AND t.TRANSACTION_DATE BETWEEN ?1 AND ?2",nativeQuery = true)
    //int checkIPtest2( LocalDateTime t1,  LocalDateTime t2, long id);

}
