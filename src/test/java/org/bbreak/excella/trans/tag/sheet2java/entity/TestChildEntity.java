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

/**
 * テスト用子エンティティ 
 *
 * @since 1.0
 */
public class TestChildEntity {

    /**
     * 文字列プロパティ1
     */
    private String childPropertyStr1 = null;
    
    /**
     * 整数プロパティ1
     */
    private Integer childPropertyInt1 = null;
    
    public String getChildPropertyStr1() {
        return childPropertyStr1;
    }
    public void setChildPropertyStr1( String childPropertyStr1) {
        this.childPropertyStr1 = childPropertyStr1;
    }
    public Integer getChildPropertyInt1() {
        return childPropertyInt1;
    }
    public void setChildPropertyInt1( Integer childPropertyInt1) {
        this.childPropertyInt1 = childPropertyInt1;
    }
}
