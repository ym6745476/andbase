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
package com.ab.task;



// TODO: Auto-generated Javadoc

/**
 * © 2012 amsoft.cn
 * 名称：AbTaskObjectListener.java 
 * 描述：数据执行的接口.
 *
 * @author 还如一梦中
 * @version v1.0
 * @date：2011-12-10 下午11:52:13
 */
public abstract class AbTaskObjectListener extends AbTaskListener{
	
	/**
	 * Gets the object.
	 *
	 * @param <T> the generic type
	 * @return 返回的结果对象
	 */
    public abstract <T> T getObject();
    
    /**
     * 描述：执行开始后调用.
     *
     * @param <T> the generic type
     * @param obj the obj
     */
    public abstract <T> void update(T obj); 
    
	
}
