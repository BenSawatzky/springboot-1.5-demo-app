package answer.king.service;

import java.util.List;

import answer.king.exception.BusinessRulesException;
import answer.king.model.Item;

public interface ItemService {

  List<Item> getAll();

  Item save(Item item) throws BusinessRulesException;
}
