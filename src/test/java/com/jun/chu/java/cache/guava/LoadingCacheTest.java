package com.jun.chu.java.cache.guava;

import java.util.concurrent.atomic.AtomicInteger;

//import java.util.Optional;

/**
 * Created by chujun on 2017/9/22.
 */
public class LoadingCacheTest {
    private static AtomicInteger                     count              = new AtomicInteger();

//    LoadingCache<String, BigExpense>                 cache              = CacheBuilder.newBuilder()
//            .build(new CacheLoader<String, BigExpense>() {
//                                                                                    public BigExpense load(String key) {
//                                                                                        return createBigExpense(key);
//                                                                                    }
//                                                                                });
//    //解决LoadingCache不允许返回null值的方法
//    LoadingCache<String, Optional<BigExpense>>       cacheNullValue     = CacheBuilder.newBuilder()
//            .build(new CacheLoader<String, Optional<BigExpense>>() {
//                                                                                    public Optional<BigExpense> load(String key) {
//                                                                                        //return createBigExpense(key);
//                                                                                        return Optional.ofNullable(
//                                                                                                createBigExpense(key));
//                                                                                    }
//                                                                                });
//
//    LoadingCache<String, Optional<List<BigExpense>>> cacheListNullValue = CacheBuilder.newBuilder()
//            .build(new CacheLoader<String, Optional<List<BigExpense>>>() {
//
//                                                                                    public Optional<List<BigExpense>> load(String key) {
//                                                                                        //return createBigExpense(key);
//                                                                                        return Optional.ofNullable(
//                                                                                                Lists.newArrayList(
//                                                                                                        createBigExpense(
//                                                                                                                key),
//                                                                                                        createBigExpense(
//                                                                                                                key)));
//                                                                                    }
//
//                                                                                });
//
//    @Test
//    public void case01_get() {
//        try {
//            System.out.println(cache.get("chujun"));
//            Optional<BigExpense> optional = cacheNullValue.get("cj");
//            //两者等效
//            System.out.println(optional.isPresent() ? optional.get() : null);
//            System.out.println(optional.orElse(null));
//
//            System.out.println(cacheListNullValue.get("cjq"));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Test
//    public void case01_get_value_is_null() throws ExecutionException {
//        try {
//            //默认方式不允许value为null
//            System.out.println(cache.get(""));
//            //com.google.common.cache.CacheLoader$InvalidCacheLoadException: CacheLoader returned null for key .
//        } catch (CacheLoader.InvalidCacheLoadException e) {
//            e.printStackTrace();
//            if (!e.getMessage().contains("CacheLoader returned null for key")) {
//                throw e;
//            }
//        }
//    }
//
//    @Test
//    public void case02_get_value_is_Absent() {
//        try {
//            Optional<BigExpense> optional = cacheNullValue.get("");
//            System.out.println(optional.isPresent() ? optional.get() : null);
//            System.out.println(optional.orElse(null));
//        } catch (ExecutionException e) {
//            e.printStackTrace();
//        }
//    }

    private BigExpense createBigExpense(String key) {
        if (null == key || "".equals(key)) {
            return null;
        }
        return new BigExpense(count.incrementAndGet(), key);
    }

    /**
     * 用来模拟高消耗对象类
     */
    static class BigExpense {
        private int    id;

        private String key;

        public BigExpense() {
        }

        public BigExpense(int id, String key) {
            this.id = id;
            this.key = key;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder("BigExpense{");
            sb.append("id=").append(id);
            sb.append(", key='").append(key).append('\'');
            sb.append('}');
            return sb.toString();
        }
    }
}
