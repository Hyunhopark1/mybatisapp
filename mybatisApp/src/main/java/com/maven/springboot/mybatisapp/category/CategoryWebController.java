package com.maven.springboot.mybatisapp.category;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/ctweb")
public class CategoryWebController {

    @Autowired
    private CategoryServiceImpl categoryService;

    @GetMapping("")
    public String indexHome() {
        return "index";
    }

    @GetMapping("/catweb/category_add")
    public String categoryAdd(){
        return "catweb/category_add";
    }


    @GetMapping("/category_old")    // 브라우저의 URL 주소
    public String categoryOld(Model model, @RequestParam(required=false) String name, @RequestParam int page) {
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
            //뷰에 allList, searchCategoryDto 데이터를 전달
            model.addAttribute("allList", allList);
            model.addAttribute("searchCategoryDto", searchCategoryDto);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", "오류가 발생했습니다. 관리자에게 문의하세요.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "oldhtml/category_old";     //해당 이름의 뷰를 찾아서 데이터를 렌더링함
    }

    @PostMapping("/category_insert")
    public String categoryOldInsert(@ModelAttribute CategoryDto dto, Model model) {
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
        return "redirect:category_old?page=1&name=";  // 브라우저 주소를 redirect 한다.
    }

    @GetMapping("/catweb/category_old_view")    // 브라우저의 URL 주소
    public String categoryOldView(Model model, @RequestParam Long id) {
        try {
            if ( id == null || id <= 0 ) {
                model.addAttribute("error_message", "ID는 1보다 커야 합니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            ICategory find = this.categoryService.findById(id);
            if ( find == null ) {
                model.addAttribute("error_message", id + " 데이터가 없습니다.");
                return "error/error_find";
            }
            model.addAttribute("categoryDto", find);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", "서버 에러입니다. 관리자에게 문의 하세요.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "catweb/category_view";     //resources/templates 폴더안의 화면파일 찾음
    }



    @PostMapping("/catweb/category_old_update")
    public String categoryOldUpdate(Model model, @ModelAttribute CategoryDto categoryDto) {
        try {
            if (categoryDto == null || categoryDto.getId() <= 0 || categoryDto.getName().isEmpty()) {
                model.addAttribute("error_message", "id는 1보다 커야하고, name 이 있어야 합니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            ICategory find = this.categoryService.findById(categoryDto.getId());
            if (find == null) {
                model.addAttribute("error_message", categoryDto.getId() + " 데이터가 없습니다.");
                return "error/error_find";
            }
            this.categoryService.update(categoryDto.getId(), categoryDto);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", categoryDto.getName() + " 중복입니다.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_old?page=1&name=";
    }

    @GetMapping("/catweb/category_old_delete")
    public String categoryDelete(Model model, @RequestParam Long id) {
        try {
            if (id == null || id <= 0) {
                model.addAttribute("error_message", "id는 1보다 커야 합니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            ICategory find = this.categoryService.findById(id);
            if (find == null) {
                model.addAttribute("error_message", id + " 데이터가 없습니다.");
                return "error/error_find";
            }
            this.categoryService.delete(id);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", "서버 에러입니다. 관리자에게 문의 하세요.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_old?page=1&name=";
    }



    /*아직 미완성*/
    @GetMapping("/category_search")
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
        return "redirect:category_old?page="+ page + "&name=" + name;
    }

}
