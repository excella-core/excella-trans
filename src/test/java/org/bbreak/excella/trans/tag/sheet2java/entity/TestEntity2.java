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
public class TestEntity2 {

    /**
     * 文字列プロパティ2
     */
    private String propertyStr2 = null;
    
    /**
     * 整数プロパティ2
     */
    private Integer propertyInt2 = null;
    
    /**
     * 日付プロパティ2
     */
    private Date propertyDate2 = null;
    
    public String getPropertyStr2() {
        return propertyStr2;
    }
    public void setPropertyStr2( String propertyStr2) {
        this.propertyStr2 = propertyStr2;
    }
    public Integer getPropertyInt2() {
        return propertyInt2;
    }
    public void setPropertyInt2( Integer propertyInt2) {
        this.propertyInt2 = propertyInt2;
    }
    public Date getPropertyDate2() {
        return propertyDate2;
    }
    public void setPropertyDate2( Date propertyDate2) {
        this.propertyDate2 = propertyDate2;
    }
}
