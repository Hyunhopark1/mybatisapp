package com.maven.springboot.mybatisapp.category;


import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor

public class CategoryDto implements ICategory {

    private Long id;
    private String name;
}
