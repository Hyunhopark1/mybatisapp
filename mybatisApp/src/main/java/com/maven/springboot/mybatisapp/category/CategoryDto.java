package com.maven.springboot.mybatisapp.category;


import lombok.*;


@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto implements ICategory {

    private Long id;
    private String name;

    @Override
    public String toString() {
        return String.format("ID : %6d, 이름 : %s", this.id, this.name);
    }
}
