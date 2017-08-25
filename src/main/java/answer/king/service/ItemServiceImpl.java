package answer.king.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import javax.validation.Validator;

import answer.king.exception.BusinessRulesException;
import answer.king.model.Item;
import answer.king.model.Receipt;
import answer.king.repo.ItemRepository;
import answer.king.validation.ValidationUtils;

@Service
@Transactional
public class ItemServiceImpl implements ItemService {

  @Autowired
  private ItemRepository itemRepository;

  @Autowired
  private Validator validator;
  private ValidationUtils<Item> validationUtils = new ValidationUtils<>();

  @Override
  public List<Item> getAll() {
    return itemRepository.findAll();
  }

  @Override
  public Item save(Item item) throws BusinessRulesException {
    validationUtils.validateParams(item, validator);
    return itemRepository.save(item);
  }
}
