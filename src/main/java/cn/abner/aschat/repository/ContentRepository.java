package cn.abner.aschat.repository;

import cn.abner.aschat.entity.MessageContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

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
public interface ContentRepository extends JpaRepository<MessageContent, Long> {
}
