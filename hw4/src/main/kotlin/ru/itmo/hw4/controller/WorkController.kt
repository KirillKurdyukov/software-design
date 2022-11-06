package ru.itmo.hw4.controller

import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.ModelAttribute
import ru.itmo.hw4.repository.WorkRepository

@Controller
class WorkController(
    private val workRepository: WorkRepository
) {

    @GetMapping("/")
    fun getWorks(modelMap: ModelMap): String {
        modelMap["listsWorks"] = workRepository.findAllListsWorks()

        return "index";
    }

    @GetMapping("/get-list-works")
    fun getWorksByList(
        @ModelAttribute("listId") id: Int,
        modelMap: ModelMap,
    ): String {
        prepareWorks(modelMap, id)

        return "works"
    }

    @PostMapping("/create-new-list-works")
    fun createNewListWorks(@ModelAttribute("listName") listName: String): String {
        workRepository.saveListWorks(listName)

        return "redirect:/";
    }

    @PostMapping("/delete-list-works")
    fun deleteListWorks(@ModelAttribute("listId") id: Int): String {
        workRepository.deleteListWorks(id)

        return "redirect:/"
    }

    @PostMapping("/add-new-work")
    fun addWork(
        @ModelAttribute("listId") id: Int,
        @ModelAttribute("workName") workName: String,
        modelMap: ModelMap,
    ): String {
        workRepository.saveWork(id, workName)

        prepareWorks(modelMap, id)
        return "works"
    }

    @PostMapping("/delete-work")
    fun deleteWork(
        @ModelAttribute("listId") listId: Int,
        @ModelAttribute("workId") workId: Int,
        modelMap: ModelMap,
    ): String {
        workRepository.deleteWork(workId)

        prepareWorks(modelMap, listId)
        return "works"
    }

    @PostMapping("/done-work")
    fun doneWork(
        @ModelAttribute("listId") listId: Int,
        @ModelAttribute("workId") workId: Int,
        modelMap: ModelMap,
    ): String {
        workRepository.markDoneWork(workId)

        prepareWorks(modelMap, listId)
        return "works"
    }

    private fun prepareWorks(modelMap: ModelMap, id: Int) {
        modelMap["listId"] = workRepository.findListById(id).get().listId
        modelMap["listName"] = workRepository.findListById(id).get().listName
        modelMap["works"] = workRepository.findWorksByListId(id)
    }
}