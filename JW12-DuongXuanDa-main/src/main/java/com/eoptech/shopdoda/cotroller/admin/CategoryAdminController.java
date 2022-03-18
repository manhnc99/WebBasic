package com.eoptech.shopdoda.cotroller.admin;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.eoptech.shopdoda.entities.Category;
import com.eoptech.shopdoda.repositories.CategoriesRepo;
import com.eoptech.shopdoda.utils.UserUtils;
import com.eoptech.shopdoda.utils.Utilities;

@Controller
public class CategoryAdminController {

	@Autowired
	private CategoriesRepo categoriesRepo;

	@RequestMapping(value = { "/admin/categories" }, method = RequestMethod.GET)
	public String categories(final ModelMap model, final HttpServletRequest request, final HttpServletResponse response)
			throws Exception {

		model.addAttribute("categoryList", categoriesRepo.findAll());

		return "back-end/category";
	}

	@RequestMapping(value = { "/admin/categories/edit/{id}" }, method = RequestMethod.GET)
	public String editCategory(final ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response, @PathVariable("id") Integer CategoryId) throws Exception {

		Category cat = categoriesRepo.findById(CategoryId).get();

		model.addAttribute("categoryEdit", cat);

		return "back-end/save_category";
	}

	@RequestMapping(value = { "/admin/categories/add" }, method = RequestMethod.GET)
	public String addCategory(final ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response) throws Exception {
		model.addAttribute("categoryEdit", new Category());
		return "back-end/save_category";
	}

	@RequestMapping(value = { "/admin/categories/save" }, method = RequestMethod.POST)
	public String saveCategory(final ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response, @ModelAttribute("categoryEdit") Category categoryEdit)
			throws Exception {
		// TH chỉnh sửa
		if (categoryEdit.getId() != null && categoryEdit.getId() > 0) {
			Category categoryInDB = categoriesRepo.findById(categoryEdit.getId()).get();
			// created & updated date
			categoryEdit.setCreatedDate(categoryInDB.getCreatedDate());
			categoryEdit.setUpdatedDate(new Date());
			// created & updated by
			categoryEdit.setCreatedBy(categoryInDB.getCreatedBy());
			categoryEdit.setUpdatedBy(UserUtils.getUserId().toString());
			
			categoryEdit.setSeo(Utilities.seo(categoryEdit.getName()));
		} else {
			categoryEdit.setCreatedDate(new Date());
			categoryEdit.setCreatedBy(UserUtils.getUserId().toString());
			categoryEdit.setSeo(Utilities.seo(categoryEdit.getName()));
		}
		categoriesRepo.save(categoryEdit);

		return "redirect:/admin/categories";
	}

	@RequestMapping(value = { "/admin/categories/delete/{id}" }, method = RequestMethod.GET)
	public String deleteCategory(final ModelMap model, final HttpServletRequest request,
			final HttpServletResponse response, @PathVariable("id") Integer categoryId) throws Exception {
		Category deletedCategory = categoriesRepo.findById(categoryId).get();
		deletedCategory.setStatus(false);
		categoriesRepo.save(deletedCategory);

		return "redirect:/admin/categories";
	}

}
