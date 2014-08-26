/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SheetToSqlSettingInfo.java 40 2009-11-12 04:51:10Z akira-yokoi $
 * $Revision: 40 $
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

import org.apache.poi.ss.usermodel.Cell;

/**
 * 解析するシートの詳細情報を保持するクラス
 * 
 * @since 1.0
 */
public class SheetToSqlSettingInfo {

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
     * 対象テーブル名
     */
    private String tableName = null;
    
    /**
     * 対象テーブル名のセル
     */
    private Cell tableNameCell = null;

    /**
     * 対象カラム名
     */
    private String columnName = null;
    
    /**
     * 対象カラム名のセル
     */
    private Cell columnNameCell = null;

    /**
     * 重複不可フラグ
     */
    private boolean isUnique = false;
    
    /**
     * 重複不可フラグのセル
     */
    private Cell uniqueCell = null;
    
    /**
     * データ型
     */
    private String dataType = null;
    
    /**
     * データ型のセル
     */
    private Cell dataTypeCell = null;

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
     * 対象データセルの取得
     * 
     * @return 対象データセル
     */
    public Cell getValueCell() {
        return valueCell;
    }

    /**
     * 対象データセルの設定
     * 
     * @param valueCell 対象データセル
     */
    public void setValueCell( Cell valueCell) {
        this.valueCell = valueCell;
    }

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
     * 対象テーブル名のセル取得
     * 
     * @return 対象テーブル名のセル
     */
    public Cell getTableNameCell() {
        return tableNameCell;
    }

    /**
     * 対象テーブル名のセル設定
     * 
     * @param tableNameCell 対象テーブル名のセル
     */
    public void setTableNameCell( Cell tableNameCell) {
        this.tableNameCell = tableNameCell;
    }

    /**
     * 対象カラム名を取得する
     * 
     * @return 対象カラム名
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * 対象カラム名を設定する
     * 
     * @param columnName 対象カラム名
     */
    public void setColumnName( String columnName) {
        this.columnName = columnName;
    }

    /**
     * 対象カラム名の取得
     * 
     * @return 対象カラム名
     */
    public Cell getColumnNameCell() {
        return columnNameCell;
    }

    /**
     * 対象カラム名の設定
     * 
     * @param columnNameCell 対象カラム名
     */
    public void setColumnNameCell( Cell columnNameCell) {
        this.columnNameCell = columnNameCell;
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
     * @param uniqueCell 重複不可フラグのセル
     */
    public void setUniqueCell( Cell uniqueCell) {
        this.uniqueCell = uniqueCell;
    }

    /**
     * データ型を取得する
     * 
     * @return データ型
     */
    public String getDataType() {
        return dataType;
    }

    /**
     * データ型を設定する
     * 
     * @param dataType データ型
     */
    public void setDataType( String dataType) {
        this.dataType = dataType;
    }

    /**
     * データ型のセルを取得する
     * 
     * @return データ型のセル
     */
    public Cell getDataTypeCell() {
        return dataTypeCell;
    }

    /**
     * データ型のセル設定
     * 
     * @param dataTypeCell データ型のセル
     */
    public void setDataTypeCell( Cell dataTypeCell) {
        this.dataTypeCell = dataTypeCell;
    }
}
