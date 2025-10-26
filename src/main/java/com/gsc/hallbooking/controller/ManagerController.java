package com.gsc.hallbooking.controller;

import com.gsc.hallbooking.entity.FoodItem;
import com.gsc.hallbooking.entity.Hall;
import com.gsc.hallbooking.service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * DEVELOPER: SRIKARSAN
 * CRUD: Manager Dashboard & Management System
 */
@Controller
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    
    private final ManagerService managerService;
    
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("hallCount", managerService.getAllHalls().size());
        model.addAttribute("foodCount", managerService.getAllFoodItems().size());
        return "manager/dashboard";
    }
    
    // ========== HALL MANAGEMENT ==========
    
    @GetMapping("/halls")
    public String listHalls(Model model) {
        model.addAttribute("halls", managerService.getAllHalls());
        return "manager/halls";
    }
    
    @GetMapping("/halls/new")
    public String newHallForm(Model model) {
        return "manager/hall-form";
    }
    
    @PostMapping("/halls/create")
    public String createHall(@RequestParam String name,
                            @RequestParam Integer capacity,
                            @RequestParam String description,
                            RedirectAttributes redirectAttributes) {
        try {
            managerService.createHall(name, capacity, description);
            redirectAttributes.addFlashAttribute("success", "Hall created successfully!");
            return "redirect:/manager/halls";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/halls/new";
        }
    }
    
    @GetMapping("/halls/edit/{id}")
    public String editHallForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            Hall hall = managerService.getHallById(id);
            model.addAttribute("hall", hall);
            return "manager/hall-edit";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/halls";
        }
    }
    
    @PostMapping("/halls/update/{id}")
    public String updateHall(@PathVariable Long id,
                            @RequestParam String name,
                            @RequestParam Integer capacity,
                            @RequestParam String description,
                            @RequestParam Boolean active,
                            RedirectAttributes redirectAttributes) {
        try {
            managerService.updateHall(id, name, capacity, description, active);
            redirectAttributes.addFlashAttribute("success", "Hall updated successfully!");
            return "redirect:/manager/halls";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/halls/edit/" + id;
        }
    }
    
    @PostMapping("/halls/delete/{id}")
    public String deleteHall(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            managerService.deleteHall(id);
            redirectAttributes.addFlashAttribute("success", "Hall deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/manager/halls";
    }
    
    // ========== FOOD MANAGEMENT ==========
    
    @GetMapping("/foods")
    public String listFoods(Model model) {
        model.addAttribute("foods", managerService.getAllFoodItems());
        return "manager/foods";
    }
    
    @GetMapping("/foods/new")
    public String newFoodForm(Model model) {
        return "manager/food-form";
    }
    
    @PostMapping("/foods/create")
    public String createFood(@RequestParam String name,
                            @RequestParam Double pricePerPerson,
                            @RequestParam String description,
                            @RequestParam String category,
                            RedirectAttributes redirectAttributes) {
        try {
            managerService.createFoodItem(name, pricePerPerson, description, category);
            redirectAttributes.addFlashAttribute("success", "Food item created successfully!");
            return "redirect:/manager/foods";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/foods/new";
        }
    }
    
    @GetMapping("/foods/edit/{id}")
    public String editFoodForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        try {
            FoodItem food = managerService.getFoodItemById(id);
            model.addAttribute("food", food);
            return "manager/food-edit";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/foods";
        }
    }
    
    @PostMapping("/foods/update/{id}")
    public String updateFood(@PathVariable Long id,
                            @RequestParam String name,
                            @RequestParam Double pricePerPerson,
                            @RequestParam String description,
                            @RequestParam String category,
                            @RequestParam Boolean active,
                            RedirectAttributes redirectAttributes) {
        try {
            managerService.updateFoodItem(id, name, pricePerPerson, description, category, active);
            redirectAttributes.addFlashAttribute("success", "Food item updated successfully!");
            return "redirect:/manager/foods";
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/manager/foods/edit/" + id;
        }
    }
    
    @PostMapping("/foods/delete/{id}")
    public String deleteFood(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            managerService.deleteFoodItem(id);
            redirectAttributes.addFlashAttribute("success", "Food item deleted successfully!");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        return "redirect:/manager/foods";
    }
}

