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

package org.bbreak.excella.trans.tag.sheet2java.model;

import org.apache.poi.ss.usermodel.Cell;

/**
 * 解析するシートの詳細情報を保持するクラス
 * 
 * @since 1.0
 */
public class SheetToJavaSettingInfo {

    /**
     * 対象シート名
     */
    private String sheetName = null;

    /**
     * 対象シート名のセル
     */
    private Cell sheetNameCell = null;

    /**
     * 対象データ
     */
    private Object value = null;

    /**
     * 対象データのセル
     */
    private Cell valueCell = null;

    /**
     * 対象クラス
     */
    private Class<?> clazz = null;

    /**
     * 対象クラスのセル
     */
    private Cell clazzCell = null;

    /**
     * 対象プロパティ名
     */
    private String propertyName = null;

    /**
     * 対象プロパティ名のセル
     */
    private Cell propertyNameCell = null;

    /**
     * 重複不可フラグ
     */
    private boolean isUnique = false;

    /**
     * 重複不可フラグのセル
     */
    private Cell uniqueCell = null;

    /**
     * 対象シート名を取得する
     * 
     * @return 対象シート名
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * 対象シート名を設定する
     * 
     * @param sheetName 対象シート名
     */
    public void setSheetName( String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 対象シート名のセル取得
     * 
     * @return 対象シート名のセル
     */
    public Cell getSheetNameCell() {
        return sheetNameCell;
    }

    /**
     * 対象シート名のセル設定
     * 
     * @param sheetNameCell 対象シート名のセル
     */
    public void setSheetNameCell( Cell sheetNameCell) {
        this.sheetNameCell = sheetNameCell;
    }

    /**
     * 対象データを取得する
     * 
     * @return 対象データ
     */
    public Object getValue() {
        return value;
    }

    /**
     * 対象データを設定する
     * 
     * @param value 対象データ
     */
    public void setValue( Object value) {
        this.value = value;
    }

    /**
     * 対象データのセル取得
     * 
     * @return 対象データのセル
     */
    public Cell getValueCell() {
        return valueCell;
    }

    /**
     * 対象データのセル設定
     * 
     * @param valueCell 対象データのセル
     */
    public void setValueCell( Cell valueCell) {
        this.valueCell = valueCell;
    }

    /**
     * 対象クラスを取得する
     * 
     * @return 対象クラス
     */
    public Class<?> getClazz() {
        return clazz;
    }

    /**
     * 対象クラスを設定する
     * 
     * @param clazz 対象クラス
     */
    public void setClazz( Class<?> clazz) {
        this.clazz = clazz;
    }
    
    /**
     * 対象クラスのセル取得
     * 
     * @return 対象クラスのセル
     */
    public Cell getClazzCell() {
        return clazzCell;
    }

    /**
     * 対象クラスのセル設定
     * 
     * @param clazzCell 対象クラスのセル
     */
    public void setClazzCell( Cell clazzCell) {
        this.clazzCell = clazzCell;
    }

    /**
     * 対象プロパティ名を取得する
     * 
     * @return 対象プロパティ名
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 対象プロパティ名を設定する
     * 
     * @param propertyName 対象プロパティ名
     */
    public void setPropertyName( String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 対象プロパティのセル取得
     * 
     * @return 対象プロパティのセル
     */
    public Cell getPropertyNameCell() {
        return propertyNameCell;
    }

    /**
     * 対象プロパティのセル設定
     * 
     * @param propertyNameCell 対象プロパティのセル
     */
    public void setPropertyNameCell( Cell propertyNameCell) {
        this.propertyNameCell = propertyNameCell;
    }

    /**
     * 重複不可フラグを取得する
     * 
     * @return 重複不可フラグ
     */
    public boolean isUnique() {
        return isUnique;
    }

    /**
     * 重複不可フラグを設定する
     * 
     * @param isUnique 重複不可フラグ
     */
    public void setUnique( boolean isUnique) {
        this.isUnique = isUnique;
    }

    /**
     * 重複不可フラグのセル取得
     * 
     * @return 重複不可フラグのセル
     */
    public Cell getUniqueCell() {
        return uniqueCell;
    }

    /**
     * 重複不可フラグのセル設定
     * 
     * @param isUniqueCell 重複不可フラグのセル
     */
    public void setUniqueCell( Cell uniqueCell) {
        this.uniqueCell = uniqueCell;
    }
}
