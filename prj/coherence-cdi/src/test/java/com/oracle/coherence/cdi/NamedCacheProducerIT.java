/*
 * Copyright (c) 2019, 2020, Oracle and/or its affiliates.
 *
 * Licensed under the Universal Permissive License v 1.0 as shown at
 * http://oss.oracle.com/licenses/upl.
 */

package com.oracle.coherence.cdi;

import java.lang.annotation.Annotation;
import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Instance;
import javax.inject.Inject;

import com.tangosol.net.AsyncNamedCache;
import com.tangosol.net.NamedCache;
import com.tangosol.net.cache.CacheMap;
import com.tangosol.util.ConcurrentMap;
import com.tangosol.util.InvocableMap;
import com.tangosol.util.ObservableMap;
import com.tangosol.util.QueryMap;

import org.hamcrest.Matchers;
import org.jboss.weld.junit5.WeldInitiator;
import org.jboss.weld.junit5.WeldJunit5Extension;
import org.jboss.weld.junit5.WeldSetup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Integration test for the {@link com.oracle.coherence.cdi.ConfigurableCacheFactoryProducer}
 * using the Weld JUnit extension.
 *
 * @author Jonathan Knight  2019.10.19
 */
@ExtendWith(WeldJunit5Extension.class)
class NamedCacheProducerIT
    {

    @WeldSetup
    private WeldInitiator weld = WeldInitiator.of(WeldInitiator.createWeld()
                                                          .addBeanClass(CtorBean.class)
                                                          .addBeanClass(NamedCacheFieldsBean.class)
                                                          .addBeanClass(AsyncNamedCacheFieldsBean.class)
                                                          .addBeanClass(SuperTypesBean.class)
                                                          .addBeanClass(DifferentCacheFactoryBean.class)
                                                          .addBeanClass(FilterProducer.class)
                                                          .addBeanClass(FilterProducer.AlwaysFilterSupplier.class)
                                                          .addBeanClass(FilterProducer.WhereFilterSupplier.class)
                                                          .addBeanClass(ValueExtractorProducer.class)
                                                          .addBeanClass(ValueExtractorProducer.UniversalExtractorSupplier.class)
                                                          .addBeanClass(ValueExtractorProducer.UniversalExtractorsSupplier.class)
                                                          .addBeanClass(ValueExtractorProducer.ChainedExtractorSupplier.class)
                                                          .addBeanClass(ValueExtractorProducer.ChainedExtractorsSupplier.class)
                                                          .addBeanClass(ValueExtractorProducer.PofExtractorSupplier.class)
                                                          .addBeanClass(ValueExtractorProducer.PofExtractorsSupplier.class)
                                                          .addBeanClass(NamedCacheProducer.class)
                                                          .addBeanClass(CacheFactoryUriResolver.Default.class)
                                                          .addBeanClass(ConfigurableCacheFactoryProducer.class)
                                                          .addExtension(new CoherenceExtension()));

    @Test
    void shouldGetDynamicNamedCache()
        {
        Annotation qualifier = Cache.Literal.of("numbers");
        Instance<NamedCache> instance = weld.select(NamedCache.class, qualifier);

        assertThat(instance.isResolvable(), is(true));
        assertThat(instance.get().getCacheName(), is("numbers"));
        }

    @Test
    void shouldInjectNamedCacheUsingFieldName()
        {
        NamedCacheFieldsBean bean = weld.select(NamedCacheFieldsBean.class).get();
        assertThat(bean.getNumbers(), is(notNullValue()));
        assertThat(bean.getNumbers().getCacheName(), is("numbers"));
        }

    @Test
    void shouldInjectQualifiedNamedCache()
        {
        NamedCacheFieldsBean bean = weld.select(NamedCacheFieldsBean.class).get();
        assertThat(bean.getNamedCache(), is(notNullValue()));
        assertThat(bean.getNamedCache().getCacheName(), is("numbers"));
        }

    @Test
    void shouldInjectNamedCacheWithGenerics()
        {
        NamedCacheFieldsBean bean = weld.select(NamedCacheFieldsBean.class).get();
        assertThat(bean.getGenericCache(), is(notNullValue()));
        assertThat(bean.getGenericCache().getCacheName(), is("numbers"));
        }

    @Test
    void shouldInjectNamedCacheWithGenericKeys()
        {
        NamedCacheFieldsBean bean = weld.select(NamedCacheFieldsBean.class).get();
        assertThat(bean.getGenericKeys(), is(notNullValue()));
        assertThat(bean.getGenericKeys().getCacheName(), is("genericKeys"));
        }

    @Test
    void shouldInjectNamedCacheWithGenericValues()
        {
        NamedCacheFieldsBean bean = weld.select(NamedCacheFieldsBean.class).get();
        assertThat(bean.getGenericValues(), is(notNullValue()));
        assertThat(bean.getGenericValues().getCacheName(), is("genericValues"));
        }

    @Test
    void shouldInjectAsyncNamedCacheUsingFieldName()
        {
        AsyncNamedCacheFieldsBean bean = weld.select(AsyncNamedCacheFieldsBean.class).get();
        assertThat(bean.getNumbers(), is(notNullValue()));
        assertThat(bean.getNumbers().getNamedCache().getCacheName(), is("numbers"));
        }

    @Test
    void shouldInjectQualifiedAsyncNamedCache()
        {
        AsyncNamedCacheFieldsBean bean = weld.select(AsyncNamedCacheFieldsBean.class).get();
        assertThat(bean.getNamedCache(), is(notNullValue()));
        assertThat(bean.getNamedCache().getNamedCache().getCacheName(), is("numbers"));
        }

    @Test
    void shouldInjectAsyncNamedCacheWithGenerics()
        {
        AsyncNamedCacheFieldsBean bean = weld.select(AsyncNamedCacheFieldsBean.class).get();
        assertThat(bean.getGenericCache(), is(notNullValue()));
        assertThat(bean.getGenericCache().getNamedCache().getCacheName(), is("numbers"));
        }

    @Test
    void shouldInjectAsyncNamedCacheWithGenericKeys()
        {
        AsyncNamedCacheFieldsBean bean = weld.select(AsyncNamedCacheFieldsBean.class).get();
        assertThat(bean.getGenericKeys(), is(notNullValue()));
        assertThat(bean.getGenericKeys().getNamedCache().getCacheName(), is("genericKeys"));
        }

    @Test
    void shouldInjectAsyncNamedCacheWithGenericValues()
        {
        AsyncNamedCacheFieldsBean bean = weld.select(AsyncNamedCacheFieldsBean.class).get();
        assertThat(bean.getGenericValues(), is(notNullValue()));
        assertThat(bean.getGenericValues().getNamedCache().getCacheName(), is("genericValues"));
        }

    @Test
    void shouldInjectCachesFromDifferentCacheFactories()
        {
        DifferentCacheFactoryBean bean = weld.select(DifferentCacheFactoryBean.class).get();

        assertThat(bean.getDefaultCcfNumbers(), is(notNullValue()));
        assertThat(bean.getDefaultCcfNumbers().getCacheName(), Matchers.is("numbers"));
        assertThat(bean.getDefaultCcfAsyncNumbers(), is(notNullValue()));
        assertThat(bean.getDefaultCcfAsyncNumbers().getNamedCache().getCacheName(), Matchers.is("numbers"));
        assertThat(bean.getDefaultCcfAsyncNumbers().getNamedCache(), is(bean.getDefaultCcfNumbers()));

        assertThat(bean.getSpecificCcfNumbers(), is(notNullValue()));
        assertThat(bean.getSpecificCcfNumbers().getCacheName(), Matchers.is("numbers"));
        assertThat(bean.getSpecificCcfAsyncNumbers(), is(notNullValue()));
        assertThat(bean.getSpecificCcfAsyncNumbers().getNamedCache().getCacheName(), Matchers.is("numbers"));
        assertThat(bean.getSpecificCcfAsyncNumbers().getNamedCache(), is(bean.getSpecificCcfNumbers()));

        assertThat(bean.getDefaultCcfNumbers(), is(not(bean.getSpecificCcfNumbers())));
        }

    @Test
    void testCtorInjection()
        {
        CtorBean bean = weld.select(CtorBean.class).get();

        assertThat(bean.getNumbers(), Matchers.notNullValue());
        assertThat(bean.getNumbers().getCacheName(), Matchers.is("numbers"));
        assertThat(bean.getLetters(), Matchers.notNullValue());
        assertThat(bean.getLetters().getNamedCache().getCacheName(), Matchers.is("letters"));
        }

    @Test
    void shouldInjectSuperTypeInvocableMap()
        {
        SuperTypesBean bean = weld.select(SuperTypesBean.class).get();
        InvocableMap map = bean.getInvocableMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(sameInstance(bean.getNamedCache())));
        }

    @Test
    void shouldInjectSuperTypeObservableMap()
        {
        SuperTypesBean bean = weld.select(SuperTypesBean.class).get();
        ObservableMap map = bean.getObservableMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(sameInstance(bean.getNamedCache())));
        }

    @Test
    void shouldInjectSuperTypeConcurrentMap()
        {
        SuperTypesBean bean = weld.select(SuperTypesBean.class).get();
        ConcurrentMap map = bean.getConcurrentMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(sameInstance(bean.getNamedCache())));
        }

    @Test
    void shouldInjectSuperTypeQueryMap()
        {
        SuperTypesBean bean = weld.select(SuperTypesBean.class).get();
        QueryMap map = bean.getQueryMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(sameInstance(bean.getNamedCache())));
        }

    @Test
    void shouldInjectSuperTypeCacheMap()
        {
        SuperTypesBean bean = weld.select(SuperTypesBean.class).get();
        CacheMap map = bean.getCacheMap();
        assertThat(map, is(notNullValue()));
        assertThat(map, is(sameInstance(bean.getNamedCache())));
        }

    // ----- test beans -----------------------------------------------------

    @ApplicationScoped
    private static class NamedCacheFieldsBean
        {
        @Inject
        private NamedCache numbers;

        @Inject
        @Cache("numbers")
        private NamedCache namedCache;

        @Inject
        @Cache("numbers")
        private NamedCache<Integer, String> genericCache;

        @Inject
        private NamedCache<List<String>, String> genericKeys;

        @Inject
        private NamedCache<String, List<String>> genericValues;

        public NamedCache getNumbers()
            {
            return numbers;
            }

        public NamedCache getNamedCache()
            {
            return namedCache;
            }

        public NamedCache<Integer, String> getGenericCache()
            {
            return genericCache;
            }

        public NamedCache<List<String>, String> getGenericKeys()
            {
            return genericKeys;
            }

        public NamedCache<String, List<String>> getGenericValues()
            {
            return genericValues;
            }
        }

    @ApplicationScoped
    private static class AsyncNamedCacheFieldsBean
        {
        @Inject
        private AsyncNamedCache numbers;

        @Inject
        @Cache("numbers")
        private AsyncNamedCache namedCache;

        @Inject
        @Cache("numbers")
        private AsyncNamedCache<Integer, String> genericCache;

        @Inject
        private AsyncNamedCache<List<String>, String> genericKeys;

        @Inject
        private AsyncNamedCache<String, List<String>> genericValues;

        public AsyncNamedCache getNumbers()
            {
            return numbers;
            }

        public AsyncNamedCache getNamedCache()
            {
            return namedCache;
            }

        public AsyncNamedCache<Integer, String> getGenericCache()
            {
            return genericCache;
            }

        public AsyncNamedCache<List<String>, String> getGenericKeys()
            {
            return genericKeys;
            }

        public AsyncNamedCache<String, List<String>> getGenericValues()
            {
            return genericValues;
            }
        }

    @ApplicationScoped
    private static class DifferentCacheFactoryBean
        {
        @Inject
        @Cache("numbers")
        private NamedCache defaultCcfNumbers;

        @Inject
        @Cache("numbers")
        private AsyncNamedCache defaultCcfAsyncNumbers;

        @Inject
        @Cache("numbers")
        @CacheFactory("test-cache-config.xml")
        private NamedCache specificCcfNumbers;

        @Inject
        @Cache("numbers")
        @CacheFactory("test-cache-config.xml")
        private AsyncNamedCache specificCcfAsyncNumbers;

        public NamedCache getDefaultCcfNumbers()
            {
            return defaultCcfNumbers;
            }

        public AsyncNamedCache getDefaultCcfAsyncNumbers()
            {
            return defaultCcfAsyncNumbers;
            }

        public NamedCache getSpecificCcfNumbers()
            {
            return specificCcfNumbers;
            }

        public AsyncNamedCache getSpecificCcfAsyncNumbers()
            {
            return specificCcfAsyncNumbers;
            }
        }

    @ApplicationScoped
    private static class CtorBean
        {

        private final NamedCache<Integer, String> numbers;

        private final AsyncNamedCache<String, String> letters;

        @Inject
        CtorBean(@Cache("numbers") NamedCache<Integer, String> numbers,
                 @Cache("letters") AsyncNamedCache<String, String> letters)
            {

            this.numbers = numbers;
            this.letters = letters;
            }

        NamedCache<Integer, String> getNumbers()
            {
            return numbers;
            }

        AsyncNamedCache<String, String> getLetters()
            {
            return letters;
            }
        }

    @ApplicationScoped
    private static class SuperTypesBean
        {
        @Inject
        @Cache("numbers")
        private NamedCache<Integer, String> namedCache;

        @Inject
        @Cache("numbers")
        private InvocableMap<Integer, String> invocableMap;

        @Inject
        @Cache("numbers")
        private ObservableMap<Integer, String> observableMap;

        @Inject
        @Cache("numbers")
        private ConcurrentMap<Integer, String> concurrentMap;

        @Inject
        @Cache("numbers")
        private QueryMap<Integer, String> queryMap;

        @Inject
        @Cache("numbers")
        private CacheMap<Integer, String> cacheMap;

        NamedCache<Integer, String> getNamedCache()
            {
            return namedCache;
            }

        InvocableMap<Integer, String> getInvocableMap()
            {
            return invocableMap;
            }

        ObservableMap<Integer, String> getObservableMap()
            {
            return observableMap;
            }

        ConcurrentMap<Integer, String> getConcurrentMap()
            {
            return concurrentMap;
            }

        QueryMap<Integer, String> getQueryMap()
            {
            return queryMap;
            }

        CacheMap<Integer, String> getCacheMap()
            {
            return cacheMap;
            }
        }
    }
