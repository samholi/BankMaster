package in.softgrid.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import in.softgrid.entity.Order;
import in.softgrid.entity.Organization;
import in.softgrid.entity.Account;
import in.softgrid.repositary.OrderRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public void saveOrder(Order order) {
        orderRepository.save(order);
    }
    
    public List<Order> findByAccountId(Long accountId) {
        return orderRepository.findByAccountId(accountId);
    }
    
    
    public List<Order> findAll() {
        return orderRepository.findAll();
    }
    
    public List<Order> findByAccountAccNo(String accountNo) {
        return orderRepository.findByOrderAccNo(accountNo); // Implement this in OrderRepository
    }

}
