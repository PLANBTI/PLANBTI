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
import com.example.demo.boundedContext.order.entity.OrderStatus;
import com.example.demo.boundedContext.order.repository.order.OrderRepository;
import com.example.demo.boundedContext.order.repository.orderdetail.OrderDetailRepository;
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
                               OrderDetailRepository orderDetailRepository, AddressRepository addressRepository, CategoryRepository categoryRepository) {

        return new CommandLineRunner() {

            @Override
            @Transactional
            public void run(String... args) throws Exception {

                /* 상품을 위한 카테고리 생성 */

                String[] rawCategories = {"istj", "isfj", "infj", "intj", "istp", "isfp", "infp", "intp", "estp",
                        "esfp", "enfp", "entp", "estj", "esfj", "enfj", "entj"};
                List<Category> categories = new ArrayList<>();

                for (String rawCategory : rawCategories) {
                    Category category = Category.builder().name(rawCategory).build();
                    categoryRepository.save(category);
                    categories.add(category);
                }

                /* 상품 생성 */

                Product product1 = productRepository.save(Product.builder()
                        .count(10)
                        .price(25000)
                        .salePrice(25000)
                        .category(categories.get(2))
                        .imageUrl("https://ifh.cc/g/4GwqBf.jpg")
                        .content("자엽풍년화는 아시아가 원산인 상록 관목이다. 흰색 꽃에 초록색 잎이 피는 종류가 있고 분홍색 꽃에 적갈색 잎이 피는 종류가 있다. 학명의 'Loropetalum'은 끈이라는 뜻의 loron과 잎 또는 꽃잎을 뜻하는 petalon이 합쳐진 말로, 꽃잎이 마치 가는 끈 같기 때문에 붙은 이름이다.")
                        .name("자엽풍년화").build());

                Product product2 = productRepository.save(Product.builder()
                        .count(100)
                        .price(10000)
                        .salePrice(10000)
                        .category(categories.get(3))
                        .imageUrl("https://ifh.cc/g/VGLJx6.png")
                        .content("타고난 생명력과 관상용으로는 으뜸으로 치는 진한 초록색 이파리 때문에 많은 사람들이 애용하는 식물이다. 식물을 처음 길러보는 사람에게 가장 추천되는 종이기도 하다. 지지대만 제대로 설치하면 실내에서도 20미터까지 키울 수 있다고 한다. (작은 화분에 심고 가끔씩 물만 줘도 쭉쭉 뻗어나간다.) 다른 식물보다 훨씬 생명력이 강하긴 하지만, 햇빛과 배수에 약간의 신경은 써줘야 더 건강하게 자랄 수 있다. 직사광선을 피하고 반양지에서 키워야 하며 물은 겉흙이 마르고 나서 충분히 줘야 과습을 피할 수 있다. 분무기로 이파리에 수분을 공급하면 싱싱하게 보이도록 만들 수 있다.")
                        .name("스킨답서스").build());

                Product product3 = productRepository.save(Product.builder()
                        .category(categories.get(0))
                        .name("뱅갈고무나무")
                        .price(39000)
                        .count(3)
                        .salePrice(25000)
                        .imageUrl("https://planbti.cdn.ntruss.com/plant3.jpg")
                        .content("뱅갈 고무나무(Hevea brasiliensis)는 고무를 생산하는 주요 작물로 알려진 열대 지역에서 자라는 나무입니다. 주로 남아메리카의 아마존 우림 지역에서 원산지를 가지며, 이후 많은 국가에서 고무 생산을 위해 재배되고 있습니다. 뱅갈 고무나무는 높이가 20~30m에 달하며, 그림자를 제공하고 온도를 조절해주는 큰 잎을 가지고 있습니다. 이 나무는 꽤 오래된 역사를 가지고 있으며, 고무 생산에 매우 중요한 역할을 합니다. 수액이라는 특별한 액체를 가지고 있는데, 이 액체에서 고무를 추출하여 다양한 용도로 활용됩니다.")
                        .build());

                Product product4 = productRepository.save(Product.builder()
                        .category(categories.get(1))
                        .name("산세베리아")
                        .price(39000)
                        .count(1)
                        .salePrice(21000)
                        .imageUrl("https://planbti.cdn.ntruss.com/plant2.jpg")
                        .content("아스파라거스과의 다육식물로 질감이 단단하고 직립으로 자란다. 특유의 생김새 때문인지 영어로는 Snake Plant(뱀 식물) 또는 잎 모양이 날카롭다고 mother-in-law's tongue(장모님의 혀)라고 불린다. 생명력이 워낙 강해 환경에 그리 민감한 편이 아니기에 원예 초보자들도 키우기 쉽지만 너무 음지에서 키우면 색이 엷어지고 반대로 너무 강한 햇빛에 두면 잎이 노랗게 변할 수 있다. 유의할 점은 여느 다육이들이 그러하듯 물을 지나치게 주면 잎이 쉬이 꺾이고 뿌리가 썩어 죽을 수 있다.")
                        .build());

                /* 회원, 관리자 생성 */

                String encode = passwordEncoder.encode("1111");
                Member user = Member.builder()
                        .username("user1")
                        .password(encode)
                        .phoneNumber("010-1111-1111")
                        .email("user1@naver.com")
                        .build();
                user.addRole(Role.USER);
                memberRepository.save(user);

                Member admin = Member.builder()
                        .username("admin")
                        .password(encode)
                        .phoneNumber("010-1111-1111")
                        .email("user1@naver.com")
                        .build();
                admin.addRole(Role.USER);
                admin.addRole(Role.ADMIN);
                memberRepository.save(admin);

                /* 상품 1에 대한 리뷰 생성 */

                for (int i = 0; i < 20; i++) {
                    Review review = Review.builder()
                            .member(user)
                            .content("content!!!!" + i)
                            .title("title" + i)
                            .rate(i % 5 + 1)
                            .build();
                    product1.addReview(review);
                    reviewRepository.save(review);
                }

                /* 주문, 주문 상세 생성 */

                Order order = Order.builder()
                        .orderName("orderName")
                        .totalPrice(20000L)
                        .member(user)
                        .build();
                orderRepository.save(order);

                Order order2 = Order.builder()
                        .orderName("order")
                        .status(OrderStatus.COMPLETE)
                        .totalPrice(10000L)
                        .member(user)
                        .build();
                orderRepository.save(order2);

                OrderDetail orderDetail1 = OrderDetail.builder()
                        .status(PENDING)
                        .product(product1)
                        .order(order2)
                        .count(2)
                        .build();
                orderDetailRepository.save(orderDetail1);

                OrderDetail orderDetail2 = OrderDetail.builder()
                        .status(PENDING)
                        .product(product1)
                        .order(order2)
                        .count(2)
                        .build();
                orderDetailRepository.save(orderDetail2);

                OrderDetail orderDetail3 = OrderDetail.builder()
                        .status(EXCHANGE)
                        .product(product1)
                        .invoiceNumber("0123456789")
                        .order(order2)
                        .count(2)
                        .build();
                orderDetailRepository.save(orderDetail3);

                OrderDetail orderDetail4 = OrderDetail.builder()
                        .status(RETURN)
                        .product(product2)
                        .invoiceNumber("0123456789")
                        .order(order2)
                        .count(3)
                        .build();
                orderDetailRepository.save(orderDetail4);

                OrderDetail orderDetail5 = OrderDetail.builder()
                        .status(PLACED)
                        .product(product2)
                        .order(order2)
                        .count(5)
                        .build();
                orderDetailRepository.save(orderDetail5);

                List<OrderDetail> orderDetails = new ArrayList<>();
                orderDetails.add(orderDetail1);
                orderDetails.add(orderDetail2);
                orderDetails.add(orderDetail3);
                orderDetails.add(orderDetail4);
                orderDetails.add(orderDetail5);

                Order modifiedOrder2 = order2.toBuilder()
                        .orderDetailList(orderDetails).build();
                orderRepository.save(modifiedOrder2);

                /* 회원의 주소, 문의, 관리자의 코멘트 생성 */

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
                Member modifiedUser = user.toBuilder().addresses(list).build();
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

                Comment comment = Comment.builder()
                        .faq(faq1).content("test content").build();
                commentRepository.save(comment);
                Faq modifiedFaq1 = faq1.toBuilder().comment(comment).build();
                faqRepository.save(modifiedFaq1);

            }
        };
    }
}