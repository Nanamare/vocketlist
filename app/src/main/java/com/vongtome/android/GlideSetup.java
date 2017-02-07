package com.vongtome.android;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.model.GenericLoaderFactory;
import com.bumptech.glide.load.model.Headers;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.stream.BaseGlideUrlLoader;
import com.bumptech.glide.load.model.stream.StreamModelLoader;
import com.bumptech.glide.module.GlideModule;
import com.vongtome.android.common.util.UserAgentUtility;

import java.io.InputStream;

public class GlideSetup implements GlideModule {
    private static final String CACHE_DIR_NAME = "glide";
    private static final int DISK_CACHE_SIZE = 1024 * 1000 * 60; // 60 Mbyte

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        // disk cache setting
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, CACHE_DIR_NAME, DISK_CACHE_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        glide.register(String.class, InputStream.class, new HeaderedLoader.Factory());
    }

    public static class HeaderedLoader extends BaseGlideUrlLoader<String> {
        public HeaderedLoader(Context context) {
            super(context);
        }

        @Override
        protected String getUrl(String model, int width, int height) {
            return model;
        }

        @Override
        protected Headers getHeaders(String model, int width, int height) {
            return new LazyHeaders.Builder()
                    .addHeader("User-Agent", UserAgentUtility.getUserAgentString())
                    .addHeader("Connection", "close")
                    .addHeader("Accept", "*/*")
                    .addHeader("Accept-Language", "ko")
                    .build();
        }

        public static class Factory implements ModelLoaderFactory<String, InputStream> {
            @Override
            public StreamModelLoader<String> build(Context context, GenericLoaderFactory factories) {
                return new HeaderedLoader(context);
            }
            @Override
            public void teardown() { /* nothing to free */ }
        }
    }
}