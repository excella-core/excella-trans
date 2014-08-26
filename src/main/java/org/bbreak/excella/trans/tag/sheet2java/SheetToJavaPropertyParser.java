/*************************************************************************
 *
 * Copyright 2009 by bBreak Systems.
 *
 * ExCella Trans - Excelファイルを利用したデータ移行支援ツール
 *
 * $Id: SheetToJavaPropertyParser.java 59 2009-11-18 04:25:47Z akira-yokoi $
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
