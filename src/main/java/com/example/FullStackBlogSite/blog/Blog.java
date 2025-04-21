package com.example.FullStackBlogSite.blog;

import java.time.LocalDateTime;

public record Blog(Integer id,
                   String title,
                   String content,
                   LocalDateTime createDate,
                   String creator
                   ) {

}
