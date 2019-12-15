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

/**
 * SheetToJava用独自プロパティ解析クラス
 * 
 * @since 1.0
 */
public abstract class SheetToJavaPropertyParser {

    /**
     * このパーサで処理するタグ
     */
    private String tag;

    /**
     * コンストラクタ
     * 
     * @param tag タグ
     */
    public SheetToJavaPropertyParser( String tag) {
        this.tag = tag;
    }

    /**
     * タグを取得する
     * 
     * @return このパーサで処理するタグ
     */
    public String getTag() {
        return tag;
    }

    /**
     * タグを設定する
     * 
     * @param tag このパーサで処理するタグ
     */
    public void setTag( String tag) {
        this.tag = tag;
    }

    /**
     * パース処理を行うか否かの判定
     * 
     * @param tagCell 対象セル
     * @return 処理対象の場合はtrue、処理対象外の場合はfalse
     * @throws ParseException パース例外
     */
    public boolean isParse( Cell tagCell) throws ParseException {
        if ( tagCell == null) {
            return false;
        }

        // 文字列かつ、タグを含むセルの場合は処理対象
        if ( tagCell.getCellType() == Cell.CELL_TYPE_STRING) {
            if ( tagCell.getStringCellValue().contains( tag)) {
                return true;
            }
        }
        return false;
    }

    /**
     * パース処理を行うか否かの判定
     * 
     * @param propertyName プロパティ名
     * @return 処理対象の場合はtrue、処理対象外の場合はfalse
     */
    public boolean isParse( String propertyName) {

        if ( propertyName == null) {
            return false;
        }

        if ( propertyName.contains( tag)) {
            return true;
        }

        return false;
    }

    /**
     * パース処理を行う
     * 
     * @param object 処理対象オブジェクト
     * @param paramCellMap パラメータとセルのマップ
     * @param paramValue パラメータと値のマップ
     * @throws ParseException パース例外
     */
    public abstract void parse( Object object, Map<String, Cell> paramCellMap, Map<String, Object> paramValueMap) throws ParseException;
}
