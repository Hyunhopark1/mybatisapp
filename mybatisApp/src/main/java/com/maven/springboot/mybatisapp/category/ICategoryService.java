package com.maven.springboot.mybatisapp.category;

import java.util.List;


public interface ICategoryService {
    ICategory findById(Long id);
    ICategory findByName(String name);
    List<ICategory> getAllList();
    ICategory insert(ICategory category) throws Exception;
    boolean delete(Long id) throws Exception;
    ICategory update(Long id, ICategory category) throws Exception;
    List<ICategory> findAllByNameContains(SearchCategoryDto dto);
}
