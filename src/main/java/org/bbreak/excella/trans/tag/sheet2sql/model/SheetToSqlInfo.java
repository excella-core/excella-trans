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

import java.util.List;
import java.util.Map;

/**
 * SQL作成に必要な情報を保持するクラス
 * 
 * @since 1.0
 */
public class SheetToSqlInfo {

    /**
     * 対象テーブル名
     */
    private String tableName = null;

    /**
     * 対象カラム・値のマップ
     */
    private Map<String, String> columnValueMap = null;

    /**
     * 対象カラムのリスト
     */
    private List<String> columnNameList = null;

    /**
     * 対象テーブル名を取得する
     * 
     * @return 対象テーブル名
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * 対象テーブル名を設定する
     * 
     * @param tableName 対象テーブル名
     */
    public void setTableName( String tableName) {
        this.tableName = tableName;
    }

    /**
     * 対象カラム・値のマップを取得する
     * 
     * @return 対象カラム・値のマップ
     */
    public Map<String, String> getColumnValueMap() {
        return columnValueMap;
    }

    /**
     * 対象カラム・値のマップを設定する
     * 
     * @param columnValueMap 対象カラム・値のマップ
     */
    public void setColumnValueMap( Map<String, String> columnValueMap) {
        this.columnValueMap = columnValueMap;
    }

    /**
     * 対象カラムのリストを取得する
     * 
     * @return 対象カラムのリスト
     */
    public List<String> getColumnNameList() {
        return columnNameList;
    }

    /**
     * 対象カラムのリストを設定する
     * 
     * @param columnNameList 対象カラムのリスト
     */
    public void setColumnNameList( List<String> columnNameList) {
        this.columnNameList = columnNameList;
    }
}
