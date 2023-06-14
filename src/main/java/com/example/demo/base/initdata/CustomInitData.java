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

                for(int i=0; i<16; i++) {
                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("자엽풍년화")
                            .price(25000)
                            .salePrice(25000)
                            .count(10)
                            .imageUrl("https://ifh.cc/g/4GwqBf.jpg")
                            .content("자엽풍년화는 아시아가 원산인 상록 관목이다. 흰색 꽃에 초록색 잎이 피는 종류가 있고 분홍색 꽃에 적갈색 잎이 피는 종류가 있다. 학명의 'Loropetalum'은 끈이라는 뜻의 loron과 잎 또는 꽃잎을 뜻하는 petalon이 합쳐진 말로, 꽃잎이 마치 가는 끈 같기 때문에 붙은 이름이다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("스킨답서스")
                            .price(10000)
                            .salePrice(10000)
                            .count(100)
                            .imageUrl("https://ifh.cc/g/VGLJx6.png")
                            .content("타고난 생명력과 관상용으로는 으뜸으로 치는 진한 초록색 이파리 때문에 많은 사람들이 애용하는 식물이다. 식물을 처음 길러보는 사람에게 가장 추천되는 종이기도 하다. 지지대만 제대로 설치하면 실내에서도 20미터까지 키울 수 있다고 한다. (작은 화분에 심고 가끔씩 물만 줘도 쭉쭉 뻗어나간다.) 다른 식물보다 훨씬 생명력이 강하긴 하지만, 햇빛과 배수에 약간의 신경은 써줘야 더 건강하게 자랄 수 있다. 직사광선을 피하고 반양지에서 키워야 하며 물은 겉흙이 마르고 나서 충분히 줘야 과습을 피할 수 있다. 분무기로 이파리에 수분을 공급하면 싱싱하게 보이도록 만들 수 있다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("뱅갈고무나무")
                            .price(25000)
                            .salePrice(25000)
                            .count(3)
                            .imageUrl("https://planbti.cdn.ntruss.com/plant3.jpg")
                            .content("뱅갈 고무나무(Hevea brasiliensis)는 고무를 생산하는 주요 작물로 알려진 열대 지역에서 자라는 나무입니다. 주로 남아메리카의 아마존 우림 지역에서 원산지를 가지며, 이후 많은 국가에서 고무 생산을 위해 재배되고 있습니다. 뱅갈 고무나무는 높이가 20~30m에 달하며, 그림자를 제공하고 온도를 조절해주는 큰 잎을 가지고 있습니다. 이 나무는 꽤 오래된 역사를 가지고 있으며, 고무 생산에 매우 중요한 역할을 합니다. 수액이라는 특별한 액체를 가지고 있는데, 이 액체에서 고무를 추출하여 다양한 용도로 활용됩니다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("산세베리아")
                            .price(21000)
                            .salePrice(21000)
                            .count(1)
                            .imageUrl("https://planbti.cdn.ntruss.com/plant2.jpg")
                            .content("아스파라거스과의 다육식물로 질감이 단단하고 직립으로 자란다. 특유의 생김새 때문인지 영어로는 Snake Plant(뱀 식물) 또는 잎 모양이 날카롭다고 mother-in-law's tongue(장모님의 혀)라고 불린다. 생명력이 워낙 강해 환경에 그리 민감한 편이 아니기에 원예 초보자들도 키우기 쉽지만 너무 음지에서 키우면 색이 엷어지고 반대로 너무 강한 햇빛에 두면 잎이 노랗게 변할 수 있다. 유의할 점은 여느 다육이들이 그러하듯 물을 지나치게 주면 잎이 쉬이 꺾이고 뿌리가 썩어 죽을 수 있다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("피쉬본 선인장")
                            .price(18000)
                            .salePrice(18000)
                            .count(10)
                            .imageUrl("https://ifh.cc/g/MjWLR4.jpg")
                            .content("공작선인장 종류의 생선뼈선인장은 수집가들로 부터 많은 인기를 가지고 있어요. 가시가 없고 공중 뿌리가 줄기를 따라 자라나 마치 생선뼈를 닮아 생선뼈 선인장이라고 불리운답니다. 선인장 종류 중 성장이 빠른편이고 물을 좋아하는 편이에요. 충분한 햇빛을 받고 성장하는 여름을 거쳐 기온이 떨어지는 가을에는 꽃이 피어요. 꽃이 피고 난 뒤 맺는 열매는 새콤하고 부드러운 과일푸딩의 맛이 난답니다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("립살리스 트리고나")
                            .price(35000)
                            .salePrice(35000)
                            .count(20)
                            .imageUrl("https://ifh.cc/g/W8JAKz.jpg")
                            .content("립살리스 트리고나는 잎 단면이 세모 모양인 특이한 식물이에요. 삐죽삐죽 세모모양의 기둥이 아래로 흘러 내리면서 자라는 모습이 특이해 많은 사랑을 받고 있어요. 립살리스는 물을 자주 주지 않아도 되고 키우기 까다롭지 않은 순둥이 식물이랍니다. 독성이 없어 반려동물이 있는 집에서도 안심하고 키울 수 있어요. 립살리스는 이리저리 휘어지는 유연한 줄기를 가져서 '갈대 선인장', 흙이 아닌 바위, 나무 등에 붙어서 자랄 수 있기 때문에 '착생 선인장'으로도 불린답니다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("필로덴드론 브랜티아넘")
                            .price(10000)
                            .salePrice(10000)
                            .count(10)
                            .imageUrl("https://ifh.cc/g/a92ozT.jpg")
                            .content("브랜티아넘은 생물 분류 체계에서 식물 계통 중 하나로서, 진정한 식물을 의미합니다. 이계식물과 진정식물로 구분되며, 모든 식물 종을 포함합니다. 브랜티아넘에는 이끼류, 관목류, 나무류 등 다양한 종류의 식물들이 포함되어 있습니다. 이러한 브랜티아넘은 광합성을 통해 탄소를 고정하고 산소를 생성하는 능력을 갖추고 있습니다. 또한 뿌리를 통해 물과 영양분을 흡수하고, 줄기와 잎을 통해 광합성을 수행합니다. 식물들은 대부분 지구상에서 가장 풍부하게 분포하고 있으며, 우리 생활에 매우 중요한 역할을 합니다. 식물들은 산소를 생산하고 대기 중 이산화탄소를 흡수하여 기후 조절, 산소 생산, 생태계 유지 등의 기능을 수행합니다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("베고니아 소요카제")
                            .price(40000)
                            .salePrice(40000)
                            .count(20)
                            .imageUrl("https://ifh.cc/g/1wdLdw.jpg")
                            .content("오래 자라면 줄기가 딱딱하게 목질화되는 특징이 있는 목성 베고니아의 대표주자 중 하나인 베고니아 소요카제는 마치 흰 물감을 톡톡톡 뿌려놓은 듯한 독특한 점 무늬가 매력이예요. 목성 베고니아의 특성상 키우기도 쉽고 빠르게 자란답니다. 초록 잎 사이로 소담하게 피어나는 하얀색~분홍색의 꽃도 매력적이예요.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("동백나무")
                            .price(25000)
                            .salePrice(22000)
                            .count(10)
                            .imageUrl("https://ifh.cc/g/PVPXyM.jpg")
                            .content("동백나무는 겨울하면 생각나는 예쁜 꽃나무에요. 추운 겨울에 아름다운 꽃이 피고, 반질반질 윤기가 나는 잎을 가진 동백나무는 꽃도 아름답지만 꽃이 진 후에는 잎을 보는 관엽식물로도 훌륭하답니다. 동백나무는 꽃 색과 모양이 다양하고, 종류에 따라 향기가 진하게 나는 품종도 있어요. 실내에서 키우기도 어렵지 않아 변치않는 사랑을 듬뿍 받고 있는 식물이랍니다. 동백나무는 한 번 걸러진 빛이 오래 들어오는 곳에서 주변 온도를 서늘하게 관리해준다면 매년 아름다운 꽃을 보여줄 거에요.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("자트로파 물티피다")
                            .price(28000)
                            .salePrice(28000)
                            .count(20)
                            .imageUrl("https://ifh.cc/g/XXl3ns.jpg")
                            .content("이국적인 느낌을 자아내는 자트로파는 멕시코에서 온 식물이에요. 아름다운 민트색 줄기와 눈의 결정을 닮은 잎이 매력적인 식물이에요. 밝은 빛이 오래 머무는 곳에서 물을 너무 자주 주지 않고 키워주면 초보 식집사도 쉽게 키울 수 있어요. 식물의 진액에는 독성이 있어 알러지 반응을 일으킬 수 있기 때문에 반려동물과 어린 아이가 있는 집에서는 주의가 필요해요.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("무늬 아단소니")
                            .price(17000)
                            .salePrice(15000)
                            .count(15)
                            .imageUrl("https://ifh.cc/g/DSD51O.jpg")
                            .content("구멍이 뽕뽕 뚫린 귀여운 잎에 선명한 흰색, 크림색 무늬가 시선을 사로잡는 몬스테라 아단소니 바리에가타는 '무단이'라는 애칭으로 잘 알려져 있는 몬스테라 아단소니의 잎 변이종이랍니다. 알록달록 잎사귀가 덩굴처럼 뻗어나가며 우아한 수형으로 자란답니다. 잎의 하얀 무늬는 유전적인 변이로 인해 초록색을 띠는 엽록소가 없어서 하얀색으로 보이며, 흰 부분이 많을수록 햇빛을 흡수하여 필요한 양분을 만들어내는 광합성 작용을 잘 하지 못하기 때문에 일반 아단소니보다 더 많은 빛이 필요해요. 잎을 자르면 나오는 수액에 독성이 있으니 아이가 있는 집이나 반려동물이 있는 집에는 주의해서 키워야 해요.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("바나나 크로톤")
                            .price(30000)
                            .salePrice(30000)
                            .count(10)
                            .imageUrl("https://ifh.cc/g/v9l5cC.jpg")
                            .content("바나나 모양의 잎에 선명한 노란 무늬가 있는 바나나 크로톤은 색감이 예쁘고 키우기 쉬워 오랫동안 사랑 받는 실내 공기정화 식물이예요. 열대지방 출신이라 추위에 약하므로 항상 따뜻한 곳에서 키워주세요. 바나나 크로톤은 따뜻하고 은은한 빛이 드는 곳에서 키워주시면 누구나 쉽게 키울 수 있는 식물입니다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("줄리아 페페")
                            .price(20000)
                            .salePrice(18000)
                            .count(15)
                            .imageUrl("https://ifh.cc/g/dlD87Q.jpg")
                            .content("줄리아 페페는 아몬드를 닮은 초록색 잎이 귀여운 식물이에요. 페페로미아 종류로 잎이 두툼해서 물을 머금고 있기 때문에 건조에 강한 편이에요. 빛이 다소 적게 드는 곳에서도 잘 자라준답니다.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("필로덴드론 마요이")
                            .price(16000)
                            .salePrice(14500)
                            .count(20)
                            .imageUrl("https://ifh.cc/g/WxchJd.jpg")
                            .content("시원하게 갈라진 반질반질한 잎이 유니크한 필로덴드론 마요이는 필로덴드론 친구들 중에 독보적인 생김새로 식집사들의 워너비 식물로 꼽혀요. 마요이는 잎의 존재감이 크고, 수형이 독특해 마요이 하나만으로도 훌륭한 뷰포인트가 된답니다. 어린 잎은 통통한 아기 손처럼 자라지만, 성체가 되면 멋스러운 길쭉길쭉 갈라진 잎을 보여줘요. 줄기가 옆으로 퍼지듯이 자라는 마요이는 주변 공기를 따뜻하게, 그리고 촉촉하게 관리해주시면 건강하게 잘 키울 수 있어요. 필로덴드론의 종류답게 공기 중의 포름알데히드와 일산화탄소를 제거하는 능력이 뛰어나 실내 공기정화 식물로의 역할도 수행해요.")
                            .build());

                    productRepository.save(Product.builder()
                            .category(categories.get(i))
                            .name("피어리스 (시기지움)")
                            .price(30000)
                            .salePrice(28000)
                            .count(12)
                            .imageUrl("https://ifh.cc/g/w2ark4.jpg")
                            .content("피어리스라고 유통되고 있는 시기지움은 사계절 싱그러운 진녹색 잎을 보여주는 식물이에요. 해외에선 조경수로 사용되어 울타리로 연출되기도 하며, 체리와 비슷한 열매를 맺는다고 해서 영명이 Brush Cherry라 불린답니다. '행운'을 의미해 유럽에서는 선물용으로 많이 이용된답니다. 가지치기를 통해 원하는 모양을 잡아주면 더욱 이쁘게 키울 수 있어요.")
                            .build());
                }

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

                Product product1 = productRepository.findById(1L).get();
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

                Product product2 = productRepository.findById(2L).get();
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