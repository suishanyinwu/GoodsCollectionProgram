import com.it.controller.FavoritesController;
import com.it.controller.GoodsController;
import com.it.controller.SearchController;
import com.it.controller.UserController;
import com.it.entity.Brand;
import com.it.entity.Goods;
import com.it.entity.User;
import com.it.entity.WXuser;
import com.it.mapper.TagListMapper;
import com.it.service.*;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath*:spring.xml"})
public class MybatisSpringTest {

    @Autowired
    TagListMapper tagListMapper;
    @Autowired
    private UserService userService;
    @Autowired
    private PublishService publishService;
    @Autowired
    private GoodsService goodsService;

    @Autowired
    private FavoritesController favoritesController;

    @Autowired
    private FavoritesService favoritesService;
    @Autowired
    private GoodsController goodsController;
    @Autowired
    private SearchController searchController;
    @Autowired
    private UserController userController;
    @Autowired
    WeiChatService weiChatService;
    @Autowired
    RecService recService;
    @Autowired
    ReviewService reviewService;


    @Test
    public void test(){
//        List<Map<String, Object>> list = favoritesController.findFavGoodsList("1205", "0");
//        System.out.println(list);

//        List<String> goodsListByUserID = favoritesService.findGoodsListByUserID("1205", 0);
//        System.out.println(goodsListByUserID);
//        List<Brand> brandsList = favoritesController.findFavBrandsList("1205", "0");
//        System.out.println(brandsList);


//        System.out.println(goodsService.findGoodInfo("goods-1"));
//        favoritesService.addFavInfo("goods-1","1205");


//        System.out.println(goodsController.findFavInfo("goods-1","120"));

//        System.out.println(goodsService.searchByText("夏彦"));

//        System.out.println(searchController.getSearchGoodsList("夏彦"));

//        System.out.println(userController.login("0e1ru7ml2pb4hf4xCHml2ECbVW2ru7mO"));
//        weiChatService.login("0c14U90w3K6gD43BKw0w3rqh3n34U90h");

     /*   System.out.println(tagListMapper.findUnTagIP("fGMjbbMagzKxPVH"));*/
//        for (Goods goods : recService.recommendGoods("fGMjbbMagzKxPVH")) {
//            System.out.println(goods.toString());
//        }
//        recService.recommendGoods("fGMjbbMagzKxPVH");

        System.out.println(reviewService.findReviewScoresByGoodsId(""));
    }


    //测试GoodsService
    @Test
    public void testGoodsService(){
/*        Goods newGoods = new Goods();
        newGoods.setGoodsId("good-1");
        newGoods.setGoodsSeries("鸣沙踏歌 浮金彩韵马口铁徽章");
        newGoods.setPrice(18);
        newGoods.setIpId("ip-0001");
        newGoods.setCraft("PET印刷+珍珠细闪工艺+烫金工艺");
        newGoods.setSize("直径75mm");
        newGoods.setType("马口铁徽章");
        goodsService.addNewGoods(newGoods);*/


        System.out.println(goodsService.searchByText("未定"));
    }

    //测试user
    @Test
    public void testMybatisSpring(){
        User user = userService.findById("1111");
        System.out.println(user);

    }


}
