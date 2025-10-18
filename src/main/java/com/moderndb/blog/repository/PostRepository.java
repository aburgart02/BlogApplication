package com.moderndb.blog.repository;

import com.moderndb.blog.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String titleKeyword, String contentKeyword);

    List<Post> findByTags_Name(String tagName);
}