package com.maven.springboot.mybatisapp.category;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Controller
@RequestMapping("/catweb")
public class CateWebController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("")
    public String indexHome() {
        return "index";
    }
    @GetMapping("/category_list")    // 브라우저의 URL 주소
    public String categoryOld(Model model, @RequestParam String name, @RequestParam int page) {
        try {
            if (name == null) {
                name = "";
            }
//            List<ICategory> allList = this.categoryService.getAllList();
            SearchCategoryDto searchCategoryDto = SearchCategoryDto.builder()
                    .name(name).page(page).build();
            int count = this.categoryService.countAllByNameContains(searchCategoryDto);
            searchCategoryDto.setTotal(count);
            List<ICategory> allList = this.categoryService.findAllByNameContains(searchCategoryDto);
            model.addAttribute("allList", allList);
            model.addAttribute("searchCategoryDto", searchCategoryDto);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", "오류가 발생했습니다. 관리자에게 문의하세요.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "catweb/category_list";     //resources/templates 폴더안의 화면파일 찾음
    }

    @GetMapping("/category_old_search")
    public String categorySearch(Model model,  @RequestParam String name, @RequestParam(value = "page", required = false, defaultValue = "1")  int page) {
        try {
            if (name == null || name.isEmpty()) {
                model.addAttribute("error_message", "name 이 있어야 합니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            SearchCategoryDto searchCategoryDto = SearchCategoryDto.builder()
                    .name(name).page(page).build();
            this.categoryService.findAllByNameContains(searchCategoryDto);

        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", name + " 오류입니다.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_list?page="+ page + "&name=" + name;
    }

    @GetMapping("/category_add")
    public String categoryAdd(){
        return "catweb/category_add";
    }

    @PostMapping("/category_insert")
    public String categoryInsert(@ModelAttribute CategoryDto dto,Model model){
        try {
            if (dto == null || dto.getName() == null || dto.getName().isEmpty()) {
                model.addAttribute("error_message", "이름이 비었습니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            this.categoryService.insert(dto);

        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", dto.getName() + " 중복입니다.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_list?page=1&name=";  // 브라우저 주소를 redirect 한다.
    }



}