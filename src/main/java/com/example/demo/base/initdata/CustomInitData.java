package com.example.demo.base.initdata;

import com.example.demo.base.Role;
import com.example.demo.boundedContext.category.entity.Category;
import com.example.demo.boundedContext.category.repository.CategoryRepository;
import com.example.demo.boundedContext.faq.entity.Comment;
import com.example.demo.boundedContext.faq.entity.Faq;
import com.example.demo.boundedContext.faq.entity.FaqCategory;
import com.example.demo.boundedContext.faq.repository.CommentRepository;
import com.example.demo.boundedContext.faq.repository.FaqRepository;
import com.example.demo.boundedContext.member.entity.Address;
import com.example.demo.boundedContext.member.entity.Member;
import com.example.demo.boundedContext.member.repository.AddressRepository;
import com.example.demo.boundedContext.member.repository.MemberRepository;
import com.example.demo.boundedContext.order.entity.Order;
import com.example.demo.boundedContext.order.entity.OrderDetail;

import com.example.demo.boundedContext.order.repository.orderdetail.OrderDetailRepository;
import com.example.demo.boundedContext.order.repository.order.OrderRepository;
import com.example.demo.boundedContext.product.entity.Product;
import com.example.demo.boundedContext.product.entity.Review;
import com.example.demo.boundedContext.product.repository.product.ProductRepository;
import com.example.demo.boundedContext.product.repository.review.ReviewRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.example.demo.boundedContext.order.entity.OrderItemStatus.*;


@Profile({"dev", "test", "prod"})
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
                List<Category> categories = new ArrayList<>();
                categories.add(Category.builder().name("istj").build());
                categories.add(Category.builder().name("isfj").build());
                categories.add(Category.builder().name("infj").build());
                categories.add(Category.builder().name("intj").build());
                categories.add(Category.builder().name("istp").build());
                categories.add(Category.builder().name("isfp").build());
                categories.add(Category.builder().name("infp").build());
                categories.add(Category.builder().name("intp").build());
                categories.add(Category.builder().name("estp").build());
                categories.add(Category.builder().name("esfp").build());
                categories.add(Category.builder().name("enfp").build());
                categories.add(Category.builder().name("entp").build());
                categories.add(Category.builder().name("estj").build());
                categories.add(Category.builder().name("esfj").build());
                categories.add(Category.builder().name("enfj").build());
                categories.add(Category.builder().name("entj").build());

                categoryRepository.saveAll(categories);

                Product save = productRepository.save(
                        Product.builder()
                                .category(categories.get(0))
                                .name("뱅갈고무나무")
                                .price(39000)
                                .count(3)
                                .salePrice(25000)
                                .imageUrl("https://planbti.cdn.ntruss.com/plant3.jpg")
                                .build()
                );

                Product save2 = productRepository.save(
                        Product.builder()
                                .category(categories.get(1))
                                .name("산세베리아")
                                .price(39000)
                                .count(1)
                                .salePrice(21000)
                                .imageUrl("https://planbti.cdn.ntruss.com/plant2.jpg")
                                .build()
                );
                Product product1 = productRepository.save(Product.builder()
                        .count(10)
                        .price(15000)
                        .salePrice(25000)
                        .category(categories.get(2))
                        .name("자엽풍년화").build());
                Product product2 = productRepository.save(Product.builder()
                        .count(100)
                        .category(categories.get(3))
                        .price(10000)
                        .name("스킨답서스").build());

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
                        .status(PENDING)
                        .product(product1)
                        .order(order)
                        .count(2)
                        .build();
                orderDetailRepository.save(orderDetail1);

                OrderDetail orderDetail2 = OrderDetail.builder()
                        .status(PENDING)
                        .product(product1)
                        .order(order)
                        .count(2)
                        .build();
                orderDetailRepository.save(orderDetail2);

                OrderDetail orderDetail3 = OrderDetail.builder()
                        .status(EXCHANGE)
                        .product(product1)
                        .invoiceNumber("0123456789")
                        .order(order)
                        .count(2)
                        .build();
                orderDetailRepository.save(orderDetail3);

                OrderDetail orderDetail4 = OrderDetail.builder()
                        .status(RETURN)
                        .product(product1)
                        .invoiceNumber("0123456789")
                        .order(order)
                        .count(2)
                        .build();
                orderDetailRepository.save(orderDetail4);

                OrderDetail orderDetail5 = OrderDetail.builder()
                        .status(PLACED)
                        .product(product1)
                        .order(order)
                        .count(2)
                        .build();
                orderDetailRepository.save(orderDetail5);

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

                Comment comment = Comment.builder()
                        .faq(faq1).content("test content").build();
                commentRepository.save(comment);
                Faq _faq1 = faq1.toBuilder().comment(comment).build();
                faqRepository.save(_faq1);

            }
        };
    }
}