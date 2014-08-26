/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SheetToJavaParseInfo.java 40 2009-11-12 04:51:10Z akira-yokoi $
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
package org.bbreak.excella.trans.tag.sheet2java.model;

import org.apache.poi.ss.usermodel.Cell;

/**
 * シート -> Javaオブジェクト変換のシート情報を保持するクラス
 * 
 * @since 1.0
 */
public class SheetToJavaParseInfo {

    /**
     * 対応する設定タグ名
     */
    private String settingTagName;

    /**
     * 設定名のセル
     */
    private Cell settingTagNameCell;

    /**
     * 対象シート名
     */
    private String sheetName;

    /**
     * 対象シート名のセル
     */
    private Cell sheetNameCell;

    /**
     * 対象シートの論理名が定義された行番号
     */
    private Integer logicalNameRowNum;

    /**
     * 対象シートの論理名の行番号セル
     */
    private Cell logicalNameRowNumCell;

    /**
     * 対象シートのデータ開始の行番号
     */
    private Integer valueRowNum;

    /**
     * 対象シートのデータ開始の行番号セル
     */
    private Cell valueRowNumCell;

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
     * 設定タグ名のセル取得
     * 
     * @return 設定タグ名のセル
     */
    public Cell getSettingTagNameCell() {
        return settingTagNameCell;
    }

    /**
     * 設定タグ名のセル設定
     * 
     * @param settingTagNameCell 設定タグ名のセル
     */
    public void setSettingTagNameCell( Cell settingTagNameCell) {
        this.settingTagNameCell = settingTagNameCell;
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
     * 対象シートの論理名が定義されたセルの取得
     * 
     * @return 対象シートの論理名が定義されたセル
     */
    public Cell getLogicalNameRowNumCell() {
        return logicalNameRowNumCell;
    }

    /**
     * 対象シートの論理名が定義されたセルの設定
     * 
     * @param logicalNameRowNumCell 対象シートの論理名が定義されたセルの設定
     */
    public void setLogicalNameRowNumCell( Cell logicalNameRowNumCell) {
        this.logicalNameRowNumCell = logicalNameRowNumCell;
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

    /**
     * 対象シートのデータ開始行のセル取得
     * 
     * @return 対象シートのデータ開始行のセル
     */
    public Cell getValueRowNumCell() {
        return valueRowNumCell;
    }

    /**
     * 対象シートのデータ開始行のセル設定
     * 
     * @param valueRowNumCell 対象シートのデータ開始行のセル
     */
    public void setValueRowNumCell( Cell valueRowNumCell) {
        this.valueRowNumCell = valueRowNumCell;
    }

}
