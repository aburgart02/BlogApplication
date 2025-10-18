package com.moderndb.blog.service;

import com.moderndb.blog.model.Comment;
import com.moderndb.blog.model.Post;
import com.moderndb.blog.model.Tag;
import com.moderndb.blog.model.User;
import com.moderndb.blog.repository.CommentRepository;
import com.moderndb.blog.repository.PostRepository;
import com.moderndb.blog.repository.TagRepository;
import com.moderndb.blog.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class BlogService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final TagRepository tagRepository;

    public BlogService(UserRepository userRepository, PostRepository postRepository, CommentRepository commentRepository, TagRepository tagRepository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
        this.tagRepository = tagRepository;
    }

    public User createUser(String username, String email) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("User with this username already exists.");
        }
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        return userRepository.save(user);
    }

    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
    }

    public Post createPost(Long userId, String title, String content, Set<String> tagNames) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Author with id " + userId + " not found."));

        Post post = new Post();
        post.setTitle(title);
        post.setContent(content);
        post.setAuthor(author);

        Set<Tag> tags = new HashSet<>();
        for (String tagName : tagNames) {
            Tag tag = tagRepository.findByName(tagName)
                    .orElseGet(() -> {
                        Tag newTag = new Tag();
                        newTag.setName(tagName);
                        return tagRepository.save(newTag);
                    });
            tags.add(tag);
        }
        post.setTags(tags);

        return postRepository.save(post);
    }

    public Post updatePostContent(Long postId, String newContent) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post with id " + postId + " not found."));
        post.setContent(newContent);
        return postRepository.save(post);
    }

    public void deletePost(Long postId) {
        postRepository.deleteById(postId);
    }

    public Comment addComment(Long postId, Long userId, String content) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post with id " + postId + " not found."));
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User with id " + userId + " not found."));

        Comment comment = new Comment();
        comment.setContent(content);
        comment.setPost(post);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    public List<Post> findPostsByKeyword(String keyword) {
        return postRepository.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    public List<Post> findPostsByTagName(String tagName) {
        return postRepository.findByTags_Name(tagName);
    }
}