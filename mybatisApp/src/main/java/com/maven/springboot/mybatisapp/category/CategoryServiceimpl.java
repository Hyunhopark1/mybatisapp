package com.maven.springboot.mybatisapp.category;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceimpl implements ICategoryService {

    @Autowired
    private CategoryMybatisMapper categoryMybatisMapper;


    @Override
    public ICategory findById(Long id) {

        CategoryDto dto = categoryMybatisMapper.findById(id);
        return dto;
    }

    @Override
    public ICategory findByName(String name) {
        if (name == null || name.isEmpty()) {
            return null;
        }
        CategoryDto dto = categoryMybatisMapper.findByName(name);
        return dto;
    }

    @Override
    public List<ICategory> getAllList() {
        List<ICategory> list = this.getICategoryList(this.categoryMybatisMapper.findAll());
        return list;
    }

    private List<ICategory> getICategoryList(List<CategoryDto> list) {
        if ( list == null || list.size() <= 0 ) {
            return new ArrayList<>();
        }
        // input : [CategoryEntity|CategoryEntity|CategoryEntity|CategoryEntity|CategoryEntity]
//        List<ICategory> result = new ArrayList<>();
//        for( CategoryEntity entity : list ) {
//            result.add( (ICategory)entity );
//        }
        List<ICategory> result = list.stream()
                .map(entity -> (ICategory)entity)
                .toList();
        // return : [ICategory|ICategory|ICategory|ICategory|ICategory]
        return result;
    }

    @Override
    public ICategory insert(ICategory category) throws Exception {
        if ( !isValidInsert(category) ) {
            return null;
        }
        CategoryDto dto = new CategoryDto();
        dto.copyFields(category);
        dto.setId(0L);
        this.categoryMybatisMapper.insert(dto);
        return dto;
    }
    private boolean isValidInsert(ICategory category) {
        if ( category == null ) {
            return false;
        } else if ( category.getName() == null || category.getName().isEmpty() ) {
            return false;
        }
        return true;
    }

    @Override
    public boolean remove(Long id) throws Exception {
        ICategory find = this.findById(id);
        if (find == null) {
            return false;
        }
        categoryMybatisMapper.deleteById(id);
        return true;
    }

    @Override
    public ICategory update(Long id, ICategory category) throws Exception {
        ICategory find = this.findById(id);
        if (find == null) {
            return null;
        }
        find.copyFields(category);

        this.categoryMybatisMapper.update((CategoryDto) find);
        return find;
    }

    @Override
    public List<ICategory> findAllByNameContains(String name) {
        if ( name == null || name.isEmpty() ) {
            return new ArrayList<>();
        }
        List<ICategory> list = this.getICategoryList(
                this.categoryMybatisMapper.findAllByNameContains(name)
        );
        return list;
    }
}
