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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.bbreak.excella.core.exception.ParseException;

/**
 * SheetToJava用プロパティ設定処理リスナ
 * プロパティ設定の前後に呼ばれるメソッドを持ち、プロパティ設定前に値の検証処理を
 * 行いたい場合に利用する。
 * 
 * @since 1.1
 */
public interface SheetToJavaListener {

    /**
     * 行処理前に任意の処理を差し込む
     * falseを返すと対象行は処理対象外とみなし、スキップする
     * 
     * @param row 行
     * @return true=処理対象、false=処理対象外
     */
    boolean preProcessRow( Row row) throws ParseException;
    
    /**
     * 行処理後に任意の処理を差し込む
     * falseを返すと対象行の結果は無効とみなし、スキップする
     * 
     * @param row 行
     * @param obj 行を処理した結果のオブジェクト
     * @return true=処理対象、false=処理対象外
     */
    boolean postProcessRow( Row row, Object obj) throws ParseException;
    
    /**
     * プロパティ設定前に任意の処理を差し込む
     * 
     * @param valueCell 値の入ったセル
     * @param obj プロパティ設定対象オブジェクト
     * @param propertyName プロパティ名
     * @param value 値
     * @throws ParseException 検証エラーの場合
     */
    void preSetProperty( Cell valueCell, Object obj, String propertyName, Object value) throws ParseException;
    
    /**
     * プロパティ設定前に任意の処理を差し込む
     * 
     * @param valueCell 値の入ったセル
     * @param obj プロパティ設定対象オブジェクト
     * @param propertyName プロパティ名
     * @param value 値
     * @throws ParseException 検証エラーの場合
     */
    void postSetProperty( Cell valueCell, Object obj, String propertyName, Object value) throws ParseException;
}
