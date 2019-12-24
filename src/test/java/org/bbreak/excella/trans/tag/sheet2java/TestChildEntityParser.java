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

package org.bbreak.excella.trans.tag.sheet2java;

import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.bbreak.excella.core.exception.ParseException;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestChildEntity;
import org.bbreak.excella.trans.tag.sheet2java.entity.TestEntity1;

/**
 * SheetToJavaExecuterテスト用カスタムプロパティパーサ
 * 
 * @since 1.0
 */
public class TestChildEntityParser extends SheetToJavaPropertyParser {

    /**
     * コンストラクタ
     */
    public TestChildEntityParser() {
        super( "@TestChildEntity");
    }

    @Override
    public void parse( Object object, Map<String, Cell> paramCellMap, Map<String, Object> paramValueMap) throws ParseException {
        TestEntity1 testEntity = ( TestEntity1) object;

        TestChildEntity child = new TestChildEntity();
        child.setChildPropertyStr1( ( String) paramValueMap.get( "childPropertyStr1"));

        Object value = paramValueMap.get( "childPropertyInt1");
        if ( value != null) {
            Double valueDouble = Double.parseDouble( value.toString());
            Integer valueInt = valueDouble.intValue();
            child.setChildPropertyInt1( valueInt);
        }

        testEntity.setChild( child);
    }
}
