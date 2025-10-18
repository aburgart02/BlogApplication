package com.moderndb.blog;

import com.moderndb.blog.model.Post;
import com.moderndb.blog.model.User;
import com.moderndb.blog.service.BlogService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@SpringBootApplication
public class BlogApplication {

	public static void main(String[] args) {
		SpringApplication.run(BlogApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(BlogService blogService) {
		return args -> {
			System.out.println("1. Создаем двух пользователей");
			User user1 = blogService.createUser("john.doe", "john.doe@example.com");
			User user2 = blogService.createUser("jane.smith", "jane.smith@example.com");
			System.out.println("   > Пользователь создан: " + user1.getUsername() + " (ID: " + user1.getId() + ")");
			System.out.println("   > Пользователь создан: " + user2.getUsername() + " (ID: " + user2.getId() + ")");

			System.out.println("\n2. Пользователь 'john.doe' создает пост с тэгами 'java', 'spring', 'jpa'");
			Post post1 = blogService.createPost(
					user1.getId(),
					"Введение в Spring Data JPA",
					"Spring Data JPA упрощает создание слоя доступа к данным",
					Set.of("java", "spring", "jpa")
			);
			System.out.println("   > Пост создан: \"" + post1.getTitle() + "\" (ID: " + post1.getId() + ")");

			System.out.println("\n3. Пользователь 'jane.smith' создает пост с тэгами 'java' и 'hibernate'");
			Post post2 = blogService.createPost(
					user2.getId(),
					"Hibernate и его кэши",
					"Hibernate предоставляет многоуровневое кэширование",
					Set.of("java", "hibernate")
			);
			System.out.println("   > Пост создан: \"" + post2.getTitle() + "\" (ID: " + post2.getId() + ")");

			System.out.println("\n4. Пользователи добавляют комментарии к первому посту");
			blogService.addComment(post1.getId(), user2.getId(), "Отличная статья! Очень полезно");
			blogService.addComment(post1.getId(), user1.getId(), "Спасибо! Рад, что понравилось");
			System.out.println("   > Добавлено 2 комментария к посту ID: " + post1.getId());

			System.out.println("\n5. Ищем посты, содержащие слово 'JPA'");
			List<Post> jpaPosts = blogService.findPostsByKeyword("JPA");
			jpaPosts.forEach(p -> System.out.println("   > Найден пост: " + p.getTitle()));

			System.out.println("\n6. Ищем все посты с тэгом 'java'");
			List<Post> javaPosts = blogService.findPostsByTagName("java");
			System.out.println("   > Найдено " + javaPosts.size() + " поста с тэгом 'java':");
			javaPosts.forEach(p -> System.out.println("   > - " + p.getTitle()));

			System.out.println("\n7. Обновляем содержание второго поста");
			blogService.updatePostContent(post2.getId(), "Обновленное содержание про кэширование в Hibernate");
			System.out.println("   > Пост ID: " + post2.getId() + " обновлен");

			System.out.println("\n8. Удаляем первый пост");
			blogService.deletePost(post1.getId());
			System.out.println("   > Пост ID: " + post1.getId() + " удален");
		};
	}
}