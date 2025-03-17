package cn.abner.aschat.repository;

import cn.abner.aschat.entity.ContactMultiKey;
import cn.abner.aschat.entity.MessageContact;
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
public interface MessageContactRepository extends JpaRepository<MessageContact, ContactMultiKey> {
    List<MessageContact> findByOwnerUidOrderByMidDesc(Long ownerUid);
}
