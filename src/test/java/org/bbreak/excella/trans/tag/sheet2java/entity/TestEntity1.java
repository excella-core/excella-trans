/*-
 * #%L
 * excella-trans
 * %%
 * Copyright (C) 2009 - 2019 bBreak Systems and contributors
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

package org.bbreak.excella.trans.tag.sheet2java.entity;

import java.util.Date;

/**
 * テスト用エンティティ
 *
 * @since 1.0
 */
public class TestEntity1 {

    /**
     * 文字列プロパティ1
     */
    private String propertyStr1 = null;

    /**
     * 整数プロパティ1
     */
    private Integer propertyInt1 = null;
    
    /**
     * 日付プロパティ1
     */
    private Date propertyDate1 = null;
    
    /**
     * 子エンティティ
     */
    private TestChildEntity child = null;
    
    public String getPropertyStr1() {
        return propertyStr1;
    }
    public void setPropertyStr1( String propertyStr1) {
        this.propertyStr1 = propertyStr1;
    }
    public Integer getPropertyInt1() {
        return propertyInt1;
    }
    public void setPropertyInt1( Integer propertyInt1) {
        this.propertyInt1 = propertyInt1;
    }
    public Date getPropertyDate1() {
        return propertyDate1;
    }
    public void setPropertyDate1( Date propertyDate1) {
        this.propertyDate1 = propertyDate1;
    }
    public TestChildEntity getChild() {
        return child;
    }
    public void setChild( TestChildEntity child) {
        this.child = child;
    }
}
