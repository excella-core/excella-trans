/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: TestChildEntityParser.java 59 2009-11-18 04:25:47Z akira-yokoi $
 * $Revision: 59 $
 *
 * This file is part of ExCella Trans.
 *
 * ExCella Trans is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License version 3
 * only, as published by the Free Software Foundation.
 *
 * ExCella Trans is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License version 3 for more details
 * (a copy is included in the COPYING.LESSER file that accompanied this code).
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with ExCella Trans.  If not, see
 * <http://www.gnu.org/licenses/lgpl-3.0-standalone.html>
 * for a copy of the LGPLv3 License.
 *
 ************************************************************************/
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
    @SuppressWarnings( "unchecked")
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
