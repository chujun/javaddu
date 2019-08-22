package com.jun.chu.java.lambda.sort;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.Test;

import java.util.Comparator;
import java.util.List;

/**
 * @author chujun
 * @date 2019-08-19 11:08
 */
public class ComparatorTest {
    @Test
    public void test() {
        List<Product> dataList = getDataList();
        //false在前，true在后
        dataList.sort(Comparator.comparing(Product::getOnline));
        //[ComparatorTest.Product(id=3, online=false, name=因为痛,所以叫青春, weight=null, stock=null), ComparatorTest.Product(id=7, online=false, name=病菌，枪炮与钢铁, weight=4, stock=null), ComparatorTest.Product(id=1, online=true, name=三国演义, weight=1, stock=null), ComparatorTest.Product(id=2, online=true, name=平凡的世界, weight=2, stock=2), ComparatorTest.Product(id=4, online=true, name=我有酒，你有故事吗, weight=3, stock=3), ComparatorTest.Product(id=5, online=true, name=邓小平时代, weight=null, stock=2), ComparatorTest.Product(id=6, online=true, name=封神榜, weight=null, stock=3)]
        System.out.println(dataList);
        //必须有null的处理,不然空指针异常，Comparator.nullsLast
        dataList.sort(Comparator.comparing(Product::haveStock, Comparator.nullsLast(Comparator.naturalOrder())));
        System.out.println(dataList);
        dataList.sort(Comparator.comparing(Product::haveStock, Comparator.nullsLast(Comparator.reverseOrder())));
        System.out.println(dataList);
    }

    public List<Product> getDataList() {
        List<Product> products = Lists.newArrayList();
        products.add(new Product(1, true, "三国演义", 1, null));
        products.add(new Product(2, true, "平凡的世界", 2, 2));
        products.add(new Product(3, false, "因为痛,所以叫青春", null, null));
        products.add(new Product(4, true, "我有酒，你有故事吗", 3, 3));
        products.add(new Product(5, true, "邓小平时代", null, 2));
        products.add(new Product(6, true, "封神榜", null, 3));
        products.add(new Product(7, false, "病菌，枪炮与钢铁", 4, null));
        return products;
    }

    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    public static class Product {
        private Integer id;
        private Boolean online;
        private String name;
        private Integer weight;
        private Integer stock;

        public Boolean haveStock() {
            if (null == stock) {
                return null;
            }
            return stock > 0;
        }
    }
}
