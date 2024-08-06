package com.naver.goods.service;

import com.naver.goods.dto.GoodsComPriceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @ClassName: SchedulingTaskService
 * @Description:
 * @Author: lg
 * @Date: 2024/8/3 21:41
 */
@Slf4j
@Component
public class SchedulingTaskService {

    @Autowired
    private GoodsInfoService goodsInfoService;

    @Autowired
    private StoreInfoService storeInfoService;

    private static String storeName = "패션 핫이슈";
    /**
     * 定时比价
     */
    @Scheduled(cron = "* 0/18 * * * *")
//    @Scheduled(cron = "*/5 * * * * ?")
    private void scheduledPriceParity() throws Exception {
//        Map<String,String> map = new HashMap<>();
//        map.put("10680406272","48658145619");
//        map.put("10680453917","35517595893");
//        map.put("10680516872","49379921621");
//        map.put("10680545463","48121274533");
//        map.put("10680591003","49331762619");
//        map.put("10680702631","42266322096");
//        map.put("10681465916","49357598623");
//        map.put("10683851619","49360875619");
//        map.put("10683874474","41757465114");
//        map.put("10683892288","48120896944");
//        map.put("10683923486","48745111622");
//        map.put("10683981714","47910304236");
//        map.put("10684024248","49122554828");
//        map.put("10684055439","41318617049");
//        map.put("10684095191","40943053534");
//        map.put("10684124861","41072138122");
//        map.put("10684149865","48359235119");
//        map.put("10684183300","49231576755");
//        map.put("10684226403","40407451149");
//        map.put("10684282951","36992856412");
//        map.put("10684325656","35385648070");
//        map.put("10684342981","39438449611");
//        map.put("10684366012","42315039666");
//        map.put("10684383521","35387426842");
//        map.put("10684403996","47446082111");
//        map.put("10686314238","41496404382");
//        map.put("10686432222","41013578682");
//        map.put("10686480116","48727882623");
//        map.put("10686522256","48900648827");
//        map.put("10686551451","49020589865");
//        map.put("10686610574","33193829703");
//        map.put("10688795050","47508840914");
//        map.put("10688960241","29873980543");
//        map.put("10688847734","31728079165");
//        map.put("10688899149","33278330876");
//        map.put("10688920099","41101323869");
//        map.put("10681242284","49440619055");
//        map.put("10699771897","43055569184");
//        map.put("10699791620","31890510989");
//        map.put("10699818352","41150211454");
//        map.put("10699850428","39725587651");
//        map.put("10699885011","49560021142");
//        map.put("10699906670","39699113342");
//        map.put("10699945821","44547674227");
//        map.put("10699691085","41235697818");
//        map.put("10693787620","43938440005");
//        map.put("10694045454","38016398436");
//        map.put("10694131545","38830337359");
//        map.put("10694167777","47936291142");
//        map.put("10694210000","38801568200");
//        map.put("10694245283","38266003378");
//        map.put("10694278045","44500238176");
//        map.put("10681543606","43477185573");
//        map.put("10690620892","45377483309");
//        map.put("10694942461","39019646948");
//        map.put("10694961050","45377477335");
//        map.put("10694988193","41234662531");
//        map.put("10690034483","47846015545");
//        map.put("10645994814","48458119608");
//        map.put("10650732028","26812389120");
//        map.put("10650903375","45723736052");
//        map.put("10651013390","40371133300");
//        map.put("10651153122","46556610020");
//        map.put("10675519584","48118023489");
//        map.put("10675170126","46810023605");
//        map.put("10675560209","49207426792");
//        map.put("10676257174","47368944667");
//        map.put("10676294556","48986968288");
//        map.put("10676327470","49503234560");
//        map.put("10676377223","48233759172");
//        map.put("10676430354","48870405044");
//        map.put("10676518229","47506773295");
//        map.put("10680308285","39835343883");
//        map.put("10690868289","48279706519");
//        map.put("10691122185","42922406763");
//        map.put("10691183136","49456245609");
//        map.put("10691215378","49621447177");
//        for (String key:map.keySet()) {
//            goodsInfoService.oprGoodsInfo(key, map.get(key), storeName);
//        }
        List<GoodsComPriceInfo> goodsComPriceInfoList = storeInfoService.getGoodsComPriceInfo();
        if (CollectionUtils.isEmpty(goodsComPriceInfoList) || goodsComPriceInfoList.size() == 0){
            return;
        }
        log.info(">>>> goodsComPriceInfoList:{}", goodsComPriceInfoList);
        for (GoodsComPriceInfo comPriceInfo : goodsComPriceInfoList){
            goodsInfoService.oprGoodsInfo(comPriceInfo);
        }
    }

}
