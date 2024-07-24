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
        }catch(Exception ex){
            log.error(ex.toString());
        }
        return "oldhtml/category_old";     //resources/templates 폴더안의 화면파일 찾음
    }

    @PostMapping("/oldhtml/category_old_act")
    public String categoryOldAct(@ModelAttribute CategoryDto dto, Model model) {
        try {
            if (dto == null || dto.getName() == null || dto.getName().isEmpty()) {
                return "redirect:category_old";    //브라우저 주소를 redirect 한다.
            }
            this.categoryservice.insert(dto);
            model.addAttribute("Dto", dto);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return "redirect:category_old";  //브라우저 주소를 redirect 한다.
    }

    @GetMapping("/oldhtml/category_old_view")    //브라우저의 URL 주소
    public String categoryOldView(@RequestParam Long id, Model model) {
        try {
            ICategory byId = this.categoryservice.findById(id);
            if (byId == null) {
                return "redirect:category_old";
            }
            model.addAttribute("categoryDto", byId);
        }catch(Exception ex){
            log.error(ex.toString());
        }
        return "oldhtml/category_view";     //resources/templates 폴더안의 화면파일 찾음
    }

    @PostMapping("/oldhtml/category_old_update")
    public String categoryOldUpdate(@ModelAttribute CategoryDto dto) {
        try {
            if (dto == null || dto.getName() == null || dto.getName().isEmpty()) {
                return "redirect:category_old";    //브라우저 주소를 redirect 한다.
            }
            this.categoryservice.update(dto.getId(),dto);
        } catch (Exception ex) {
            log.error(ex.toString());
        }
        return "redirect:category_old";  //브라우저 주소를 redirect 한다.
    }

    @GetMapping("/oldhtml/category_old_delete")    //브라우저의 URL 주소
    public String categoryOldDelete(@RequestParam Long id) {
        try {
            if (id == null) {
                return "redirect:category_old";
            }
           this.categoryservice.remove(id);
        }catch(Exception ex){
            log.error(ex.toString());
        }
        return "redirect:category_old";     //resources/templates 폴더안의 화면파일 찾음
    }
}
