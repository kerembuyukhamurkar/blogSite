package com.example.FullStackBlogSite.blog;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/blog")
public class BlogController {
    private final Blogrepository blogrepository;

    public BlogController(Blogrepository blogrepository) {
        this.blogrepository = blogrepository;
    }

    @PostMapping("")
    void create(@RequestBody Blog blog){
        blogrepository.createBlog(blog);
    }

    @GetMapping("")
    List<Blog> getAll(){
        return blogrepository.getAllBlogs();
    }
    @GetMapping("/{id}")
    Blog getById(@PathVariable Integer id){
        return blogrepository.findBlogById(id);
    }
}
