package com.devcourse.springjpaboard.order.service;

import com.devcourse.springjpaboard.domain.order.Order;
import com.devcourse.springjpaboard.domain.order.OrderRepository;
import com.devcourse.springjpaboard.order.converter.OrderConverter;
import com.devcourse.springjpaboard.order.dto.OrderDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    // OSIV
    // 엔티티를 트랜잭션이 묶여있지 않은 곳까지 끌고 나가는건 좋은 습관이 아님
    // 엔티티는 RDB와 통신하고 있는 객체이기 때문에 트랜잭션으로 관리되지 않는 영역까지 가지고 가게되면 예상치 못한 쿼리가 발생할 수 있음
    @Autowired
    private OrderConverter orderConverter;

    // Transactional 어노테이션을 붙이면 aop를 이용해서 엔티티 매니저를 이용하여 트랜잭션 매니저를 관리하던 부분을 자동 설정해줌
    @Transactional
    public String save(OrderDto dto) {
        // 1. dto -> entity 변환 (준영속)
        Order order = orderConverter.convertOrder(dto);
        // 2. orderRepository.save(entity) -> 영속화
        Order entity = orderRepository.save(order);
        // 3. 결과 반환
        return entity.getUuid();
    }


    public void findAll() {

    }

    public void findOne() {

    }
}
