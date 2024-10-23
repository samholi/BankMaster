package in.softgrid.repositary;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import in.softgrid.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByOrderAccNo(String orderAccNo); // This method will be used to fetch orders by account number
    List<Order> findByAccountId(long accountId); // This method will be used to fetch orders by account number


}
