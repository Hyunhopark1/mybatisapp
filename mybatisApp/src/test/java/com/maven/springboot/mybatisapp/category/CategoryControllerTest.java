package com.maven.springboot.mybatisapp.category;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CategoryControllerTest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void CategoryTest() {
        String url = "http://localhost:" + port;

        //Category Insert Test
        //빈 Category 객체
        CategoryDto requestInsert = CategoryDto.builder().build();
        ResponseEntity<CategoryDto> responseInsert = this.testRestTemplate.postForEntity(url + "/ct"
                ,requestInsert, CategoryDto.class);
        assertThat(responseInsert).isNotNull();
        assertThat(responseInsert.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        //정상적으로 이름이 들어간 Category 객체
        CategoryDto requestInsert2 = CategoryDto.builder().name("aaaaaaa").build();
        ResponseEntity<CategoryDto> responseInsert2 = this.testRestTemplate.postForEntity(url + "/ct"
                ,requestInsert2, CategoryDto.class);
        assertThat(responseInsert2).isNotNull();
        assertThat(responseInsert2.getStatusCode()).isEqualTo(HttpStatus.OK);
        System.out.println("responseInsert2.getBody().getId() = " + responseInsert2.getBody().getId());
        assertThat(responseInsert2.getBody().getName()).isEqualTo("aaaaaaa");

        //제한길이가 초과된 이름이 들어간 Category객체
        CategoryDto requestInsert3 = CategoryDto.builder().name("a8a8a8a8a8").build();
        ResponseEntity<CategoryDto> responseInsert3 = this.testRestTemplate.postForEntity(url + "/ct", requestInsert3, CategoryDto.class);
        assertThat(responseInsert3).isNotNull();
        assertThat(responseInsert3.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

        //Category Find Test
        Long insertId = responseInsert2.getBody().getId();
        ResponseEntity<CategoryDto> findEntity = this.testRestTemplate.getForEntity(url + "/ct/" + insertId.toString(), CategoryDto.class);
        assertThat(findEntity).isNotNull();
        assertThat(findEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        ICategory resultFind = findEntity.getBody();
        assertThat(resultFind).isNotNull();
        assertThat(resultFind.getId()).isEqualTo(insertId);
        assertThat(resultFind.getName()).isEqualTo("aaaaaaa");

        ResponseEntity<CategoryDto> notFindEntity1 = this.testRestTemplate.getForEntity(url + "/ct/0", CategoryDto.class);
        assertThat(notFindEntity1).isNotNull();
        assertThat(notFindEntity1.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ResponseEntity<CategoryDto> notFindEntity = this.testRestTemplate.getForEntity(url + "/ct/1000" , CategoryDto.class);
        assertThat(notFindEntity).isNotNull();
        assertThat(notFindEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        //Category Update Test
        ICategory update = CategoryDto.builder().build();
        update.copyFields(resultFind);
        update.setName("ABCDE");
        CategoryDto resultEntity = this.testRestTemplate.patchForObject(url + "/ct/" + update.getId(), update, CategoryDto.class);
        assertThat(resultEntity).isNotNull();
        assertThat(resultEntity.getName()).isEqualTo("ABCDE");

        //Category Delete Test

        CategoryDto delete = CategoryDto.builder().id(update.getId()).build();
        this.testRestTemplate.delete(url+ "/ct/"+delete.getId());

        ResponseEntity<CategoryDto> deleteEntity = this.testRestTemplate.getForEntity(url + "/ct/" + delete.getId(), CategoryDto.class);
        assertThat(deleteEntity).isNotNull();
        assertThat(deleteEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}