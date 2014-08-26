/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SheetToSqlInfo.java 2 2009-06-22 04:48:53Z yuta-takahashi $
 * $Revision: 2 $
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
