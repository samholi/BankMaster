package in.softgrid.service;



import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.softgrid.entity.Customer;
import in.softgrid.repositary.customerRepository;


@Service
public class CustomerService 
{

    @Autowired
    private customerRepository custRepo;
 
    public void saveCustomer(Customer customer) {
        custRepo.save(customer);
    }
    
    public Customer findCustomerById(Long id) {
        return custRepo.findById(id).orElse(null);
    }
    
    public List<Customer> searchCustomers(Long id, String firstname, String lastname, String email, String phone) {
        return custRepo.findByCriteria(id, firstname, lastname, email, phone);
    }
    
}



