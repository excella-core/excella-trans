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

package org.bbreak.excella.trans.tag.sheet2sql.model;

/**
 * シート -> Sql変換のシート情報を保持するクラス
 * 
 * @since 1.0
 */
public class SheetToSqlParseInfo {

    /**
     * 対応する設定タグ名
     */
    private String settingTagName;

    /**
     * 対象シート名
     */
    private String sheetName;

    /**
     * 対象シートの論理名が定義された行番号
     */
    private Integer logicalNameRowNum;

    /**
     * 対象シートのデータ開始の行番号
     */
    private Integer valueRowNum;

    /**
     * 対応する設定タグ名を取得する
     * 
     * @return 対応する設定タグ名
     */
    public String getSettingTagName() {
        return settingTagName;
    }

    /**
     * 対応する設定タグ名を設定する
     * 
     * @param settingTagName 対応する設定タグ名
     */
    public void setSettingTagName( String settingTagName) {
        this.settingTagName = settingTagName;
    }

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
     * 対象シートの論理名が定義された行番号を取得する
     * 
     * @return 対象シートの論理名が定義された行番号
     */
    public Integer getLogicalNameRowNum() {
        return logicalNameRowNum;
    }

    /**
     * 対象シートの論理名が定義された行番号を設定する
     * 
     * @param logicalNameRowNum 対象シートの論理名が定義された行番号
     */
    public void setLogicalNameRowNum( Integer logicalNameRowNum) {
        this.logicalNameRowNum = logicalNameRowNum;
    }

    /**
     * 対象シートのデータ開始の行番号を取得する
     * 
     * @return 対象シートのデータ開始の行番号
     */
    public Integer getValueRowNum() {
        return valueRowNum;
    }

    /**
     * 対象シートのデータ開始の行番号を設定する
     * 
     * @param valueRowNum 対象シートのデータ開始の行番号
     */
    public void setValueRowNum( Integer valueRowNum) {
        this.valueRowNum = valueRowNum;
    }
}
