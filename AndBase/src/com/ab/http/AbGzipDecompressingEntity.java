package com.ab.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.GZIPInputStream;
import org.apache.http.entity.HttpEntityWrapper;
import org.apache.http.HttpEntity;
/**
 * © 2012 amsoft.cn
 * 名称：AbGzipDecompressingEntity.java 
 * 描述：Http解压内容
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2014-06-17 上午10:19:52
 */
public class AbGzipDecompressingEntity extends HttpEntityWrapper{
    
    public AbGzipDecompressingEntity(final HttpEntity entity){
        super(entity);
    }

    public InputStream getContent() throws IOException, IllegalStateException{
        InputStream wrappedin = wrappedEntity.getContent();
        return new GZIPInputStream(wrappedin);
    }

    public long getContentLength(){
        return -1;
    }
}