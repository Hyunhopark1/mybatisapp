package com.maven.springboot.mybatisapp.category;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@Controller
@RequestMapping("/ctweb")
public class CategoryWebController {

    @Autowired
    private CategoryServiceimpl categoryservice;

    @GetMapping("")
    public String indexHome() {
        return "index";
    }

    @GetMapping("/oldhtml/category_old")    //브라우저의 URL 주소
    public String category_old(Model model) {
        try {
            List<ICategory> allList = this.categoryservice.getAllList();
            model.addAttribute("itemList", allList);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return "oldhtml/category_old";     //resources/templates 폴더안의 화면파일 찾음
    }

    @PostMapping("/oldhtml/category_old_act")
    public String categoryOldInsert(@ModelAttribute CategoryDto dto, Model model) {
        try {
            if (dto == null || dto.getName() == null || dto.getName().isEmpty()) {
                model.addAttribute("error_message", "이름이 비었습니다.");
                return "error/error_save";
            }
            this.categoryservice.insert(dto);

        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", dto.getName() + " 중복 혹은 제한글자 수를 벗어났습니다.");
            return "error/error_save";
        }
        return "redirect:category_old";  //브라우저 주소를 redirect 한다.
    }

    @GetMapping("/oldhtml/category_old_view")    //브라우저의 URL 주소
    public String categoryOldView(@RequestParam Long id, Model model) {
        try {
            if (id == null || id <= 0) {
                model.addAttribute("error_message", "ID는 1보다 커야합니다.");
                return "error/error_bad";
            }
            ICategory find = this.categoryservice.findById(id);
            if (find == null) {
                model.addAttribute("error_message", id + "데이터가 없습니다.");
                return "error/error_find";
            }
            model.addAttribute("categoryDto", find);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return "oldhtml/category_view";     //resources/templates 폴더안의 화면파일 찾음
    }

    @PostMapping("/oldhtml/category_old_update")
    public String categoryOldUpdate(Model model, @ModelAttribute CategoryDto categoryDto) {
        try {
            if (categoryDto == null || categoryDto.getId() <= 0 || categoryDto.getName().isEmpty()) {
                model.addAttribute("error_message", "id는 1보다 커야하고, name 이 있어야 합니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            ICategory find = this.categoryservice.findById(categoryDto.getId());
            if (find == null) {
                model.addAttribute("error_message", categoryDto.getId() + " 데이터가 없습니다.");
                return "error/error_find";
            }
            this.categoryservice.update(categoryDto.getId(), categoryDto);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", categoryDto.getName() + " 중복입니다.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_old";  //브라우저 주소를 redirect 한다.
    }

    @GetMapping("/oldhtml/category_old_delete")
    public String categoryDelete(Model model, @RequestParam Long id) {
        try {
            if (id == null || id <= 0) {
                model.addAttribute("error_message", "id는 1보다 커야 합니다.");
                return "error/error_bad";  // resources/templates 폴더안의 화면파일
            }
            ICategory find = this.categoryservice.findById(id);
            if (find == null) {
                model.addAttribute("error_message", id + " 데이터가 없습니다.");
                return "error/error_find";
            }
            this.categoryservice.remove(id);
        } catch (Exception ex) {
            log.error(ex.toString());
            model.addAttribute("error_message", "서버 에러입니다. 관리자에게 문의 하세요.");
            return "error/error_save";  // resources/templates 폴더안의 화면파일
        }
        return "redirect:category_old";
    }
}
