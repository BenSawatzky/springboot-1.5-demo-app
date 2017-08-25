package answer.king.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import answer.king.model.Receipt;
import answer.king.repo.ReceiptRepository;

@Service
@Transactional
public class ReceiptServiceImpl implements ReceiptService {

  @Autowired
  private ReceiptRepository receiptRepository;

  @Override
  public Receipt getById(Long id) {
    return receiptRepository.findOne(id);
  }

}
