package com.untacstore.modules.review;

import com.untacstore.modules.store.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public interface ReviewRepository extends JpaRepository<Review, Long> {

    @EntityGraph(attributePaths = {"replies"})
    List<Review> findReviewWithReplyByStoreOrderByReviewAt(Store store);
}
