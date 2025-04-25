package com.example.FullStackBlogSite.blog;

import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class Blogrepository {
    private final JdbcClient jdbcClient;

    public Blogrepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }
    public Blog findBlogById(Integer id){
        return  jdbcClient
                .sql("Select * from blog.blogs where id=:id")
                .param("id",id)
                .query(Blog.class)
                .single();

    }
    public List<Blog> getAllBlogs(){
        return jdbcClient
                .sql("Select * from blog.blogs")
                .query(Blog.class).list();
    }

    public void createBlog(Blog blog) {
        jdbcClient.sql("INSERT INTO blog.blogs(title, content, create_date, creator) VALUES (?, ?,now(), ?)")
                .params(List.of(
                        blog.title(),
                        blog.content(),
                        blog.creator()
                ))
                .update();
    }
    public void updateBlog(String title, String content,Integer id){
        jdbcClient.sql("""
                        UPDATE blog.blogs
                        SET title =:title,
                             content=:content,
                             create_date=now()
                        WHERE id=:id
                        """)
                .param("id",id)
                .param("content",content)
                .param("title",title)
                .update();
    }

    public void deleteBlog(Integer id){
        jdbcClient.sql("DELETE FROM blog.blogs where id=:id")
                .param("id",id)
                .update();
    }
}
