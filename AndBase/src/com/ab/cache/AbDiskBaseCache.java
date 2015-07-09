/*
 * Copyright (C) 2012 www.amsoft.cn
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ab.cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import android.os.SystemClock;

import com.ab.global.AbAppConfig;
import com.ab.util.AbLogUtil;
import com.ab.util.AbStreamUtil;

// TODO: Auto-generated Javadoc
/**
 * 
 * © 2012 amsoft.cn
 * 名称：AbDiskBasedCache.java 
 * 描述：磁盘缓存
 * @author 还如一梦中
 * @date 2015年4月3日 上午9:59:42
 * @version v1.0
 */
public class AbDiskBaseCache implements AbDiskCache {

    /**  所有缓存文件. */
    private final Map<String, CacheHeader> mEntries =
            new LinkedHashMap<String, CacheHeader>(16, .75f, true);

    /** 当前缓存大小. */
    private long mTotalSize = 0;

    /** 缓存根目录. */
    private final File mRootDirectory;

    /** 最大缓存字节数. */
    private final int mMaxCacheSizeInBytes;

    /**  缓存达到高水品的百分比. */
    private static final float HYSTERESIS_FACTOR = 0.9f;

    /** 文件头标识. */
    private static final int CACHE_MAGIC = 0x20120504;

    /**
     * 指定缓存空间的构造.
     * @param rootDirectory The root directory of the cache.
     * @param maxCacheSizeInBytes The maximum size of the cache in bytes.
     */
    public AbDiskBaseCache(File rootDirectory, int maxCacheSizeInBytes) {
        mRootDirectory = rootDirectory;
        mMaxCacheSizeInBytes = maxCacheSizeInBytes;
        initialize();
    }

    /**
     * 默认缓存空间的构造.
     * the default maximum cache size of 5MB.
     * @param rootDirectory The root directory of the cache.
     */
    public AbDiskBaseCache(File rootDirectory) {
        this(rootDirectory, AbAppConfig.MAX_DISK_USAGE_INBYTES);
    }
    
    /**
     * 初始化磁盘缓存文件.
     */
    @Override
    public synchronized void initialize() {
        if (!mRootDirectory.exists()) {
            if (!mRootDirectory.mkdirs()) {
                AbLogUtil.e(AbDiskBaseCache.class,"缓存目录创建失败，"+mRootDirectory.getAbsolutePath());
            }
            return;
        }

        File[] files = mRootDirectory.listFiles();
        if (files == null) {
            return;
        }
        for (File file : files) {
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(file);
                CacheHeader entry = CacheHeader.readHeader(fis);
                entry.size = file.length();
                putEntry(entry.key, entry);
            } catch (Exception e) {
                if (file != null) {
                   file.delete();
                }
            } finally {
                try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (Exception e) { 
                }
            }
        }
    }

    /**
     * 清空所有磁盘缓存.
     */
    @Override
    public synchronized void clear() {
        File[] files = mRootDirectory.listFiles();
        if (files != null) {
            for (File file : files) {
                file.delete();
            }
        }
        mEntries.clear();
        mTotalSize = 0;
        AbLogUtil.d(AbDiskBaseCache.class,"Cache cleared.");
    }

    /**
     * 获取缓存实体.
     *
     * @param key the key
     * @return the entry
     */
    @Override
    public synchronized Entry get(String key) {
        CacheHeader entry = mEntries.get(key);
        // if the entry does not exist, return.
        if (entry == null) {
            return null;
        }

        File file = getFileForKey(key);
        AbLogUtil.d(AbDiskBaseCache.class, "想要从缓存中获取文件"+file.getAbsolutePath());
        CountingInputStream cis = null;
        try {
            cis = new CountingInputStream(new FileInputStream(file));
            CacheHeader.readHeader(cis); // eat header
            byte[] data = AbStreamUtil.stream2Bytes(cis, (int) (file.length() - cis.bytesRead));
            return entry.toCacheEntry(data);
        } catch (Exception e) {
        	e.printStackTrace();
            remove(key);
            return null;
        } finally {
            if (cis != null) {
                try {
                    cis.close();
                } catch (Exception ioe) {
                	ioe.printStackTrace();
                    return null;
                }
            }
        }
    }

    /**
     * 添加实体到缓存.
     *
     * @param key the key
     * @param entry the entry
     */
    @Override
    public synchronized void put(String key, Entry entry) {
        pruneIfNeeded(entry.data.length);
        File file = getFileForKey(key);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            CacheHeader e = new CacheHeader(key, entry);
            e.writeHeader(fos);
            fos.write(entry.data);
            fos.close();
            putEntry(key, e);
            return;
        } catch (IOException e) {
        }
        boolean deleted = file.delete();
        if (!deleted) {
            AbLogUtil.d(AbDiskBaseCache.class,"缓存文件删除失败"+file.getAbsolutePath());
        }
    }

    /**
     * 从缓存中移除实体.
     *
     * @param key the key
     */
    @Override
    public synchronized void remove(String key) {
        boolean deleted = getFileForKey(key).delete();
        removeEntry(key);
        if (!deleted) {
            AbLogUtil.d(AbDiskBaseCache.class,"缓存文件删除失败");
        }
    }

    /**
     * 从key中生成文件名.
     * @param key The key to generate a file name for.
     * @return A pseudo-unique filename.
     */
    private String getFileNameForKey(String key) {
        int firstHalfLength = key.length() / 2;
        String localFilename = String.valueOf(key.substring(0, firstHalfLength).hashCode());
        localFilename += String.valueOf(key.substring(firstHalfLength).hashCode());
        return localFilename;
    }

    /**
     * 从key中得到文件.
     *
     * @param key the key
     * @return the file for key
     */
    public File getFileForKey(String key) {
        return new File(mRootDirectory, getFileNameForKey(key));
    }

    /**
     * Prunes the cache to fit the amount of bytes specified.
     * @param neededSpace The amount of bytes we are trying to fit into the cache.
     */
    private void pruneIfNeeded(int neededSpace) {
    	//可以缓存
        if ((mTotalSize + neededSpace) < mMaxCacheSizeInBytes) {
            return;
        }

        //释放部分空间
        long before = mTotalSize;
        int prunedFiles = 0;
        long startTime = SystemClock.elapsedRealtime();

        Iterator<Map.Entry<String, CacheHeader>> iterator = mEntries.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, CacheHeader> entry = iterator.next();
            CacheHeader e = entry.getValue();
            //删除
            boolean deleted = getFileForKey(e.key).delete();
            if (deleted) {
                mTotalSize -= e.size;
            } else {
               AbLogUtil.d(AbDiskBaseCache.class,"Could not delete cache entry for key=%s, filename=%s",
                       e.key, getFileNameForKey(e.key));
            }
            iterator.remove();
            prunedFiles++;
            
            //删除缓存到这个级别
            if ((mTotalSize + neededSpace) < mMaxCacheSizeInBytes * HYSTERESIS_FACTOR) {
                break;
            }
        }

        if (AbLogUtil.D) {
        	AbLogUtil.d(AbDiskBaseCache.class,"pruned %d files, %d bytes, %d ms",
                    prunedFiles, (mTotalSize - before), SystemClock.elapsedRealtime() - startTime);
        }
    }

    /**
     * 将实体加入到缓存中.
     * @param key The key to identify the entry by.
     * @param entry The entry to cache.
     */
    private void putEntry(String key, CacheHeader entry) {
        if (!mEntries.containsKey(key)) {
            mTotalSize += entry.size;
        } else {
            CacheHeader oldEntry = mEntries.get(key);
            mTotalSize += (entry.size - oldEntry.size);
        }
        mEntries.put(key, entry);
    }

    /**
     * 从缓存中移除某个实体.
     *
     * @param key the key
     */
    private void removeEntry(String key) {
        CacheHeader entry = mEntries.get(key);
        if (entry != null) {
            mTotalSize -= entry.size;
            mEntries.remove(key);
        }
    }

    /**
     * 缓存头部信息.
     */
    static class CacheHeader {
    	
        /** 内容大小 */
        public long size;

        /** 实体的key. */
        public String key;

        /** ETag仅仅是一个和文件相关的标记. */
        public String etag;

        /** 缓存时间 总毫秒数. */
        public long serverTimeMillis;

        /** 失效日期 总毫秒数. */
        public long expiredTimeMillis;

        /** 响应头信息. */
        public Map<String, String> responseHeaders;

        /**
         * 构造.
         */
        private CacheHeader() { }

        /**
         * 构造.
         *
         * @param key The key that identifies the cache entry
         * @param entry The cache entry.
         */
        public CacheHeader(String key, Entry entry) {
            this.key = key;
            this.size = entry.data.length;
            this.etag = entry.etag;
            this.serverTimeMillis = entry.serverTimeMillis;
            this.expiredTimeMillis = entry.expiredTimeMillis;
            this.responseHeaders = entry.responseHeaders;
        }

        /**
         * Reads the header off of an InputStream and returns a CacheHeader object.
         *
         * @param is The InputStream to read from.
         * @return the cache header
         * @throws IOException Signals that an I/O exception has occurred.
         */
        public static CacheHeader readHeader(InputStream is) throws IOException {
            CacheHeader entry = new CacheHeader();
            int magic = AbStreamUtil.readInt(is);
            if (magic != CACHE_MAGIC) {
                // don't bother deleting, it'll get pruned eventually
                throw new IOException();
            }
            entry.key = AbStreamUtil.readString(is);
            entry.etag = AbStreamUtil.readString(is);
            if (entry.etag.equals("")) {
                entry.etag = null;
            }
            entry.serverTimeMillis = AbStreamUtil.readLong(is);
            entry.expiredTimeMillis = AbStreamUtil.readLong(is);
            entry.responseHeaders = AbStreamUtil.readStringStringMap(is);
            return entry;
        }

        /**
         * Creates a cache entry for the specified data.
         *
         * @param data the data
         * @return the entry
         */
        public Entry toCacheEntry(byte[] data) {
            Entry e = new Entry();
            e.data = data;
            e.etag = etag;
            e.serverTimeMillis = serverTimeMillis;
            e.expiredTimeMillis = expiredTimeMillis;
            e.responseHeaders = responseHeaders;
            return e;
        }


        /**
         * Writes the contents of this CacheHeader to the specified OutputStream.
         *
         * @param os the os
         * @return true, if successful
         */
        public boolean writeHeader(OutputStream os) {
            try {
            	AbStreamUtil.writeInt(os, CACHE_MAGIC);
            	AbStreamUtil.writeString(os, key);
            	AbStreamUtil.writeString(os, etag == null ? "" : etag);
            	AbStreamUtil.writeLong(os, serverTimeMillis);
            	AbStreamUtil.writeLong(os, expiredTimeMillis);
            	AbStreamUtil.writeStringStringMap(responseHeaders, os);
                os.flush();
                return true;
            } catch (IOException e) {
                AbLogUtil.d(AbDiskBaseCache.class,"%s", e.toString());
                return false;
            }
        }

    }

    /**
     * 计数.
     */
    private static class CountingInputStream extends FilterInputStream {
        
        /** The bytes read. */
        private int bytesRead = 0;

        private CountingInputStream(InputStream in) {
            super(in);
        }
        
        @Override
        public int read() throws IOException {
            int result = super.read();
            if (result != -1) {
                bytesRead++;
            }
            return result;
        }
        
        @Override
        public int read(byte[] buffer, int offset, int count) throws IOException {
            int result = super.read(buffer, offset, count);
            if (result != -1) {
                bytesRead += result;
            }
            return result;
        }
    }

}
