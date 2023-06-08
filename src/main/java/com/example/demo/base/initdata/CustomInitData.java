package com.example.demo.base.initdata;
import com.example.demo.base.Role;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.entity.FaqCategory;
import com.example.demo.boundedContext.faq.repository.CommentRepository;
import com.example.demo.boundedContext.faq.repository.FaqRepository;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.category.repository.CategoryRepository;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.AddressRepository;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;
import com.example.demo.boundedContext.order.entity.OrderItemStatus;
import com.example.demo.boundedContext.order.repository.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.Review;
import com.example.demo.boundedContext.product.repository.ProductRepository;
import com.example.demo.boundedContext.product.repository.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Profile({"dev", "test"})
@Configuration
public class CustomInitData {
    @Bean
    CommandLineRunner initData(MemberRepository memberRepository, PasswordEncoder passwordEncoder,
                               OrderRepository orderRepository, ProductRepository productRepository,
                               FaqRepository faqRepository, CommentRepository commentRepository,
                               ReviewRepository reviewRepository,
                               OrderDetailRepository orderDetailRepository, AddressRepository addressRepository,CategoryRepository categoryRepository ) {

        return new CommandLineRunner() {

            @Override
            @Transactional
            public void run(String... args) throws Exception {
                Product product1 = productRepository.save(Product.builder()
                                .count(10)
                        .price(15000)
                        .name("product1").build());
                Product product2 = productRepository.save(Product.builder()
                        .count(100)
                        .price(10000)
                        .name("product2").build());

                for (int i = 0; i < 20; i++) {
                    Review review = Review.builder()
                            .content("content!!!!"+i)
                            .title("title"+i)
                            .rate(i)
                            .build();
                    product1.addReview(review);
                    reviewRepository.save(review);
                }

                String encode = passwordEncoder.encode("1111");
                Member user = Member.builder()
                        .username("user1")
                        .password(encode)
                        .phoneNumber("010-1111-1111")
                        .email("user1@naver.com")
                        .build();
                user.addRole(Role.USER);
                memberRepository.save(user);

                Order order = Order.builder()
                        .orderName("orderName")
                        .member(user)
                        .build();
                orderRepository.save(order);

                OrderDetail orderDetail1 = OrderDetail.builder()
                        .count(2)
                        .build();
                orderDetail1.addOrder(order,product1);
                orderDetailRepository.save(orderDetail1);

                OrderDetail orderDetail2 = OrderDetail.builder()
                        .count(2)
                        .build();
                orderDetail2.addOrder(order,product2);
                orderDetailRepository.save(orderDetail2);

                Address address = Address.builder()
                        .member(user)
                        .name("배송지 1")
                        .addr("서울시")
                        .addrDetail("중구")
                        .zipCode("00000")
                        .phoneNumber("01012345678")
                        .build();
                addressRepository.save(address);

                List<Address> list = user.getAddresses();
                list.add(address);
                Member modifiedUser = user.toBuilder()
                        .addresses(list).build();
                memberRepository.save(modifiedUser);

                Faq faq1 = Faq.builder()
                        .member(user)
                        .category(FaqCategory.PRODUCT)
                        .title("test1")
                        .content("test content1")
                        .email(user.getEmail()).build();
                faqRepository.save(faq1);

                Faq faq2 = Faq.builder()
                        .member(user)
                        .category(FaqCategory.EXCHANGE)
                        .title("test2")
                        .content("test content2")
                        .email(user.getEmail()).build();
                faqRepository.save(faq2);

                Member admin = Member.builder()
                        .username("admin")
                        .password(encode)
                        .phoneNumber("010-1111-1111")
                        .email("user1@naver.com")
                        .build();
                admin.addRole(Role.USER);
                admin.addRole(Role.ADMIN);
                memberRepository.save(admin);

                List<Category> categories = new ArrayList<>();

                categories.add(Category.builder().id(1L).name("istj").build());
                categories.add(Category.builder().id(2L).name("isfj").build());
                categories.add(Category.builder().id(3L).name("infj").build());
                categories.add(Category.builder().id(4L).name("intj").build());
                categories.add(Category.builder().id(5L).name("istp").build());
                categories.add(Category.builder().id(6L).name("isfp").build());
                categories.add(Category.builder().id(7L).name("infp").build());
                categories.add(Category.builder().id(8L).name("intp").build());
                categories.add(Category.builder().id(9L).name("estp").build());
                categories.add(Category.builder().id(10L).name("esfp").build());
                categories.add(Category.builder().id(11L).name("enfp").build());
                categories.add(Category.builder().id(12L).name("entp").build());
                categories.add(Category.builder().id(13L).name("estj").build());
                categories.add(Category.builder().id(14L).name("esfj").build());
                categories.add(Category.builder().id(15L).name("enfj").build());
                categories.add(Category.builder().id(16L).name("entj").build());

                categoryRepository.saveAll(categories);


                Product save = productRepository.save(
                        Product.builder()
                                .category(categories.get(0))
                                .name("뱅갈고무나무")
                                .price(39000)
                                .count(3)
                                .salePrice(25000)
                                .build()
                );

                Product save2 = productRepository.save(
                        Product.builder()
                                .category(categories.get(1))
                                .name("산세베리아")
                                .price(39000)
                                .count(1)
                                .salePrice(21000)
                                .build()
                );

                Comment comment = Comment.builder()
                        .faq(faq1).content("test content").build();
                commentRepository.save(comment);
                Faq _faq1 = faq1.toBuilder().comment(comment).build();
                faqRepository.save(_faq1);
            }
        };
      }
}
