package cn.abner.aschat.repository;

import cn.abner.aschat.entity.MessageRelation;
import cn.abner.aschat.entity.RelationMultiKey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Description for this class
 *
 * <p>
 *
 * @author: Abner Song
 * <p>
 * @date: 2025/3/17
 */
@Repository
public interface MessageRelationRepository extends JpaRepository<MessageRelation, RelationMultiKey> {
    List<MessageRelation> findAllByOwnerUidAndOtherUidOrderByMidAsc(Long ownerUid, Long otherUid);

    List<MessageRelation> findAllByOwnerUidAndOtherUidAndMidIsGreaterThanOrderByMidAsc(Long ownerUid, Long otherUid, Long lastMid);
}
