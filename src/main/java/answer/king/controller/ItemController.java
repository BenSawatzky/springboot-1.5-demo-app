package answer.king.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import answer.king.exception.BusinessRulesException;
import answer.king.model.Item;
import answer.king.service.ItemService;

@RestController
@RequestMapping("/item")
public class ItemController {

  @Autowired
  private ItemService itemService;

  @RequestMapping(method = RequestMethod.GET)
  public List<Item> getAll() {
    return itemService.getAll();
  }

  @RequestMapping(method = RequestMethod.POST)
  public Item create(@RequestBody Item item) throws BusinessRulesException {
    return itemService.save(item);
  }

  @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
  public Item update(@PathVariable("id") Long id, @RequestBody Item item) throws BusinessRulesException {
    item.setId(id);
    return itemService.save(item);
  }
}

